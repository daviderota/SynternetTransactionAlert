package ro.da.synternet.whale.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ro.da.synternet.common.isValidUserConfig
import ro.da.synternet.whale.R
import ro.da.synternet.whale.ui.startMyForegroundService
import ro.da.synternet.whale.ui.stopMyForegroundService
import ro.da.synternet.whale.viewmodel.DataViewModel

var isPermissionGranted: Boolean = false


@Composable
fun ServiceActivatedScreen(
    context: Context,
    goToUserConfig: () -> Unit,
    viewModel: DataViewModel = hiltViewModel()
) {

    val accessToken = viewModel.accessToken
    val natsUrl = viewModel.natsUrl
    val thresholdEth = viewModel.thresholdEth
    val thresholdSol = viewModel.thresholdSol


    if (isValidUserConfig(
            accessToken,
            natsUrl,
            thresholdEth,
            thresholdSol
        )
    ) {
        startMyForegroundService(
            LocalContext.current, accessToken,
            natsUrl,
            thresholdEth,
            thresholdSol
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally, // Allinea i contenuti orizzontalmente al centro
            verticalArrangement = Arrangement.Center //
        ) {


            Text(
                text = stringResource(id = R.string.activated_services_hello),
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 42.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(id = R.string.activated_services_whale_assistant),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )



            LottieAnim(R.raw.skyrocket)
            Text(
                text = stringResource(id = R.string.activated_services_is_started),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    stopMyForegroundService(context)
                    goToUserConfig()
                }) {
                Text(text = stringResource(id = R.string.activated_services_stop_and_edit))
            }



        }
    }

}