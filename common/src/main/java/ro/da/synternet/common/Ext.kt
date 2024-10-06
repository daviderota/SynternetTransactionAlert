package ro.da.synternet.common

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import java.text.DecimalFormatSymbols

val ueth_number: Int = "1000000000000000000".length //number of ueth that comose 1 eth
val usol_number: Int = "1000000000000000000".length //number of usol that compose 1 sol
val decimalSeparator = "."//DecimalFormatSymbols.getInstance().decimalSeparator

fun isValidUserConfig(
    accessToken: String,
    natsUrl: String,
    thresholdEth: String,
    thresholdSol: String
): Boolean {
    return (accessToken.trim().isNotEmpty() &&
            natsUrl.trim().isNotEmpty() &&
            thresholdSol.trim().isNotEmpty() &&
            thresholdEth.trim().isNotEmpty() &&
            thresholdSol.trim().toLong() > 0 &&
            thresholdEth.trim().toLong() > 0)
}


fun areNotificationsEnabled(context: Context): Boolean {
    val notificationManagerCompat = NotificationManagerCompat.from(context)
    return notificationManagerCompat.areNotificationsEnabled()
}