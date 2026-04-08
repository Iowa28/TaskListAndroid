package ru.aminovniaz.firstapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.aminovniaz.firstapp.ui.theme.FirstAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstAppTheme {
                var name by remember {
                    mutableStateOf("")
                }
                val names = remember { mutableStateListOf<String>() }
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(top = 56.dp, start = 16.dp, end = 16.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            name,
                            { text ->
                                name = text
                            },
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(Modifier.width(16.dp))

                        Button(
                            onClick = {
                                if (name.isNotBlank()) {
                                    names += name
                                    name = ""
                                }
                            },
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text("Добавить")
                        }
                    }

                    NamesList(
                        names
                    ) { nameToDelete ->
                        names.remove(nameToDelete)
                    }
                }
            }
        }
    }
}

@Composable
private fun NamesList(
    names: List<String>,
    onDelete: (String) -> Unit
) {
    LazyColumn {
        items(names) { currentName ->
            // 2. Создаем строку с текстом и кнопкой
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentName,
                    modifier = Modifier.weight(1f) // Текст занимает все свободное место
                )
                IconButton(
                    onClick = { onDelete(currentName) } // 3. Вызываем удаление
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            // Разделитель можно оставить после строки
            HorizontalDivider()
        }
    }
}
