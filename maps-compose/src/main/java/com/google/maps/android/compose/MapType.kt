package com.google.maps.android.compose

import androidx.compose.runtime.Immutable
import com.huawei.hms.maps.HuaweiMap

/** Enumerates the different types of map tiles. */
@Immutable
public enum class MapType(public val value: Int) {
  NONE(HuaweiMap.MAP_TYPE_NONE),
  NORMAL(HuaweiMap.MAP_TYPE_NORMAL),
  SATELLITE(HuaweiMap.MAP_TYPE_SATELLITE),
  TERRAIN(HuaweiMap.MAP_TYPE_TERRAIN),
  HYBRID(HuaweiMap.MAP_TYPE_HYBRID)
}
