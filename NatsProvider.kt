import com.google.gson.Gson
import io.nats.client.Connection
import io.nats.client.Dispatcher
import io.nats.client.MessageHandler
import io.nats.client.NKey
import io.nats.client.Nats
import io.nats.client.Options
import io.nats.client.support.Encoding
import java.io.File
import java.time.Instant
import java.util.*

class NatsProvider(
    private val accessToken: String,
    private val natsUrl: String,
    private val stream: String
) {

    private val options: Options
    private var nc: Connection? = null
    private var connectionDispatcher: Dispatcher? = null

    init {
        val jwt = createAppJwt(accessToken)
        val temp = File.createTempFile("temp", null)
        temp.writeText(jwt)
        val tempPath = temp.absolutePath

        val authHandler = Nats.credentials(tempPath)
        options = Options.Builder().server(natsUrl).authHandler(authHandler).build()
    }

    fun connect(connectionMessageHandler: MessageHandler) {
        nc = Nats.connect(options)
        connectionDispatcher = nc?.createDispatcher(connectionMessageHandler)
    }

    fun subscribe(subscribeMessageHandler: MessageHandler) {
        if (nc == null || connectionDispatcher == null)
            throw Exception("Nats connection is not established")
        else
            connectionDispatcher?.subscribe(stream, subscribeMessageHandler)
    }

    fun unsubscribe() {
        if (nc == null || connectionDispatcher == null)
            throw Exception("Nats connection is not established")
        else
            connectionDispatcher?.unsubscribe(stream)
    }

    fun publish(message: String) {
        if (nc == null)
            throw Exception("Nats connection is not established")
        else
            nc?.publish(stream, message.toByteArray(Charsets.UTF_8))
    }

    fun publish(byteArray: ByteArray) {
        if (nc == null)
            throw Exception("Nats connection is not established")
        else
            nc?.publish(stream, byteArray)

    }

    fun disconnect() {
        nc?.close()
    }


    private fun createAppJwt(seed: String): String {
        val encodedAccSeed = seed.toCharArray()
        val account = NKey.fromSeed(encodedAccSeed)
        val accPubkey = account.publicKey
        val payload = mapOf(
            "jti" to generateJti(),
            "iat" to generateIat(),
            "iss" to String(accPubkey),
            "name" to "developer",
            "sub" to String(accPubkey),
            "nats" to getNatsConfig()
        )
        val jwt = signJwt(payload, account)
        val creds =
            "-----BEGIN NATS USER JWT-----\n$jwt\n------END NATS USER JWT------\n\n************************* IMPORTANT *************************\nNKEY Seed printed below can be used to sign and prove identity.\nNKEYs are sensitive and should be treated as secrets. \n\n-----BEGIN USER NKEY SEED-----\n$seed\n------END USER NKEY SEED------\n\n*************************************************************"
        return creds
    }

    private fun generateJti(): String {
        val timestamp = Instant.now().epochSecond
        val random = Random().nextDouble().toString().substring(2)
        return "$timestamp$random"
    }

    private fun generateIat(): Long {
        return Instant.now().epochSecond
    }

    private fun getNatsConfig(): Map<String, Any> {
        return mapOf(
            "pub" to emptyMap<String, Any>(),
            "sub" to emptyMap<String, Any>(),
            "subs" to -1,
            "data" to -1,
            "payload" to -1,
            "type" to "user",
            "version" to 2
        )

    }

    private fun signJwt(payload: Map<String, Any>, account: NKey): String {
        val header = mapOf("typ" to "JWT", "alg" to "ed25519-nkey")
        val gson = Gson()
        val headerEncoded =
            String(Encoding.base64UrlEncode(gson.toJson(header).toByteArray())).trimEnd('=')
        val payloadEncoded =
            String(Encoding.base64UrlEncode(gson.toJson(payload).toByteArray())).trimEnd('=')

        val jwtBase = "$headerEncoded.$payloadEncoded"
        val signature =
            String(Encoding.base64UrlEncode(account.sign(jwtBase.toByteArray()))).trimEnd('=')

        return "$jwtBase.$signature"
    }
}
