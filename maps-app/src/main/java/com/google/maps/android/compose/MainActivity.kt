package com.google.maps.android.compose

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.theme.MapsComposeSampleTheme
import com.huawei.hms.maps.model.LatLng

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )

        setContent {
            MapsComposeSampleTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                ) { paddingValues ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val singapore = LatLng(1.35, 103.87)
                        val cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(singapore, 10f)
                        }
                        HuaweiMap(
                            modifier  = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                        ) {
                            Marker(
                                state = rememberUpdatedMarkerState(singapore)
                            )
                        }
                    }
                }
            }
        }
    }
}
