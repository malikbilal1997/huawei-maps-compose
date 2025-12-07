package com.google.maps.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.huawei.hms.maps.model.ButtCap
import com.huawei.hms.maps.model.Cap
import com.huawei.hms.maps.model.JointType
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.PatternItem
import com.huawei.hms.maps.model.Polyline

internal class PolylineNode(val polyline: Polyline, var onPolylineClick: (Polyline) -> Unit) :
  MapNode {
  override fun onRemoved() {
    polyline.remove()
  }
}

/**
 * A composable for a polyline on the map.
 *
 * @param points the points comprising the polyline
 * @param clickable boolean indicating if the polyline is clickable or not
 * @param color the color of the polyline
 * @param endCap a cap at the end vertex of the polyline
 * @param geodesic specifies whether to draw the polyline as a geodesic
 * @param jointType the joint type for all vertices of the polyline except the start and end
 * vertices
 * @param pattern the pattern for the polyline
 * @param startCap the cap at the start vertex of the polyline
 * @param visible the visibility of the polyline
 * @param width the width of the polyline in screen pixels
 * @param zIndex the z-index of the polyline
 * @param onClick a lambda invoked when the polyline is clicked
 */
@Composable
@HuaweiMapComposable
public fun Polyline(
  points: List<LatLng>,
  clickable: Boolean = false,
  color: Color = Color.Black,
  endCap: Cap = ButtCap(),
  geodesic: Boolean = false,
  jointType: Int = JointType.DEFAULT,
  pattern: List<PatternItem>? = null,
  startCap: Cap = ButtCap(),
  tag: Any? = null,
  visible: Boolean = true,
  width: Float = 10f,
  zIndex: Float = 0f,
  onClick: (Polyline) -> Unit = {}
) {
  PolylineImpl(
    points = points,
    clickable = clickable,
    color = color,
    endCap = endCap,
    geodesic = geodesic,
    jointType = jointType,
    pattern = pattern,
    startCap = startCap,
    tag = tag,
    visible = visible,
    width = width,
    zIndex = zIndex,
    onClick = onClick,
  )
}

/**
 * Internal implementation for an advanced polyline on a Huawei map.
 *
 * @param points the points comprising the polyline
 * @param clickable boolean indicating if the polyline is clickable or not
 * @param color the color of the polyline
 * @param endCap a cap at the end vertex of the polyline
 * @param geodesic specifies whether to draw the polyline as a geodesic
 * @param jointType the joint type for all vertices of the polyline except the start and end
 * vertices
 * @param pattern the pattern for the polyline
 * @param startCap the cap at the start vertex of the polyline
 * @param visible the visibility of the polyline
 * @param width the width of the polyline in screen pixels
 * @param zIndex the z-index of the polyline
 * @param onClick a lambda invoked when the polyline is clicked
 */
@Composable
@HuaweiMapComposable
private fun PolylineImpl(
  points: List<LatLng>,
  clickable: Boolean = false,
  color: Color = Color.Black,
  endCap: Cap = ButtCap(),
  geodesic: Boolean = false,
  jointType: Int = JointType.DEFAULT,
  pattern: List<PatternItem>? = null,
  startCap: Cap = ButtCap(),
  tag: Any? = null,
  visible: Boolean = true,
  width: Float = 10f,
  zIndex: Float = 0f,
  onClick: (Polyline) -> Unit = {},
) {
  val mapApplier = currentComposer.applier as MapApplier?
  ComposeNode<PolylineNode, MapApplier>(
    factory = {
      val polyline =
        mapApplier?.map?.addPolyline {
          addAll(points)
          clickable(clickable)
          color(color.toArgb())
          endCap(endCap)
          geodesic(geodesic)
          jointType(jointType)
          pattern(pattern)
          startCap(startCap)
          visible(visible)
          width(width)
          zIndex(zIndex)
        }
          ?: error("Error adding Polyline")
      polyline.tag = tag
      PolylineNode(polyline, onClick)
    },
    update = {
      update(onClick) { this.onPolylineClick = it }

      update(points) { this.polyline.points = it }
      update(clickable) { this.polyline.isClickable = it }
      update(color) { this.polyline.color = it.toArgb() }
      update(endCap) { this.polyline.endCap = it }
      update(geodesic) { this.polyline.isGeodesic = it }
      update(jointType) { this.polyline.jointType = it }
      update(pattern) { this.polyline.pattern = it }
      update(startCap) { this.polyline.startCap = it }
      update(tag) { this.polyline.tag = it }
      update(visible) { this.polyline.isVisible = it }
      update(width) { this.polyline.width = it }
      update(zIndex) { this.polyline.zIndex = it }
    }
  )
}
