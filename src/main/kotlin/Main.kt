import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.File


@Composable
@Preview
fun App() {
    var csvData by remember { mutableStateOf<List<CSVRecord>>(emptyList()) }
    var headers by remember { mutableStateOf<List<String>>(emptyList()) }
    val hiddenColumns = remember { mutableStateListOf<String>() }
    val searchFilters = remember { mutableStateMapOf<String, String>() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = {
            val file = File("C:\\Users\\Peter.Nganga1\\Downloads\\event_log.csv")
            val parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(file.bufferedReader())
            headers = parser.headerNames
            csvData = parser.records
        }) {
            Text("Load CSV File")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (headers.isNotEmpty()) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
            ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(headers.size),
                modifier = Modifier
                    .fillMaxWidth() // Ensure it fills the available width
                    .padding(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                // Header Row
                headers.forEach { header ->
                    if (header !in hiddenColumns) {
                        item {
                            Column(modifier = Modifier.padding(8.dp).border(1.dp, MaterialTheme.colors.primary).width(200.dp)) {
                                Text(
                                    text = header,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(4.dp)
                                )
                                TextField(
                                    value = searchFilters[header] ?: "",
                                    onValueChange = { searchFilters[header] = it },
                                    placeholder = { Text("Search") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Button(onClick = { hiddenColumns.add(header) }) {
                                    Text("Hide")
                                }
                            }
                        }
                    }
                }

                // Data Rows
                csvData.filter { record ->
                    searchFilters.all { (column, filter) ->
                        column in headers && record[column].contains(filter, ignoreCase = true)
                    }
                }.forEach { record ->
                    headers.forEach { header ->
                        if (header !in hiddenColumns) {
                            item {
                                Text(
                                    text = record[header],
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(8.dp).border(1.dp, MaterialTheme.colors.onBackground).width(200.dp)
                                )
                            }
                        }
                    }
                }
            }}
        }
    }
}

//fun main() = androidx.compose.desktop.application.Application {
//    App()
//}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
