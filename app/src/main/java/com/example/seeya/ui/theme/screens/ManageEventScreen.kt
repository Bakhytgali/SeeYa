package com.example.seeya.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.data.model.Participant
import com.example.seeya.data.model.QrCodeData
import com.example.seeya.data.model.QrDataModel
import com.example.seeya.data.model.SearchUser
import com.example.seeya.ui.theme.components.SimpleTopBar
import com.example.seeya.viewmodel.event.EventViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json


@Composable
fun ManageEventScreen(
    eventId: String,
    navController: NavController,
    eventViewModel: EventViewModel,
    modifier: Modifier = Modifier
) {
    val manageOptions = listOf("Info", "Attendance")

    var manageOption by remember { mutableStateOf(manageOptions[0]) }

    val pagerState = rememberPagerState(pageCount = { manageOptions.size })

    var qrText by remember { mutableStateOf("") }
    var qrData by remember { mutableStateOf<QrCodeData?>(null) }

    val context = LocalContext.current

    val scanLauncher = rememberLauncherForActivityResult(contract = ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(context, "Scan canceled", Toast.LENGTH_SHORT).show()
            qrText = "Scan canceled"
        } else {
            qrText = result.contents

            Log.d("QR SCANNER", result.contents)
            try {
                val parsedData = Json.decodeFromString<QrCodeData>(result.contents)
                Log.d("QR SCANNER", parsedData.toString())
                qrData = parsedData

                val newQRData = QrDataModel(eventId = qrData!!.eventId, userId = qrData!!.userId)
                eventViewModel.checkAttendance(
                    qrData = newQRData,
                    onSuccess = {

                    },
                    onError = {

                    }
                )
            } catch (e: Exception) {
                Log.e("QR_PARSE", "Error parsing QR data", e)
                qrText = "Invalid QR format: ${e.message}"
                Toast.makeText(context, "Invalid QR format", Toast.LENGTH_SHORT).show()
            }

            Log.d("QR_SCAN", "Raw data: $qrText")
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        manageOption = manageOptions[pagerState.currentPage]
    }

    LaunchedEffect(manageOption) {
        pagerState.animateScrollToPage(manageOptions.indexOf(manageOption))
    }

    Scaffold(
        topBar = {
            SimpleTopBar("") {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    manageOptions.forEachIndexed { index, option ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { manageOption = option },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp)
                            ) {
                                Text(
                                    text = option,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = if (manageOption == option)
                                        MaterialTheme.colorScheme.onBackground
                                    else
                                        MaterialTheme.colorScheme.secondaryContainer,
                                )
                            }
                            HorizontalDivider(
                                color = if (manageOption == option)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.secondaryContainer,
                                thickness = 3.dp,
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                        }
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    when (page) {
                        0 -> {}
                        1 -> {
                            ManageEventAttendance(
                                scanLauncher = scanLauncher,
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                attendees = eventViewModel.attendanceList,
                                loadingAttendance = eventViewModel.loadingAttendance
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ManageEventAttendance(
    scanLauncher: ActivityResultLauncher<ScanOptions>,
    attendees: StateFlow<List<Participant>>,
    loadingAttendance: StateFlow<Boolean>,
    modifier: Modifier = Modifier
) {
    val loading = loadingAttendance.collectAsState()
    val attendanceList = attendees.collectAsState()

    Log.d("AttendanceList", "${attendanceList.value}")

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ) {

            Button(
                onClick = {
                    scan(scanLauncher)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(
                    "Check Attendance",
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(40.dp))

            Text(
                "Attendees:",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            if(loading.value) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    itemsIndexed(attendanceList.value.reversed()) { index, attendant ->
                        AttendantCard(
                            index = index,
                            user = attendant,
                            onClick = {

                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AttendantCard(
    onClick: (String) -> Unit,
    user: Participant,
    index: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "$index",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondaryContainer
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            onClick = { onClick(user.id) }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp)
            ) {
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "${user.name} ${user.surname}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                )
            }
        }
    }
}

fun scan(scanLauncher: ActivityResultLauncher<ScanOptions>) {
    scanLauncher.launch(ScanOptions())
}

