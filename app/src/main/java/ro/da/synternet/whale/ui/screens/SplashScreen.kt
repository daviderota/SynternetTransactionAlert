package ro.da.synternet.whale.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ro.da.synternet.common.areNotificationsEnabled
import ro.da.synternet.common.isValidUserConfig
import ro.da.synternet.whale.R
import ro.da.synternet.whale.ui.navigation.NavRoute
import ro.da.synternet.whale.ui.stopMyForegroundService
import ro.da.synternet.whale.viewmodel.DataViewModel

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: DataViewModel = hiltViewModel()
) {

    val userConfigLoaded by viewModel.userConfigLoaded.collectAsState()

    val accessToken = viewModel.accessToken
    val natsUrl = viewModel.natsUrl
    val thresholdEth = viewModel.thresholdEth
    val thresholdSol = viewModel.thresholdSol

    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LaunchedEffect(userConfigLoaded) {
            delay(2000)

            if(userConfigLoaded){
                if (isValidUserConfig(
                        accessToken,
                        natsUrl,
                        thresholdEth,
                        thresholdSol
                    )
                ) {
                    if (areNotificationsEnabled(context)) {
                        navController.navigate(NavRoute.ServiceActivated.path)
                    } else {
                        navController.navigate(NavRoute.RequestPermissions.path)
                    }
                } else {
                    stopMyForegroundService(context)
                    if (areNotificationsEnabled(context)) {
                        navController.navigate(NavRoute.FormUserConfig.path)
                    } else {
                        navController.navigate(NavRoute.RequestPermissions.path)
                    }
                }
            }


        }

        LottieAnim(R.raw.whale)
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "Hi, I'm TApp",
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold
        )

    }
}