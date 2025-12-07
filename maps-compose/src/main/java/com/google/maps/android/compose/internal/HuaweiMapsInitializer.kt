package com.google.maps.android.compose.internal

import android.content.Context
import android.os.StrictMode
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import com.huawei.hms.maps.MapsInitializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/** Enum representing the initialization state of the Google Maps SDK. */
public enum class InitializationState {
  /** The SDK has not been initialized. */
  UNINITIALIZED,

  /** The SDK is currently being initialized. */
  INITIALIZING,

  /** The SDK has been successfully initialized. */
  SUCCESS,

  /** The SDK initialization failed. */
  FAILURE
}

public interface HuaweiMapsInitializer {
  public val state: State<InitializationState>

  /**
   * Initializes Huawei Maps. This function must be called before using any other functions in this
   * library.
   *
   * If initialization fails with a recoverable error (e.g., a network issue), the state will be
   * reset to [InitializationState.UNINITIALIZED], allowing for a subsequent retry. In the case of
   * an unrecoverable error (e.g., a missing manifest value), the state will be set to
   * [InitializationState.FAILURE] and the original exception will be re-thrown.
   *
   * @param context The context to use for initialization.
   * @param forceInitialization When true, initialization will be attempted even if it has already
   * succeeded or is in progress. This is useful for retrying a previously failed initialization.
   */
  public suspend fun initialize(
    context: Context,
    forceInitialization: Boolean = false,
  )

  /**
   * Resets the initialization state.
   *
   * This function cancels any ongoing initialization and resets the state to `UNINITIALIZED`. This
   * is primarily useful in test environments where the SDK might need to be re-initialized multiple
   * times.
   */
  public suspend fun reset()
}

/**
 * The default implementation of [HuaweiMapsInitializer].
 *
 * @param ioDispatcher The dispatcher to use for IO operations.
 */
public class DefaultHuaweiMapsInitializer(
  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
  private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
) : HuaweiMapsInitializer {
  private val _state = mutableStateOf(InitializationState.UNINITIALIZED)
  override val state: State<InitializationState> = _state
  private val mutex = Mutex()

  override suspend fun initialize(
    context: Context,
    forceInitialization: Boolean,
  ) {
    try {
      if (!forceInitialization &&
          (_state.value == InitializationState.INITIALIZING ||
            _state.value == InitializationState.SUCCESS)
      ) {
        return
      }

      mutex.withLock {
        if (_state.value != InitializationState.UNINITIALIZED) {
          return
        }
        _state.value = InitializationState.INITIALIZING
      }

      withContext(mainDispatcher) {
        val policy = StrictMode.getThreadPolicy()
        try {
          StrictMode.allowThreadDiskReads()
          MapsInitializer.initialize(context)
          _state.value = InitializationState.SUCCESS
        } finally {
          StrictMode.setThreadPolicy(policy)
        }
      }
    } catch (e: Exception) {
      // This could be a transient error.
      // Reset to UNINITIALIZED to allow for a retry.
      _state.value = InitializationState.UNINITIALIZED
      throw e
    }
  }

  override suspend fun reset() {
    mutex.withLock { _state.value = InitializationState.UNINITIALIZED }
  }
}

/** CompositionLocal that provides a [HuaweiMapsInitializer]. */
public val LocalHuaweiMapsInitializer: ProvidableCompositionLocal<HuaweiMapsInitializer> =
    compositionLocalOf {
  // Default implementation of the initializer
  DefaultHuaweiMapsInitializer()
}
