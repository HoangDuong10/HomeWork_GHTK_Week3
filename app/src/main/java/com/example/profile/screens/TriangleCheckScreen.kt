package com.example.profile.screens

import com.example.profile.viewmodel.TriangleViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profile.R
import com.example.profile.model.Coordinates

@Composable
fun TriangleCheckScreen() {
    CustomTriangle(color = colorResource(id = R.color.green))
}

@Composable
fun CustomTriangle(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    viewModel: TriangleViewModel = viewModel()
) {
    val listCoordinates = viewModel.listCoordinates
    val message by remember { viewModel.message }

    Box(modifier = modifier.pointerInput(Unit) {
        detectTapGestures { offset ->
            viewModel.addCoordinate(Coordinates(offset.x, offset.y))
        }
    }) {
        DrawTriangleCanvas(listCoordinates, color)
        DisplayMessage(message, listCoordinates) {
            viewModel.clearCoordinates()
        }
    }
}

@Composable
fun DrawTriangleCanvas(
    listCoordinates: List<Coordinates>,
    color: Color
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        for (coordinate in listCoordinates) {
            drawCircle(
                color = color,
                radius = 10f,
                center = Offset(coordinate.x, coordinate.y)
            )
        }

        if (listCoordinates.size > 2) {
            val path = Path().apply {
                moveTo(listCoordinates[0].x, listCoordinates[0].y)
                lineTo(listCoordinates[1].x, listCoordinates[1].y)
                lineTo(listCoordinates[2].x, listCoordinates[2].y)
                close()
            }
            drawPath(path, color, style = Stroke(width = 10f))
        }
    }
}

@Composable
fun DisplayMessage(message: String, listCoordinates: List<Coordinates>, onClear: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        if(listCoordinates.size<3){
            Text(text = "Nhấp vào màn hình 3 lần để tạo 1 tam giác",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
        }

        if (listCoordinates.isNotEmpty()) {
            Text(
                text = "Nhấn vào đây để vẽ lại tam giác",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp).pointerInput(Unit) {
                    detectTapGestures { onClear() }
                }
            )

        }

        Text(text = message, color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp),
            fontSize = 18.sp)
    }
}

@Preview
@Composable
fun PreviewCustomTriangle() {
    CustomTriangle()
}