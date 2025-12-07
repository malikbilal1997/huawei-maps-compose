package com.google.maps.android.compose

import androidx.annotation.RestrictTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.remember
import com.huawei.hms.maps.HuaweiMap

/**
 * Returns a lambda that, when invoked, will reattach click listeners set by the [MapApplier] on the
 * [HuaweiMap]. Used for working around other functionality that modifies those click listeners,
 * such as clustering.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
@Composable
public fun rememberReattachClickListenersHandle(): () -> Unit {
  val map = currentComposer.applier as MapApplier
  return remember(map) { { map.attachClickListeners() } }
}
