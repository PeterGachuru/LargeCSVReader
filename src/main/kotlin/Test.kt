import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.File

@Composable
@Preview
fun App2() {
    var csvData by remember { mutableStateOf<List<CSVRecord>>(emptyList()) }
    var headers by remember { mutableStateOf<List<String>>(emptyList()) }
    val hiddenColumns = remember { mutableStateListOf<String>() }
    val searchFilters = remember { mutableStateMapOf<String, String>() }
    val fixedColumnWidth = 150.dp // Fixed width for all columns

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
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Row with Search Boxes
                Row(modifier = Modifier.fillMaxWidth()) {
                    headers.forEach { header ->
                        if (header !in hiddenColumns) {
                            Column(
                                modifier = Modifier
                                    .width(fixedColumnWidth)
                                    .padding(4.dp)
                                    .border(1.dp, MaterialTheme.colors.primary)
                            ) {
                                Text(
                                    text = header,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                                )
                                TextField(
                                    value = searchFilters[header] ?: "",
                                    onValueChange = { searchFilters[header] = it },
                                    placeholder = { Text("Search") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Button(
                                    onClick = { hiddenColumns.add(header) },
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Text("Hide")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // LazyColumn for Data Rows
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(csvData.filter { record ->
                        searchFilters.all { (column, filter) ->
                            column in headers && record[column].contains(filter, ignoreCase = true)
                        }
                    }) { record ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            headers.forEach { header ->
                                if (header !in hiddenColumns) {
                                    Text(
                                        text = record[header],
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .width(fixedColumnWidth)
                                            .padding(8.dp)
                                            .border(1.dp, MaterialTheme.colors.onBackground)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App2()
    }
}
