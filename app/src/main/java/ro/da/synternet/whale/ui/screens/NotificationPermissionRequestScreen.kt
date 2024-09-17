package ro.da.synternet.whale.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ro.da.synternet.whale.ui.navigation.NavRoute
import ro.da.synternet.whale.ui.stopMyForegroundService
import ro.da.synternet.whale.viewmodel.DataViewModel

@Composable
fun NotificationPermissionRequestScreen(
    goToUserConfigForm: () -> Unit,
    navGraphBuilder: NavGraphBuilder,
    viewModel: DataViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var isPermissionGranted by remember { mutableStateOf(false) }

    // Launcher per richiedere permessi
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            isPermissionGranted = isGranted
            if (isPermissionGranted) {
                stopMyForegroundService(context)
                goToUserConfigForm()

            } else {
                //todo errore
            }
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true
            } else {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // Non necessario richiedere il permesso per versioni precedenti
            isPermissionGranted = true
            stopMyForegroundService(context)
            goToUserConfigForm()
            // navController.navigate(NavRoute.FormUserConfig.path)
        }
    }
}
