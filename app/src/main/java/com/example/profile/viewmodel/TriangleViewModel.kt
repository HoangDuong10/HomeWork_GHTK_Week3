package com.example.profile.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.profile.model.Coordinates
import kotlin.math.abs

class TriangleViewModel : ViewModel() {
    val listCoordinates = mutableStateListOf<Coordinates>()
    var message = mutableStateOf("")

    fun addCoordinate(coordinate: Coordinates) {
        listCoordinates.add(coordinate)
        if (listCoordinates.size > 3) {
            val (px, py) = coordinate
            val (x1, y1) = listCoordinates[0]
            val (x2, y2) = listCoordinates[1]
            val (x3, y3) = listCoordinates[2]
            message.value = if (isPointInTriangle(px, py, x1, y1, x2, y2, x3, y3)) {
                "Điểm vừa chọn nằm trong tam giác"
            } else {
                "Điểm vừa chọn nằm ngoài trong tam giác"
            }
        }
    }

    fun clearCoordinates() {
        listCoordinates.clear()
        message.value = ""
    }

    private fun isPointInTriangle(px: Float, py: Float, x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Boolean {
        val areaOrig = triangleArea(x1, y1, x2, y2, x3, y3)
        val area1 = triangleArea(px, py, x2, y2, x3, y3)
        val area2 = triangleArea(x1, y1, px, py, x3, y3)
        val area3 = triangleArea(x1, y1, x2, y2, px, py)
        return abs(areaOrig - (area1 + area2 + area3)) < 0.1
    }

    private fun triangleArea(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Float {
        return abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0f)
    }
}