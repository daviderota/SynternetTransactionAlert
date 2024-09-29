package service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import io.nats.client.MessageHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ro.da.synternet.common.Const
import ro.da.synternet.common.bean.eth.EthStream
import ro.da.synternet.common.bean.sol.SolStream
import ro.da.synternet.common.isValidUserConfig
import ro.da.synternet.natsprovider.NatsProvider
import ro.da.synternet.whale.R
import ro.da.synternet.whale.ui.MainActivity
import java.nio.charset.StandardCharsets
import java.util.Random


class WebSocketService : Service() {

    private var accessToken: String? = null
    private var natsUrl: String? = null
    private var streamEth: String? = null
    private var streamSol: String? = null
    private var thresholdEth: Long? = null
    private var thresholdSol: Long? = null


    private val job = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + job)

    private var natsProviderEth: NatsProvider? = null
    private var natsProviderSol: NatsProvider? = null

    private var gson: Gson = Gson()
    private var notificationId: Random = Random()


    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        accessToken = intent?.getStringExtra(Const.ACCESS_TOKEN)
        natsUrl = intent?.getStringExtra(Const.NATS_URL)
        streamEth = intent?.getStringExtra(Const.STREAM_ETH)
        streamSol = intent?.getStringExtra(Const.STREAM_SOL)
        thresholdEth = intent?.getStringExtra(Const.THRESHOLD_ETH)?.toLong()//?.toUETH()
        thresholdSol = intent?.getStringExtra(Const.THRESHOLD_SOL)?.toLong()//?.toUSOL()

        if (isValidUserConfig(
                accessToken ?: "",
                natsUrl ?: "",
                (thresholdEth ?: "").toString(),
                (thresholdSol ?: "").toString()
            )
        ) {
            serviceScope.launch {
                startWebSocketEth(accessToken ?: "", natsUrl ?: "", streamEth ?: "")
                startWebSocketSol(accessToken ?: "", natsUrl ?: "", streamSol ?: "")
            }
        }

        createNotificationChannel()
        val notification = createServiceNotification()
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, "FS_CHANNEL_ID")
            .setContentTitle(getString(R.string.service_name))
            .setContentText(getString(R.string.service_welcome_message))
            .setOngoing(true)
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_notification).build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "FS_CHANNEL_ID",
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        natsProviderEth?.unsubscribe()
        natsProviderEth?.disconnect()

        natsProviderSol?.unsubscribe()
        natsProviderSol?.disconnect()
    }

    private fun startWebSocketEth(accessToken: String, natsUrl: String, stream: String) {
        try {
            natsProviderEth = NatsProvider(accessToken, natsUrl, "smith.stream.live")
            natsProviderEth?.connect(connectionMessageHandlerEth)
            natsProviderEth?.subscribe(subscribeMessageHandlerEth)
        } catch (e: Exception) {

        }
    }

    private fun startWebSocketSol(accessToken: String, natsUrl: String, stream: String) {
        try {
            natsProviderSol = NatsProvider(accessToken, natsUrl, stream)
            natsProviderSol?.connect(connectionMessageHandlerSol)
            natsProviderSol?.subscribe(subscribeMessageHandlerSol)
        } catch (e: Exception) {

        }
    }


    private val connectionMessageHandlerEth = MessageHandler { connectionMsg ->
    }

    private val subscribeMessageHandlerEth = MessageHandler { subscribeMsg ->
        val response = String(subscribeMsg.data, StandardCharsets.UTF_8)
        Log.d("ETH", response)
        val ethStream = gson.fromJson(response, EthStream::class.java)
        thresholdEth?.let { thresholdEth ->
            val ethValue = ethStream.getEthValue()
            if (ethValue > thresholdEth.toFloat()) {
                triggerLocalNotification(Type.ETH, ethStream.hash, ethValue.toString())
            }

        }
    }


    private val connectionMessageHandlerSol = MessageHandler { connectionMsg ->
    }

    private val subscribeMessageHandlerSol = MessageHandler { subscribeMsg ->
        val response = String(subscribeMsg.data, StandardCharsets.UTF_8)
        Log.d("SOL", response)
        val solStream = gson.fromJson(response, SolStream::class.java)
        thresholdSol?.let { thresholdSol ->
            val solValue = solStream.getSolValue()
            if (solValue > thresholdSol.toFloat()) {
                triggerLocalNotification(Type.SOL, solStream.hash, solValue.toString())
            }

        }

    }

    private fun triggerLocalNotification(type: Type, address: String?, value: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = createLocalNotification(type, value, address)
        notificationManager.notify(notificationId.nextInt(100), notification)
    }

    enum class Type(val value: String) {
        ETH("ETH"), SOL("SOL")
    }

    private fun createLocalNotification(type: Type, value: String, address: String?): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("cry_address", address)
        bundle.putString("cry_value", value)
        bundle.putString("cry_type", if (type == Type.ETH) "ETH" else "SOL")
        notificationIntent.putExtras(bundle)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 1010, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )


        val title = when (type) {
            Type.ETH -> "Eth Whale"
            Type.SOL -> "SOLANA WHALE"
        }

        val crypto = when (type) {
            Type.ETH -> "Ethereum"
            Type.SOL -> "Solana"
        }

        val icon = when (type) {
            Type.ETH -> R.drawable.ethereum
            Type.SOL -> R.drawable.solana
        }
        val addressStr = if (address != null)
            "($address)"
        else ""

        return NotificationCompat.Builder(this, "FS_CHANNEL_ID")
            .setContentTitle(title)
            .setContentText(getString(R.string.service_someone_moved) + " $value $crypto $addressStr")
            .setOngoing(true)
            .setAutoCancel(true)
            .setSmallIcon(icon)
            .setContentIntent(pendingIntent)
            .build()
    }
}