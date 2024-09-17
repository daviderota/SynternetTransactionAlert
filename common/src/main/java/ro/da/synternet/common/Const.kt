package ro.da.synternet.common

class Const{
    companion object {
        const val STRING_PREFERENCE: String = "settings"
        const val ACCESS_TOKEN: String = "access_token"
        const val NATS_URL:String = "nats_url"
        const val THRESHOLD_ETH = "THRESHOLD_ETH"
        const val THRESHOLD_SOL = "THRESHOLD_SOL"
        const val STREAM_ETH:String = "stream_eth"
        const val STREAM_SOL:String = "stream_sol"

        const val STREAM_ETH_VALUE:String = "synternet.ethereum.tx"
        const val STREAM_SOL_VALUE:String = "synternet.solana.tx"
    }
}
