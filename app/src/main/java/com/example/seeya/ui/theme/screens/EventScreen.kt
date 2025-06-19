package com.example.seeya.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.seeya.data.model.PostModel
import com.example.seeya.ui.theme.components.EventClubScreenButton
import com.example.seeya.ui.theme.components.EventInfoBottomModal
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.components.ParticipateModal
import com.example.seeya.ui.theme.components.RatingModal
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.ui.theme.components.SimpleTopBar
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.event.EventViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun EventScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    eventId: String,
    eventViewModel: EventViewModel,
    bottomBarViewModel: BottomBarViewModel
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                ""
            ) {
                navController.popBackStack()
            }
        },
        floatingActionButton = {
            val event = eventViewModel.event
            val currentUserId = authViewModel.currentUser.value?.id
            if (event != null) {
                val date = event.startDate
                val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                val eventDate = LocalDateTime.parse(date, formatter)
                val now = LocalDateTime.now(ZoneId.systemDefault())

                if(currentUserId == event.creator.id || eventDate.isBefore(now)) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate("addPost")
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add Post Button",
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            eventViewModel.getEvent(
                eventId,
                onSuccess = {
                    eventViewModel.checkIfParticipating(authViewModel.currentUser.value!!.id!!)

                    eventViewModel.fetchPosts(eventId) { result, message ->
                        if (!result) {
                            Log.d("MyLog", message ?: "Ошибка загрузки постов")
                        }
                    }
                },
                onError = {
                    Toast.makeText(context, "Ошибка загрузки события", Toast.LENGTH_SHORT).show()
                }
            )
        }


        val doesParticipate = eventViewModel.isParticipating.collectAsState()
        val isLoading = eventViewModel.eventIsLoading.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading.value -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Loading event...")
                    }
                }

                eventViewModel.event == null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Failed to load event")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            eventViewModel.getEvent(
                                eventId,
                                onSuccess = {
                                    eventViewModel.checkIfParticipating(authViewModel.currentUser.value!!.id!!)
                                },
                                onError = {}
                            )
                        }) {
                            Text("Retry")
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        val event = eventViewModel.event!!

                        Row(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(15.dp)
                                )
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            if (!event.eventPicture.isNullOrEmpty()) {
                                AsyncImage(
                                    model = event.eventPicture,
                                    contentDescription = "Event Image",
                                    modifier = Modifier
                                        .size(150.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                SeeYaLogo(
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            ) {
                                Text(
                                    text = event.name,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(15.dp))

                                Text(
                                    text = event.eventTags ?: "Null",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                )

                                Spacer(modifier = Modifier.height(15.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.clickable {
                                            navController.navigate("eventUsers")
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Person,
                                            contentDescription = "Participants",
                                            tint = MaterialTheme.colorScheme.secondaryContainer,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = event.participants?.size?.toString() ?: "0",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    }

                                    Spacer(Modifier.width(25.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Lock,
                                            contentDescription = "Event Type",
                                            tint = MaterialTheme.colorScheme.secondaryContainer,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = if (event.isOpen) "Open" else "Closed",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            if (event.creator.id == authViewModel.currentUser.value!!.id) {
                                EventClubScreenButton(
                                    title = "Manage",
                                    onClick = {
                                        navController.navigate("manageEvent/${event.eventId}")
                                    },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    textColor = MaterialTheme.colorScheme.onBackground,
                                    borderColor = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier.weight(1f),
                                    textPadding = 5
                                )
                            } else if (doesParticipate.value) {
                                EventClubScreenButton(
                                    title = "Participating",
                                    onClick = {
                                        eventViewModel.onAlreadyParticipateModalOpen(true)
                                    },
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    textColor = MaterialTheme.colorScheme.primary,
                                    borderColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f),
                                    textPadding = 5
                                )
                            } else {
                                EventClubScreenButton(
                                    title = "Participate",
                                    onClick = {
                                        eventViewModel.joinEvent(
                                            eventId = event.eventId,
                                            onSuccess = {
                                                eventViewModel.onSetIsParticipating(true)
                                                eventViewModel.onIsParticipateModalOpen(true)
                                            },
                                            onError = {
                                                eventViewModel.onSetIsParticipating(false)
                                                eventViewModel.onIsParticipateModalOpen(true)
                                            }
                                        )
                                    },
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    textColor = MaterialTheme.colorScheme.background,
                                    borderColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f),
                                    textPadding = 5
                                )
                            }

                            EventClubScreenButton(
                                title = "Info",
                                onClick = {
                                    eventViewModel.onEventInfoModalOpenChange(true)
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                textColor = MaterialTheme.colorScheme.secondaryContainer,
                                borderColor = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.weight(1f),
                                textPadding = 5
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        Text(
                            text = "Event Media",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )

                        Spacer(Modifier.height(10.dp))

                        var showRatingModal by remember {
                            mutableStateOf(false)
                        }

                        if (showRatingModal) {
                            RatingModal(
                                onSubmit = { rating ->
                                    eventViewModel.rateEvent(event.eventId, rating) { success ->
                                        if (success) {
                                            Toast.makeText(
                                                context,
                                                "Thanks for your rating!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Failed to rate event",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    showRatingModal = false
                                },
                                onDismiss = {
                                    showRatingModal = false
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(15.dp)
                        ) {
                            EventClubScreenButton(
                                title = "Rating ${event.eventRating}",
                                onClick = {
                                    if (doesParticipate.value) {
                                        showRatingModal = true
                                    }
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                textColor = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.weight(1f),
                                textPadding = 10,
                                borderColor = MaterialTheme.colorScheme.primaryContainer,
                                fontWeight = FontWeight.Normal
                            )

                            EventClubScreenButton(
                                title = "Media",
                                onClick = {
                                    navController.navigate("eventMedia/${event.eventId}")
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                textColor = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier.weight(1f),
                                textPadding = 10,
                                borderColor = MaterialTheme.colorScheme.primaryContainer,
                                fontWeight = FontWeight.Normal
                            )
                        }

                        Spacer(Modifier.height(20.dp))


                        Text(
                            text = "Event Posts",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )

                        Spacer(Modifier.height(20.dp))

                        val posts = eventViewModel.posts

                        if (posts.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(700.dp),
                                verticalArrangement = Arrangement.spacedBy(15.dp),
                            ) {
                                items(posts) { post ->
                                    EventPostCard(
                                        post = post,
                                        userId = authViewModel.currentUser.value?.id ?: "None",
                                        eventViewModel = eventViewModel,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "No posts have been posted yet!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            )
                        }
                    }
                }
            }
            if (eventViewModel.isParticipateModalOpen) {
                eventViewModel.event?.let { event ->
                    ParticipateModal(
                        event = event,
                        onOk = {
                            eventViewModel.onIsParticipateModalOpen(false)
                        }
                    )
                }
            }

            if (eventViewModel.alreadyParticipateModalOpen) {
                EventScreenModal(
                    title = "Already Joined!",
                    text = "You are already participating in the event",
                    onDismiss = {
                        eventViewModel.onAlreadyParticipateModalOpen(false)
                    },
                    onConfirm = {
                        eventViewModel.onAlreadyParticipateModalOpen(false)
                    },
                    confirmTitle = "Undo"
                )
            }

            if (eventViewModel.eventInfoModalOpen) {
                eventViewModel.event?.let { event ->
                    EventInfoBottomModal(
                        event = event,
                        onDismiss = {
                            eventViewModel.onEventInfoModalOpenChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EventPostCard(
    eventViewModel: EventViewModel,
    userId: String,
    post: PostModel,
    modifier: Modifier = Modifier
) {
    val likes = remember { mutableStateListOf<String>().apply { addAll(post.likes) } }
    var likeCount by remember {
        mutableIntStateOf(post.likeCount)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = post.header,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = post.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(0.9f),
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            if (post.media.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(post.media) { media ->
                        AsyncImage(
                            model = media,
                            contentDescription = "Event Post Media",
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .size(250.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            val updatedLikes = if (userId in likes) {
                                eventViewModel.unlikePost(post.id)
                            } else {
                                eventViewModel.likePost(post.id)
                            }

                            updatedLikes?.let {
                                likes.clear()
                                likes.addAll(it)
                                likeCount = it.size
                            } ?: run {
                                Toast.makeText(
                                    context,
                                    "Не удалось обновить лайки",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (userId in likes) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                        contentDescription = "Post Like Button",
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                }

                Text(
                    text = "$likeCount",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@Composable
fun EventScreenModal(
    confirmTitle: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Ok",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = confirmTitle,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp
                )
            }
        },
        text = {
            Text(
                text = text,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 25.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        title = {
            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    )
}
