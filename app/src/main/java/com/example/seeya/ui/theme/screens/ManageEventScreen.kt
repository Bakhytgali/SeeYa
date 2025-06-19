package com.example.seeya.ui.theme.screens

import android.app.Application
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.data.model.EventApplication
import com.example.seeya.data.model.Participant
import com.example.seeya.data.model.QrCodeData
import com.example.seeya.data.model.QrDataModel
import com.example.seeya.data.model.SearchUser
import com.example.seeya.ui.theme.components.ManageEventInfo
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
    val manageOptions = listOf("Info", "Applications", "Attendance")

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
                        Toast.makeText(context, "User Was Added to the Attendance", Toast.LENGTH_SHORT).show()
                    },
                    onError = {
                        Toast.makeText(context, "Invalid QR format", Toast.LENGTH_SHORT).show()
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

        if (manageOption == "Info" && !eventViewModel.event?.isOpen!!) {
            eventViewModel.loadEventApplications(eventId)
        }
    }

    Scaffold(
        topBar = {
            SimpleTopBar("") {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight()
                ,
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
                        0 -> {
                            ManageEventInfo(
                                eventViewModel = eventViewModel,
                                navController = navController
                            )
                        }
                        1 -> {
                            ManageApplications(
                                applicants = eventViewModel.applications,
                                loading = eventViewModel.loadingApplications,
                                onAccept = {
                                    eventViewModel.acceptApplication(it)
                                },
                                onReject = {
                                    eventViewModel.rejectApplication(it)
                                }
                            )
                        }
                        2 -> {
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
fun ManageApplications(
    onReject: (String) -> Unit,
    onAccept: (String) -> Unit,
    modifier: Modifier = Modifier,
    applicants: StateFlow<List<EventApplication>>,
    loading: StateFlow<Boolean>,
) {
    val loadingState = loading.collectAsState()
    val applicantsList = applicants.collectAsState()

    Column(
        modifier = modifier.fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "Applications",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 20.dp)
        )

        if (loadingState.value) {
            CircularProgressIndicator()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
            ) {
                itemsIndexed(applicantsList.value) { index, applicant ->
                    AttendantCard(
                        index = index + 1,
                        user = applicant,
                        onAccept = {
                            onAccept(it)
                        },
                        onReject = {
                            onReject(it)
                        }
                    )
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

            if(loading.value) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    itemsIndexed(attendanceList.value.reversed()) { index, attendant ->
                        ParticipantCard(participant = attendant, index = index + 1)
                    }
                }
            }
        }
    }
}

@Composable
fun AttendantCard(
    user: EventApplication,
    index: Int,
    onReject: (String) -> Unit,
    onAccept: (String) -> Unit,
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
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp).weight(1f)
                ) {
                    Text(
                        text = "@${user.applicant?.username}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "${user.applicant?.name} ${user.applicant?.surname}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    )
                }

                IconButton(
                    onClick = {
                        user.id?.let { onAccept(it) }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "",
                        tint = Color.Green,
                    )
                }

                Spacer(Modifier.width(15.dp))

                IconButton(
                    onClick = {
                        user.id?.let { onReject(it) }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "",
                        tint = Color.Red,
                    )
                }
            }
        }
    }
}

@Composable
fun ParticipantCard(
    participant: Participant,
    index: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "$index. ${participant.username ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "Attended",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


fun scan(scanLauncher: ActivityResultLauncher<ScanOptions>) {
    scanLauncher.launch(ScanOptions())
}

