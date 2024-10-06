package ro.da.synternet.whale.ui.navigation

import androidx.navigation.navArgument

sealed class NavRoute(val path: String) {

    data object Splashscreen: NavRoute("splashscreen")
    data object RequestPermissions: NavRoute("requestPermissions")
    data object FormUserConfig: NavRoute("formUserConfig")
    data object ServiceActivated: NavRoute("serviceActivated")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/$arg")
            }
        }
    }

    fun withArgsFormat(vararg args: String) : String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/{$arg}")
            }
        }
    }
}