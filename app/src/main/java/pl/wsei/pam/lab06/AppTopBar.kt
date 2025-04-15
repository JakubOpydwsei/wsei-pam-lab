package pl.wsei.pam.lab06

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pl.wsei.pam.lab06.Lab06Activity.Companion.messageExtra
import pl.wsei.pam.lab06.Lab06Activity.Companion.titleExtra
import pl.wsei.pam.lab06.notifications.NotificationBroadcastReceiver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavController,
    title: String,
    showBackIcon: Boolean,
    route: String
) {
    val context = LocalContext.current

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(text = title) },
        navigationIcon = {
            if (showBackIcon) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (route != "form") {

            } else {
                IconButton(onClick = {
                    val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
                        putExtra(titleExtra, "Test Option")
                        putExtra(messageExtra, "Option button was clicked, alarm was executed")
                    }
                    context.sendBroadcast(intent)
                }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Test Notification")
                }

                IconButton(onClick = { /* TO DO */ }) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "Go Home")
                }
            }
        }
    )
}
