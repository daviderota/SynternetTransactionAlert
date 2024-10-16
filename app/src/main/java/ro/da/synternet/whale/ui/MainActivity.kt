package ro.da.synternet.whale.ui

import RootTheme
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
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
        handleIntent(intent)

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
            val dataType = _b.getString(Const.B_CRY_TYPE)
            if (dataType == WebSocketService.Type.ETH.value) {
                val address = _b.getString(Const.B_CRY_ADDRESS) ?: ""
                //open webview etherscan
                val url = "https://etherscan.io/tx/$address"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // start browser
                startActivity(browserIntent)
            } else if (dataType == WebSocketService.Type.SOL.value) {
                val address = _b.getString(Const.B_CRY_ADDRESS) ?: ""
                //open webview etherscan
                val url = "https://solana.io/tx/$address"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                // start browser
                startActivity(browserIntent)
            }
        }
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
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
    if(!context.isServiceRunning(WebSocketService::class.java)) {
        val serviceIntent = Intent(context, WebSocketService::class.java)
        serviceIntent.putExtra(Const.ACCESS_TOKEN, accessToken)
        serviceIntent.putExtra(Const.NATS_URL, natsUrl)
        serviceIntent.putExtra(Const.STREAM_ETH, Const.STREAM_ETH_VALUE)
        serviceIntent.putExtra(Const.STREAM_SOL, Const.STREAM_SOL_VALUE)
        serviceIntent.putExtra(Const.THRESHOLD_ETH, thresholdEth)
        serviceIntent.putExtra(Const.THRESHOLD_SOL, thresholdSol)
        context.startForegroundService(serviceIntent)
    }
}


fun stopMyForegroundService(context: Context) {
    val serviceIntent: Intent = Intent(context, WebSocketService::class.java)
    context.stopService(serviceIntent)
}



@Suppress("DEPRECATION")
fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
    return (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it -> it.service.className == service.name }
}