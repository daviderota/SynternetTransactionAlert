package ro.da.synternet.common.bean.eth

import com.google.gson.annotations.SerializedName
import ro.da.synternet.common.decimalSeparator
import ro.da.synternet.common.ueth_number

data class EthStream(
    @SerializedName("hash") val hash: String,
    @SerializedName("value") val value: String//eth moved
) {

    fun getEthValue(): Float {
        try {
            val returnValue = if (value.length == ueth_number) {
                value.substring(0, 1) + "$decimalSeparator" + value.substring(1, 3)
            } else if (value.length > ueth_number) {
                value.substring(
                    0,
                    (value.length - ueth_number)+1
                ) + "$decimalSeparator" + value.substring(
                    value.length - ueth_number,
                    (value.length - ueth_number) + 2
                )
            } else {
                if(value.length >1) {
                    "0" + "$decimalSeparator" + value.substring(0, 2)
                }else{
                    "0"+decimalSeparator+"00"
                }
            }
            return returnValue.toFloat()
        } catch (ex: Exception) {
            var foo = 0
        }
        return 0.toFloat()
    }
}