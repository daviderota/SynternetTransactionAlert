package ro.da.synternet.whale.ui

import RootTheme
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ro.da.synternet.common.Const
import ro.da.synternet.whale.ui.navigation.BuildNavGraph
import ro.da.synternet.whale.viewmodel.DataViewModel
import service.WebSocketService


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        handleIntent(intent) // Gestisci i dati dell'intent

        setContent {
            RootTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(this, viewModel())
                }
            }
        }


    }


    private fun handleIntent(intent: Intent?) {
        intent?.extras?.let { _b ->

            val dataType = _b.getString("cry_type")
            if (dataType == WebSocketService.Type.ETH.value) {
                val address = _b.getString("cry_address") ?: ""
                //open webview etherscan
                val url = "https://etherscan.io/tx/$address"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // Avvio del browser
                startActivity(browserIntent)
            } else if (dataType == WebSocketService.Type.ETH.value) {
                val address = _b.getString("cry_address") ?: ""
                //open webview etherscan
                val url = "https://solana.io/tx/$address"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // Avvio del browser
                startActivity(browserIntent)
            }


        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent);
        setIntent(intent);  // Imposta il nuovo intent
        handleIntent(intent);  // Gestisci i dati del nuovo intent

    }


}


@Composable
fun MainScreen(context: Context, viewModel: DataViewModel) {

    val navController = rememberNavController()
    BuildNavGraph(context, navController, viewModel)


}


fun startMyForegroundService(
    context: Context,
    accessToken: String,
    natsUrl: String,
    thresholdEth: String,
    thresholdSol: String
) {
    val serviceIntent: Intent = Intent(context, WebSocketService::class.java)
    serviceIntent.putExtra(Const.ACCESS_TOKEN, accessToken)
    serviceIntent.putExtra(Const.NATS_URL, natsUrl)
    serviceIntent.putExtra(Const.STREAM_ETH, Const.STREAM_ETH_VALUE)
    serviceIntent.putExtra(Const.STREAM_SOL, Const.STREAM_SOL_VALUE)
    serviceIntent.putExtra(Const.THRESHOLD_ETH, thresholdEth)
    serviceIntent.putExtra(Const.THRESHOLD_SOL, thresholdSol)
    context.startForegroundService(serviceIntent) // Lancia il servizio
}


fun stopMyForegroundService(context: Context) {
    val serviceIntent: Intent = Intent(context, WebSocketService::class.java)
    context.stopService(serviceIntent) // stoppa il servizio
}

/*
@Composable
fun next(
    navController: NavController,
    modifier: Modifier,
    viewModel: DataViewModel,
    isPermissionGranted: Boolean,
    accessToken: String,
    natsUrl: String,
    thresholdEth: String,
    thresholdSol: String
) {
    val context = LocalContext.current
    if (isPermissionGranted) {
        if (isValidUserConfig(
                accessToken,
                natsUrl,
                thresholdEth,
                thresholdSol
            )
        ) {
            startMyForegroundService(
                context,
                accessToken,
                natsUrl,
                thresholdEth,
                thresholdSol
            )
        } else {
            stopMyForegroundService(context)
            navController.navigate("form")
        }
    } else {
        // UI per richiedere il permesso
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            LottieAnim(R.raw.enable_push, modifier)
            Text(text = "Le notifiche non sono abilitate.")
            Button(onClick = {
                navController.navigate("requestPermissions")
            }) {
                Text(text = "Abilita notifiche")
            }

        }
    }
}
*/