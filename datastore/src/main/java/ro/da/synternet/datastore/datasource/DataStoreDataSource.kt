package ro.da.synternet.datastore.datasource

import kotlinx.coroutines.flow.Flow

/**
 * val accessToken = "access_token"
 *     val natsUrl = "nats://nats.url"
 *     val streamName = "syntropy.bitcoin.tx"
 */
interface DataStoreDataSource {

    suspend fun setAccessToken(value: String)
    fun getAccessToken(): Flow<String>

    suspend fun setNatsUrl(value: String)
    fun getNatsUrl(): Flow<String>

    suspend fun setStreamName(value: String)
    fun getStreamName(): Flow<String>


}