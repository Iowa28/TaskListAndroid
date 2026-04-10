package ru.aminovniaz.firstapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.aminovniaz.firstapp.data.Task
import ru.aminovniaz.firstapp.data.TaskRepository
import ru.aminovniaz.firstapp.ui.theme.FirstAppTheme

class MainActivity : ComponentActivity() {

    private val repository by lazy {
        TaskRepository(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstAppTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            MainViewModel(repository)
                        }
                    }
                )

                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val tasks by viewModel.tasks.collectAsState()
    var text by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 56.dp, start = 16.dp, end = 16.dp)
            .clickable( // Делаем всю область кликабельной
                interactionSource = remember { MutableInteractionSource() },
                indication = null // Убираем визуальный эффект нажатия (ripple)
            ) {
                focusManager.clearFocus() // Убираем фокус и скрываем клавиатуру
            }
    ) {
        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                text,
                { text = it },
                label = { Text("Новая задача") },
                modifier = Modifier
                    .weight(1f)
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Back) {
                            if (keyEvent.type == KeyEventType.KeyUp) {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                            true
                        } else {
                            false
                        }
                    }
            )
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        viewModel.addTask(text)
                        text = ""
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(top = 11.dp)
            ) {
                Text("Добавить")
            }
        }

        NamesList(tasks) { taskToDelete ->
            viewModel.deleteTaskById(taskToDelete.id)
        }
    }
}

@Composable
private fun NamesList(
    tasks: List<Task>,
    onDelete: (Task) -> Unit
) {
    LazyColumn {
        items(tasks) { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.name,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { onDelete(task) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
            HorizontalDivider()
        }
    }
}
