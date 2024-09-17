package ro.da.synternet.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ro.da.synternet.common.Const
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        // Chiave per recuperare i dati
        val accessTokenKey = stringPreferencesKey(Const.ACCESS_TOKEN)
        val natsUrlKey = stringPreferencesKey(Const.NATS_URL)
        val streamEthKey = stringPreferencesKey(Const.STREAM_ETH)
        val streamSolKey = stringPreferencesKey(Const.STREAM_SOL)
        val thresholdEthKey = stringPreferencesKey(Const.THRESHOLD_ETH)
        val thresholdSolKey = stringPreferencesKey(Const.THRESHOLD_SOL)
    }

    // Funzione per recuperare l'access token da DataStore
    suspend fun getAccessTokenFromDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.accessTokenKey]
        }.first() ?: ""
    }

    suspend fun getNatsUrlDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.natsUrlKey]
        }.first() ?: ""
    }

    suspend fun getStreamEthDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.streamEthKey]
        }.first() ?: ""
    }

    suspend fun getStreamSolDataStore(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.streamSolKey]
        }.first() ?: ""
    }



    suspend fun getThresholdSol(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.thresholdSolKey]
        }.first() ?: "0"
    }

    suspend fun getThresholdEth(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.thresholdEthKey]
        }.first() ?: "0"
    }


    // Funzione per salvare i dati
    suspend fun save(
        accessToken: String,
        natsUrl: String,
        streamEth: String,
        streamSol: String,
        thresholdEth: String,
        thresholdSol: String
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.accessTokenKey] = accessToken
            preferences[PreferencesKeys.natsUrlKey] = natsUrl
            preferences[PreferencesKeys.streamEthKey] = streamEth
            preferences[PreferencesKeys.streamSolKey] = streamSol
            // 21004032965000000
            preferences[PreferencesKeys.thresholdEthKey] = thresholdEth.toInt().toString()
            preferences[PreferencesKeys.thresholdSolKey] = thresholdSol.toInt().toString()

        }
    }


}
