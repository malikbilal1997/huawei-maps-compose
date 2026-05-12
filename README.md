# Huawei Maps Compose SDK

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](https://developer.android.com)

A professional Jetpack Compose library for Huawei Maps (HMS Map Kit). This SDK allows you to easily integrate Huawei Maps into your Android applications using a Compose-first architecture, following the same patterns as the official Google Maps Compose library.

## 🚀 Introduction

**Huawei Maps Compose** provides a declarative way to build map-based UIs on Huawei devices. It bridges the gap between the imperative Huawei Map SDK and Jetpack Compose, offering a seamless developer experience for HMS-core applications.

Whether you are building a dedicated HMS app or a multi-flavor application supporting both GMS and HMS, this SDK provides the tools needed to maintain a clean, modern codebase.

## ✨ Features

- 🧩 **Compose-first:** Fully declarative API for map initialization and UI components.
- 📍 **Markers:** Add and customize markers with ease.
- 🎥 **Camera Control:** Sophisticated camera state management with `CameraPositionState`.
- 📐 **Shapes:** Support for Polylines, Polygons, and Circles.
- 🗺️ **Map Settings:** Simple toggles for UI settings (zoom, compass, etc.) and map properties.
- 🔄 **Lifecycle Aware:** Automatic handling of map lifecycle events within Composables.
- 🏗️ **Flavor Support:** Designed to work side-by-side with GMS-based projects using product flavors.
- 🛡️ **Type Safe:** Fully written in Kotlin with strict null-safety.

## 📋 Requirements

- **Minimum SDK:** 21+
- **Kotlin:** 2.0.0+ (Recommended `2.2.21`)
- **Compose Compiler:** Compatible with your Kotlin version
- **Huawei HMS Core:** Installed on the target device
- **Huawei Developer Account:** Registered and configured in [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html)

## 📦 Installation

Add the following to your `build.gradle.kts` files:

### 1. Repository Configuration
In your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://developer.huawei.com/repo/") }
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Dependency Setup
In your module-level `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.malikbilal1997:huawei-maps-compose:Tag")
}

apply(plugin = "com.huawei.agconnect")
```

## 🛠️ HMS Configuration

To use Huawei Maps, you must configure your project in AppGallery Connect:

1.  **Create Project:** Register your app in the [Huawei AppGallery Connect console](https://developer.huawei.com/consumer/en/service/josp/agc/index.html).
2.  **Enable Map Kit:** Navigate to **Project Settings > Manage APIs** and enable **Map Kit**.
3.  **SHA-256 Certificate:** Add your app's SHA-256 fingerprint in Project Settings.
4.  **Download Config:** Download the `agconnect-services.json` file.
5.  **Place File:** Move `agconnect-services.json` to your app module's root directory (`app/`).

## 🎭 Flavor-Based GMS/HMS Architecture

For production apps, it is best practice to support both GMS and HMS using product flavors. This keeps your APK size small and ensures compatibility across all devices.

### Gradle Configuration
```kotlin
android {
    flavorDimensions.add("provider")
    productFlavors {
        create("gms") {
            dimension = "provider"
        }
        create("hms") {
            dimension = "provider"
        }
    }
}
```

### Source Set Structure
Organize your code to abstract the map implementation:
- `src/main`: Common logic and UI.
- `src/gms`: GMS-specific implementation (using `google-maps-compose`).
- `src/hms`: HMS-specific implementation (using `huawei-maps-compose`).

### Common Abstraction Layer
Create a common interface for your Map UI:

```kotlin
// src/main/java/com/example/app/MyMap.kt
@Composable
expect fun MyMap(
    modifier: Modifier,
    cameraPosition: LatLng,
    onMapClick: (LatLng) -> Unit
)
```

Implement it in the respective flavor folders using the appropriate SDK.

## 💡 Usage Examples

### Basic Map Implementation
```kotlin
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }

    HuaweiMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = rememberMarkerState(position = singapore),
            title = "Singapore",
            snippet = "Marker in Singapore"
        )
    }
}
```

### Drawing a Polyline
```kotlin
HuaweiMap(modifier = Modifier.fillMaxSize()) {
    Polyline(
        points = listOf(
            LatLng(1.35, 103.87),
            LatLng(1.40, 103.90)
        ),
        color = Color.Blue,
        width = 5f
    )
}
```

## 🏗️ Recommended Architecture

To maintain a scalable map integration, we recommend:

1.  **Dependency Injection:** Use Hilt or Koin to inject map-related configurations based on the detected environment (GMS vs HMS).
2.  **Repository Pattern:** Abstract location and map data fetching into repositories.
3.  **Clean Separation:** Keep your Domain models (like a custom `MapLocation` class) separate from SDK-specific models (`com.huawei.hms.maps.model.LatLng`). Use mappers to convert between them.

## 🔍 Troubleshooting

| Issue | Possible Solution |
| :--- | :--- |
| **Map is blank** | Ensure `agconnect-services.json` is in the correct folder and Map Kit is enabled in AGC. |
| **API Key Error** | Verify that your SHA-256 certificate in AGC matches your signing key. |
| **HMS Core Missing** | Huawei Maps requires HMS Core installed on the device. It will not work on standard GMS-only devices. |
| **Markers not showing** | Check if the `MarkerState` is being updated correctly within the Composable lifecycle. |

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

Distributed under the Apache 2.0 License. See `LICENSE` for more information.
