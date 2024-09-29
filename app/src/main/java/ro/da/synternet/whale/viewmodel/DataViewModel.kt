package ro.da.synternet.whale.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ro.da.synternet.common.Const
import ro.da.synternet.datastore.repository.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    var accessToken by mutableStateOf("")
    var natsUrl by mutableStateOf("")
    var streamEth by mutableStateOf("")
    var streamSol by mutableStateOf("synternet.solana.tx")
    var thresholdEth by mutableStateOf("")
    var thresholdSol by mutableStateOf("")
    private val _userConfigLoaded = MutableStateFlow(false)
    val userConfigLoaded: StateFlow<Boolean> = _userConfigLoaded

    private var _isSaved by mutableStateOf(false)
        private set

    init {

        viewModelScope.launch {
            accessToken = userPreferencesRepository.getAccessTokenFromDataStore()
            Log.d("MyDataStoreService", "Access Token: $accessToken")

            natsUrl = userPreferencesRepository.getNatsUrlDataStore()
            Log.d("MyDataStoreService", "Nats url: $natsUrl")

            streamEth = userPreferencesRepository.getStreamEthDataStore()
            Log.d("MyDataStoreService", "Stream Data Store Eth: $streamEth")


            streamSol = userPreferencesRepository.getStreamSolDataStore()
            Log.d("MyDataStoreService", "Stream Data Store Sol: $streamSol")

            thresholdSol = userPreferencesRepository.getThresholdSol()

            thresholdEth = userPreferencesRepository.getThresholdEth()
            _userConfigLoaded.value = true
        }

    }


    /**
     * 3 000 000 000 000 000 000 -> 3ETH
     * 2 100 403 296 500 00 00
     */

    fun saveData(
        accessToken: String,
        natsUrl: String,
        thresholdEth: String,
        thresholdSol: String,
        callback: () -> Unit
    ) {
        _isSaved = false
        viewModelScope.launch {
            userPreferencesRepository.save(
                accessToken,
                natsUrl,
                Const.STREAM_ETH_VALUE,
                Const.STREAM_SOL_VALUE,
                thresholdEth,
                thresholdSol
            )
            callback()
            _isSaved = true
        }
    }

}