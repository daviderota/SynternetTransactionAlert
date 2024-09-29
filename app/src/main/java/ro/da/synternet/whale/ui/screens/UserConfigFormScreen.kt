package ro.da.synternet.whale.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ro.da.synternet.common.isValidUserConfig
import ro.da.synternet.whale.R
import ro.da.synternet.whale.viewmodel.DataViewModel

@Composable
fun UserConfigFormScreen(
    goToServiceActivated: () -> Unit,
    viewModel: DataViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    BackHandler {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(id = R.string.alert_exit_title)) },
            text = { Text(stringResource(id = R.string.alert_exit_questions)) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    activity?.finishAffinity()
                    // Perform the back action manually
                }) {
                    Text(stringResource(id = R.string.btn_yes))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(id = R.string.btn_no))
                }
            }
        )
    }

    val natsUrl = viewModel.natsUrl
    val thresholdEth = viewModel.thresholdEth
    var thresholdSol = viewModel.thresholdSol

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally, // Allinea i contenuti orizzontalmente al centro
        verticalArrangement = Arrangement.Center //
    ) {

        Text(
            text = stringResource(id = R.string.user_config_hello),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Text(
            text = stringResource(id = R.string.user_config_description),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = viewModel.accessToken,
            onValueChange = { viewModel.accessToken = it },
            label = { Text("Access Token") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { }
        )

        TextField(
            value = natsUrl,
            onValueChange = { viewModel.natsUrl = it },
            label = { Text("URL") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { }
        )

        TextField(
            value = "synternet.ethereum.tx",
            enabled = false,
            onValueChange = { },
            label = { Text("Ethereum Stream") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { }
        )

        TextField(
            value = "synternet.solana.tx",
            enabled = false,
            onValueChange = {},
            label = { Text("Solana Stream") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { },
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Threshold")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { },
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = thresholdEth,
                onValueChange = { viewModel.thresholdEth = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ethereum),
                        contentDescription = "Eth",
                        modifier = Modifier.size(24.dp)
                    )

                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { }
            )

            TextField(
                value = thresholdSol,
                onValueChange = { viewModel.thresholdSol = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.solana),
                        contentDescription = "Sol",
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveData(
                    viewModel.accessToken,
                    natsUrl,
                    thresholdEth,
                    thresholdSol
                ) {
                    goToServiceActivated()
                }
            },
            enabled = isValidUserConfig(
                viewModel.accessToken,
                natsUrl,
                thresholdEth,
                thresholdSol
            )
        ) {
            Text(text = stringResource(id = R.string.save_and_run))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}