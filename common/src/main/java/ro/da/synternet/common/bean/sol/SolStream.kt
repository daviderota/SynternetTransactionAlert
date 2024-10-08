package ro.da.synternet.common.bean.sol

import ro.da.synternet.common.decimalSeparator
import ro.da.synternet.common.ueth_number
import ro.da.synternet.common.usol_number
import java.text.DecimalFormatSymbols

data class SolStream(
    val hash: String,
    val value: String
) {

    fun getSolValue(): Float {
        val returnValue = if (value.length == usol_number) {
            value.substring(0, 1) + "$decimalSeparator" + value.substring(1, 3)
        } else if (value.length > usol_number) {
            value.substring(0, value.length - usol_number) + "$decimalSeparator" + value.substring(
                value.length - usol_number,
                (value.length - usol_number) + 2
            )
        } else {
            val leng = usol_number - value.length
            if (leng >= 0) {
                var strLeng = ""
                repeat(leng - 1) {
                    strLeng += "0"
                }
                "0" + "$decimalSeparator$strLeng" + value.substring(0, 2)
            } else
                "0" + "$decimalSeparator" + value.substring(0, 2)
        }
        return returnValue.toFloat()
    }
}