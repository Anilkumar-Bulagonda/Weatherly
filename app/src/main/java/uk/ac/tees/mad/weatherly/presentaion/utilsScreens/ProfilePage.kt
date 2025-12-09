package uk.ac.tees.mad.weatherly.presentaion.utilsScreens

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import uk.ac.tees.mad.careerconnect.presentation.auth.AuthViewModel
import uk.ac.tees.mad.weatherly.R
import uk.ac.tees.mad.weatherly.converter.uriToByteArray
import uk.ac.tees.mad.weatherly.presentaion.viewModels.HomeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,

    ) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        authViewModel.fetchCurrentDonerData()
    }
    var updateState by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        authViewModel.fetchCurrentDonerData()
    }

    var newName by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var isUpdating by rememberSaveable { mutableStateOf(false) }
    val cornerShape = RoundedCornerShape(14.dp)
    var isEditing by rememberSaveable { mutableStateOf(false) }
    val currentUser by authViewModel.currentUserData.collectAsState()


    val freshUrl = "${currentUser.profileImageUrl}?t=${System.currentTimeMillis()}"
    val imageRequest = ImageRequest.Builder(context).data(freshUrl).crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED).memoryCachePolicy(CachePolicy.ENABLED).build()

    val painter = rememberAsyncImagePainter(model = imageRequest)
    val state by painter.state.collectAsState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val defaulImagetUri = Uri.parse(
        "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.drawable.default_profile}"
    )
    val imageUri: Uri = if (selectedImageUri == null) {
        defaulImagetUri
    } else {
        selectedImageUri!!
    }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        })


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(), onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        })

    var expanded by rememberSaveable { mutableStateOf(false) }

    var showDialogUnites by rememberSaveable { mutableStateOf(false) }

    var showDialogLogout by rememberSaveable { mutableStateOf(false) }

    var showDialogClearCache by rememberSaveable { mutableStateOf(false) }

    var showDialogNotification by rememberSaveable { mutableStateOf(false) }


    var isNotificationEnabled = isAppNotificationEnabled(context)


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile", color = MaterialTheme.colorScheme.background, fontSize = 22.sp
                    )

                },
                actions = @Composable {
                    Box {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.Black
                            )
                        }

                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

                            DropdownMenuItem(text = { Text("App Notifications") }, onClick = {

                                showDialogNotification = true
                            })
                            DropdownMenuItem(text = { Text("Clear Cache") }, onClick = {

                                showDialogClearCache = true
                            })
                            DropdownMenuItem(text = { Text("LogOut") }, onClick = {


                                showDialogLogout = true

                            }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.app))
            )
        }) { innerPadding ->

        Box(modifier = Modifier.padding(innerPadding)) {


            if (showDialogLogout) {
                AlertDialog(
                    onDismissRequest = {
                        expanded = false
                        showDialogLogout = false
                    },
                    title = { Text("Logout") },
                    text = { Text("Are you sure you want to log out?") },
                    confirmButton = {
                        TextButton(onClick = {
                            authViewModel.logoutUser()
                            expanded = false
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {

                            expanded = false
                            showDialogLogout = false

                        }) {
                            Text("No")
                        }
                    })
            }



            var isDeleting by rememberSaveable { mutableStateOf(false) }

            if (showDialogClearCache) {
                AlertDialog(
                    onDismissRequest = {
                        if (!isDeleting) {
                            expanded = false
                            showDialogClearCache = false
                        }
                    },
                    title = { Text("Clear Cache") },
                    text = {
                        if (isDeleting) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                Text("Clearing cache, please wait...")
                            }
                        } else {
                            Text("Are you sure you want to clear the app cache? This action cannot be undone.")
                        }
                    },
                    confirmButton = {
                        if (!isDeleting) {
                            TextButton(onClick = {
                                isDeleting = true

                                homeViewModel.clearAppDataSafely { success, message ->
                                    isDeleting = false
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    expanded = false
                                    showDialogClearCache = false
                                }
                            }) {
                                Text("Yes")
                            }
                        }
                    },
                    dismissButton = {
                        if (!isDeleting) {
                            TextButton(onClick = {
                                expanded = false
                                showDialogClearCache = false
                            }) {
                                Text("No")
                            }
                        }
                    }
                )
            }


            if (showDialogNotification) {

                var isNotificationEnabled = isAppNotificationEnabled(context)

                AlertDialog(onDismissRequest = {
                    expanded = false
                    showDialogNotification = false
                }, title = { Text("Notifications") }, text = {
                    Text(
                        if (isNotificationEnabled) "Notifications is currently ON. Do you want to turn them OFF?"
                        else "Notifications is currently OFF. Do you want to turn them ON?"
                    )
                }, confirmButton = {
                    TextButton(onClick = {

                        val intent = Intent().apply {
                            action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                        context.startActivity(intent)
                        showDialogNotification = false
                        expanded = false
                        isNotificationEnabled = isAppNotificationEnabled(context)

                    }) {
                        Text(if (isNotificationEnabled) "Turn Off" else "Turn On")
                    }
                }, dismissButton = {
                    TextButton(onClick = {
                        expanded = false
                        showDialogNotification = false
                    }) {
                        Text("No")
                    }
                })
            }





            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(contentAlignment = Alignment.BottomEnd) {

                    if (selectedImageUri != null) {

                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    } else if (state is AsyncImagePainter.State.Loading) {
                        Box(
                            contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)
                        ) {
                            AsyncImage(
                                model = R.drawable.pf,
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )


                            CircularProgressIndicator(
                                color = Color.Black,

                                strokeWidth = 2.dp, modifier = Modifier.size(30.dp)

                            )
                        }


                    } else if (state is AsyncImagePainter.State.Success) {

                        AsyncImage(
                            model = imageRequest,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        AsyncImage(
                            model = R.drawable.pf,
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    }


                    if (isEditing) {
                        IconButton(
                            onClick = {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                } else {
                                    val intent = Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    )
                                    galleryLauncher.launch(intent)
                                }
                            }, modifier = Modifier
                                .size(35.dp)
                                .background(
                                    color = colorResource(id = R.color.app), CircleShape
                                )
                                .size(30.dp)

                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Image",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))

                if (isEditing == false) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Edit your profile",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        IconButton(onClick = {
                            isEditing = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFBDE2FF)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (!isEditing) {

                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Name",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color(0xFF333333)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))

                            if (isEditing) {
                                OutlinedTextField(
                                    value = newName,
                                    onValueChange = { newName = it },
                                    label = { Text("Update Name", color = Color.Black) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(Color(0xFFBDE2FF)),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        focusedLabelColor = Color.Black,
                                        unfocusedLabelColor = Color.Black,
                                        focusedIndicatorColor = Color.Black,
                                        unfocusedIndicatorColor = Color.Black,
                                        cursorColor = Color.Black,
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(onClick = {

                                    isUpdating = true
                                    val profielImageByteArray = imageUri.uriToByteArray(context)
                                    profielImageByteArray?.let() {

                                        authViewModel.updateProfile(
                                            ProfielImageByteArray = profielImageByteArray,
                                            name = if (newName.isNotBlank()) newName else currentUser.name,
                                            onResult = { message, boolean ->
                                                if (boolean) {
                                                    Toast.makeText(
                                                        context, message, Toast.LENGTH_SHORT
                                                    ).show()
                                                    updateState = !updateState
                                                    isEditing = false

                                                } else {
                                                    isUpdating = false

                                                    Toast.makeText(
                                                        context, message, Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                            })
                                    }


                                }) {

                                    if (isUpdating) {
                                        CircularProgressIndicator()
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Save",
                                            tint = Color(0xFF333333)
                                        )
                                    }

                                }
                                IconButton(onClick = { isEditing = false }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cancel",
                                        tint = Color(0xFF333333)
                                    )
                                }
                            } else {
                                Text(
                                    text = currentUser.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333),
                                    modifier = Modifier.weight(1f)
                                )

                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFBDE2FF) // your custom blue color
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Name",
                                modifier = Modifier.size(24.dp),
                                tint = Color(0xFF333333)
                            )
                            Spacer(modifier = Modifier.width(12.dp))


                            Text(
                                text = currentUser.email,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333),
                                modifier = Modifier.weight(1f)
                            )


                        }
                    }
                }


            }


        }


    }


}

fun isAppNotificationEnabled(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}

@Composable
fun LogoutAlertDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    onConfirmLogout()
                    onDismiss()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No")
                }
            })
    }
}



