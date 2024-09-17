package ro.da.synternet.common.bean

import kotlinx.coroutines.flow.StateFlow

data class UserConfig(
    var accessToken: String,
    var natsUrl:String,
    var streamEth:String,
    var streamSol:String,
    var thresholdEth:String,
    var thresholdSol:String
)