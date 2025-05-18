package com.example.seeya.ui.theme.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.seeya.data.model.ClubCategories
import com.example.seeya.ui.theme.components.ClubCustomBottomSheet
import com.example.seeya.data.model.ClubTypes
import com.example.seeya.data.model.EventTags
import com.example.seeya.ui.theme.components.CreateClubSoothe
import com.example.seeya.ui.theme.components.CustomTextField
import com.example.seeya.ui.theme.components.ListOfTags
import com.example.seeya.ui.theme.components.MainScaffold
import com.example.seeya.ui.theme.components.SeeYaLogo
import com.example.seeya.ui.theme.components.SimpleTopBar
import com.example.seeya.viewmodel.BottomBarViewModel
import com.example.seeya.viewmodel.auth.AuthViewModel
import com.example.seeya.viewmodel.clubs.ClubsViewModel
import java.time.LocalDateTime
import java.util.Calendar

@Composable
fun CreateClubScreen(
    bottomBarViewModel: BottomBarViewModel,
    clubsViewModel: ClubsViewModel,
    navController: NavController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    var currentStep by remember {
        mutableIntStateOf(0)
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                clubsViewModel.handleImageUri(it)
            }
        }

    BackHandler {
        if (currentStep > 0) {
            currentStep--
        } else {
            navController.popBackStack()
            clubsViewModel.clearEntries()
        }
    }

    val navOption = {
        if (currentStep > 0) {
            currentStep--
        } else {
            clubsViewModel.clearEntries()
            navController.popBackStack()
        }
    }

    val content: @Composable () -> Unit = when (currentStep) {
        0 -> {
            {
                ClubCategoryChoose(
                    onClubCategorySelected = {
                        clubsViewModel.onNewClubCategoryChange(it.title)
                        currentStep++
                    }
                )
            }
        }

        1 -> {
            {
                NameClub(
                    text = clubsViewModel.createClubTitle,
                    onValueChange = {
                        clubsViewModel.onNewClubTitleChange(it)
                    },
                    onRadioButtonClick = {
                        clubsViewModel.onNewClubIsOpenChange(it)
                    },
                    clubViewModel = clubsViewModel
                )
            }
        }

        2 -> {
            {
                ChooseClubTags(
                    clubsViewModel = clubsViewModel
                )
            }
        }

        3 -> {
            {
                CreateClubChoosePicture(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    clubsViewModel = clubsViewModel
                )
            }
        }

        4 -> {
            {
                CreateClubDetails(
                    description = clubsViewModel.createNewClubDescription,
                    onDescriptionChange = {
                        clubsViewModel.onNewClubDescriptionChange(it)
                    }
                )
            }
        }

        else -> {
            { }
        }
    }
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "New Event",
                onClick = {
                    navOption()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CreateClubSoothe(
                title = when (currentStep) {
                    0 -> "What is your goal?"
                    1 -> "Name your event"
                    2 -> "Select subject"
                    3 -> "Choose a picture"
                    4 -> "Finally, fill the details"
                    else -> ""
                },
                subtitle = when (currentStep) {
                    0 -> "So, what kind of club are you planning to start?"
                    1 -> "Name your club so that it captures what it’s about!"
                    2 -> "Choose what your clubs is about — games, business, music, or whatever you’re into!"
                    3 -> "Show off the vibe and let people see what they’re signing up for!"
                    4 -> "Tell a bit more about what it’s about and anything people should know!"
                    else -> ""
                },
                content = content,
                currentStep = currentStep,
                onButtonClick = {
                    if (currentStep < 4) {
                        currentStep++
                        Log.d("MyLog", "$currentStep")
                    } else if (currentStep == 4) {
                        Log.d("My Log", "Creating an Event...")
                        clubsViewModel.createNewClub(
                            onSuccess = {
                                navController.popBackStack()
                                clubsViewModel.clearEntries()
                            },
                            onError = {
                                Log.d("My Log", "Event Not Created")
                                currentStep = 0
                            }
                        )
                    }
                },
                clubNameIsFilled = clubsViewModel.createClubTitle.isNotBlank(),
                clubAreTagsChosen = clubsViewModel.createNewClubTags.isNotBlank(),
                clubPictureIsChosen = clubsViewModel.createNewClubPicture.isNotBlank(),
                clubDescriptionFilled = clubsViewModel.createNewClubDescription.isNotBlank()
            )
        }
    }
}


@Composable
fun ClubCategoryChoose(
    onClubCategorySelected: (ClubCategories) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ClubCategories.entries.forEach { eventType ->
            ClubCategoryCard(eventType, onClick = {
                onClubCategorySelected(eventType)
            })
        }
    }
}

@Composable
fun ClubCategoryCard(
    clubCategory: ClubCategories,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .height(90.dp)
        ) {
            Image(
                painter = painterResource(clubCategory.icon),
                contentDescription = "${clubCategory.title} icon"
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = clubCategory.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(clubCategory.description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                )
            }
        }
    }
}


@Composable
fun NameClub(
    text: String,
    onValueChange: (String) -> Unit,
    onRadioButtonClick: (ClubTypes) -> Unit,
    clubViewModel: ClubsViewModel,
    modifier: Modifier = Modifier
) {
    var bottomSheetVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 3.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "0/40",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            CustomTextField(
                text = text,
                onValueChange = onValueChange,
                placeholder = "Event Title",
                modifier = modifier.fillMaxWidth()
            )
        }

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 3.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Event Type",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            CustomTextField(
                text = clubViewModel.createNewClubType,
                onValueChange = {},
                placeholder = "Club Type",
                isActive = false,
                onClick = {
                    bottomSheetVisible = true
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                modifier = modifier.fillMaxWidth()
            )
        }

        if (bottomSheetVisible) {
            ClubCustomBottomSheet(
                onDismiss = {
                    bottomSheetVisible = false
                },
                onRadioButtonClick = {
                    onRadioButtonClick(it)
                },
                clubsViewModel = clubViewModel
            )
        }
    }
}

@Composable
fun ChooseClubTags(
    clubsViewModel: ClubsViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CustomTextField(
            text = "Search",
            onValueChange = {

            },
            placeholder = "Search",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Tags Icon",
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scroll to see more options",
                color = MaterialTheme.colorScheme.secondaryContainer,
                style = MaterialTheme.typography.bodySmall
            )
            ListOfClubTags(
                clubsViewModel = clubsViewModel
            )
        }
    }
}

@Composable
fun ListOfClubTags(
    clubsViewModel: ClubsViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(EventTags.entries) { eventTag ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        clubsViewModel.onNewClubTagsChange(eventTag.title)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = eventTag.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
                if (clubsViewModel.createNewClubTags == eventTag.title) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Tag is Chosen",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CreateClubChoosePicture(
    onClick: () -> Unit,
    clubsViewModel: ClubsViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable {
                    onClick()
                }
            ,
            contentAlignment = Alignment.Center
        ) {
            if (clubsViewModel.imageBitmap != null) {
                Image(
                    bitmap = clubsViewModel.imageBitmap!!.asImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                SeeYaLogo(
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Tap to choose",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}

@Composable
fun CreateClubDetails(
    description: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Column {
            Text(
                text = "0/80",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                textAlign = TextAlign.End
            )
            CustomTextField(
                text = description,
                placeholder = "Event Description",
                onValueChange = onDescriptionChange,
                numberOfLines = 4,
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
            )
        }
    }
}