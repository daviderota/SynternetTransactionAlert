package ro.da.synternet.whale.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ro.da.synternet.whale.ui.screens.NotificationPermissionRequestScreen
import ro.da.synternet.whale.ui.screens.ServiceActivatedScreen
import ro.da.synternet.whale.ui.screens.SplashScreen
import ro.da.synternet.whale.ui.screens.UserConfigFormScreen
import ro.da.synternet.whale.viewmodel.DataViewModel

@Composable
fun BuildNavGraph(context: Context, navController: NavHostController, viewModel: DataViewModel) {

    NavHost(
        navController = navController,
        startDestination = NavRoute.Splashscreen.path
    ) {

        addSplash(navController, this, viewModel)

        addNotificationPermissionRequest(navController, this)

        addUserConfig(navController, this)

        addServiceActivated(context = context, navController, this)
    }
}

private fun addSplash(
    navController: NavHostController,
    navGraphBuilder: NavGraphBuilder,
    viewModel: DataViewModel
) {
    navGraphBuilder.composable(route = NavRoute.Splashscreen.path) {
        SplashScreen(
            navController,
            viewModel = viewModel
        )
    }
}

private fun addNotificationPermissionRequest(
    navController: NavController,
    navGraphBuilder: NavGraphBuilder
) {

    navGraphBuilder.composable(route = NavRoute.RequestPermissions.path) {

        NotificationPermissionRequestScreen(goToUserConfigForm = {
            navController.navigate(NavRoute.FormUserConfig.path)
        })
    }

}


fun goToServiceActivated(navController: NavController) {
    navController.navigate(NavRoute.ServiceActivated.path)
}

private fun addUserConfig(
    navController: NavController,
    navGraphBuilder: NavGraphBuilder
) {

    navGraphBuilder.composable(route = NavRoute.FormUserConfig.path) {
        UserConfigFormScreen(goToServiceActivated = {
            goToServiceActivated(navController)
        })

    }
}

private fun addServiceActivated(
    context: Context,
    navController: NavController,
    navGraphBuilder: NavGraphBuilder
) {
    navGraphBuilder.composable(route = NavRoute.ServiceActivated.path) {
        ServiceActivatedScreen(context,
            goToUserConfig = {
                navController.navigate(
                    route = NavRoute.FormUserConfig.path)
            }
        )

    }
}