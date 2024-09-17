package ro.da.synternet.whale.ui.navigation

sealed class NavRoute(val path: String) {

    object Splashscreen: NavRoute("splashscreen")
    object RequestPermissions: NavRoute("requestPermissions")
    object FormUserConfig: NavRoute("formUserConfig")
    object ServiceActivated: NavRoute("serviceActivated")

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