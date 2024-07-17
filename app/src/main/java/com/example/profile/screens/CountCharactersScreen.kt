package com.example.profile.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profile.viewmodel.CountCharactersViewModel

@Composable
fun CountCharactersScreen (){
    val countCharactersViewModel: CountCharactersViewModel = viewModel()
    val characterCount by countCharactersViewModel.characterCount
    Column(modifier = Modifier.fillMaxSize()) {
        var input by remember { mutableStateOf("") }
        TextField(value = input, onValueChange = {
            input = it
            countCharactersViewModel.countCharacters(it)
        },
            placeholder = {
                Text(text = "Enter text")
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp).border(2.dp, Color.Gray,
                RoundedCornerShape(16.dp)
            ),

            colors = TextFieldDefaults.colors(
                cursorColor = Color.Black,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent

            ),
        )
        LazyColumn(modifier = Modifier.padding( 16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(characterCount.entries.toList()) { entry ->
                Column {
                    Text(
                        "${entry.key}  :   ${entry.value}",
                        fontSize = 22.sp)
                    Divider(color = Color.Gray, thickness = 0.5.dp, modifier = Modifier.padding(top = 4.dp))
                }

            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TestPreview(){
    CountCharactersScreen()
}