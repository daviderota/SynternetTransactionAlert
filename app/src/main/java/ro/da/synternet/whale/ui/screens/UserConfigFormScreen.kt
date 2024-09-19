package ro.da.synternet.whale.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import ro.da.synternet.common.isValidUserConfig
import ro.da.synternet.whale.R
import ro.da.synternet.whale.viewmodel.DataViewModel

@Composable
fun UserConfigFormScreen(
    goToServiceActivated: () -> Unit,
    navGraphBuilder: NavGraphBuilder,
    viewModel: DataViewModel = hiltViewModel()
) {
    // val accessToken = viewModel.accessToken
    val natsUrl = viewModel.natsUrl
    val thresholdEth = viewModel.thresholdEth
    var thresholdSol = viewModel.thresholdSol


    // Layout verticale con padding
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally, // Allinea i contenuti orizzontalmente al centro
        verticalArrangement = Arrangement.Center //
    ) {

        Text(
            text = "Hi, i'm TApp and i'll help you intercept the Crypto Whale!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Text(
            text = "Subscribe the 'synternet.ethereum.tx' and 'synternet.solana.tx' streams from https://portal.synternet.com and compile the form behind",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        // Primo campo: Access Token
        TextField(
            value = viewModel.accessToken,
            onValueChange = { viewModel.accessToken = it },
            label = { Text("Access Token") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { }
        )

        // Secondo campo: URL
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

        // Terzo campo: Stream
        TextField(
            value = "synternet.ethereum.tx",
            enabled = false,
            onValueChange = { },
            label = { Text("Ethereum Stream ") },
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
            // Primo campo di testo
            TextField(
                value = thresholdEth,
                onValueChange = { viewModel.thresholdEth = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ethereum), // Nome del file XML
                        contentDescription = "Eth",
                        modifier = Modifier.size(24.dp) // Modifica le dimensioni secondo necessità
                    )

                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { } // Assegna lo spazio in base al peso


            )

            // Secondo campo di testo
            TextField(
                value = thresholdSol,
                onValueChange = { viewModel.thresholdSol = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                trailingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.solana), // Nome del file XML
                        contentDescription = "Sol",
                        modifier = Modifier.size(24.dp) // Modifica le dimensioni secondo necessità
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable { } // Assegna lo spazio in base al peso

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
                )
                {
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
            Text(text = "Save and Run")
        }


    }

}