
# Ethereum & Solana Transaction Alert App

This is a Kotlin-based **Android** application developed using **Clean Architecture** principles. The app listens to blockchain transaction streams (`synternet.ethereum.tx` and `synternet.solana.tx`) and sends push notifications when an Ethereum (ETH) or Solana (SOL) transaction above a user-defined threshold is detected. Clicking on the notification opens the corresponding transaction on **EthScan.io** or **Solscan.io** in the user's default browser.

## Features
- **Blockchain Monitoring**: Listens to ETH and SOL transaction streams from the **Synternet** network.
- **User Notifications**: Sends push notifications when an ETH or SOL transaction surpasses a specific threshold.
- **Browser Integration**: Clicking a notification redirects the user to the transaction details on **EthScan.io** or **Solscan.io**.
- **Custom Threshold**: Users can define their own ETH or SOL transaction threshold.

## Technologies & Libraries Used
### 1. **Clean Architecture**
   - This app follows **Clean Architecture**, promoting separation of concerns and ensuring scalability, testability, and maintainability.
   - It consists of the following modules:
    
      **app**: This module is the application itself, containing the user interface (UI) and initiating the foreground service.

      **common**: This module includes functions that are shared across all modules and supports business logic.

      **datastore**: This module handles saving and reading data on the device.

      **NatsProvider**: This module contains the Kotlin library found at https://github.com/daviderota/syntropy-pubsub-kotlin, which has been validated by the Synternet team and referenced in the SDK Data Layer documentation.

### 2. **Networking**
   - **[OkHttp](https://square.github.io/okhttp/)**: Handles HTTP connections and low-level networking operations.
   - **[Retrofit](https://square.github.io/retrofit/)**: Provides an abstraction over OkHttp for making REST API calls to third-party services.

### 3. **Data Persistence**
   - **[DataStore](https://developer.android.com/topic/libraries/architecture/datastore)**: A modern, fast, and efficient solution for storing user preferences, used to persist user-defined transaction thresholds.

### 4. **UI & User Experience**
   - **Jetpack Compose**: A modern, declarative UI toolkit for Android, used to build the user interface.
   - **Foreground Services**: Ensures the app remains active in the background while listening to transaction streams, even when the user closes the app.

### 5. **Synternet SDK**
   - **[Synternet SDK DataLayer](https://github.com/daviderota/syntropy-pubsub-kotlin/)**: Used to subscribe to `synternet.ethereum.tx` and `synternet.solana.tx` streams, ensuring real-time transaction monitoring. This SDK is a third-party, verified by the Synternet team and fully integrated into the app for blockchain event handling.

## App Flow
1. **Foreground Service**: The app starts a foreground service upon user initiation to continuously listen to transaction streams.
2. **Blockchain Subscription**: Using the Synternet SDK, the app subscribes to `synternet.ethereum.tx` and `synternet.solana.tx` streams.
3. **Threshold Check**: Each incoming transaction is analyzed. If the transaction value exceeds the user-defined threshold:
   - A **push notification** is sent to the user with details of the transaction (amount and address).
4. **Notification Action**: When the user taps on the notification:
   - The app opens the default browser on the device, showing the transaction details on either **EthScan.io** (for ETH) or **Solscan.io** (for SOL).

## Setup & Installation
### Prerequisites
- **Android Studio**: Latest version.
- **Kotlin**: The app is written in Kotlin and requires a Kotlin-compatible environment.
- **Minimum SDK Version**: 21 (Android 5.0 Lollipop)

### Steps
1. **Clone the Repository**
   ```bash
   git clone https://github.com/daviderota/SynternetTransactionAlert.git
   ```
2. **Open the Project in Android Studio**
3. **Configure API & Dependencies**
   - The app uses `OkHttp`, `Retrofit`, and the `Synternet SDK`. Ensure these libraries are correctly added to the `build.gradle` file.
   ```groovy
   dependencies {
       implementation "com.squareup.okhttp3:okhttp:<version>"
       implementation "com.squareup.retrofit2:retrofit:<version>"
       implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:<version>"
       implementation 'androidx.datastore:datastore-preferences:<version>'
       implementation 'androidx.compose.ui:ui:<version>'
       implementation 'androidx.work:work-runtime-ktx:<version>'

       // Synternet SDK
       implementation 'com.github.daviderota:syntropy-pubsub-kotlin:<latest_version>'
   }
   ```
4. **Architecture schema**
   ![Architecture](https://github.com/daviderota/SynternetTransactionAlert/blob/main/release/Architecture.png)
   

5. **Run the App**: Launch the app in Android Studio.

   OR **Run compiled App**:
   Alternatively, you can install the compiled version, namely:
   1. The first thing to do is to enable Developer Mode on your smartphone. (If you don't know how, search on Google.com for a guide that explains how to do it based on the model of your smartphone.)

2. Download the file found in this repository: release/app-release.apk to your smartphone and install it by clicking on it.

3. Once installed, launch the application "WApp."


## Usage
1. On first launch, the app will request a user-defined transaction threshold for both Ethereum and Solana.
2. The app will start monitoring blockchain activity in the background.
3. If a transaction surpasses the threshold, a notification will alert the user.
4. Clicking the notification opens a browser with the transaction details.

## Step-by-step
Step 1: Splash Screen
<div style="display: flex; align-items: center;">
    <img src="https://github.com/daviderota/SynternetTransactionAlert/blob/main/release/001_Splash.png"style="width: 150px; margin-right: 20px;"/>
    <p>
        Upon opening the app, you will be greeted by the splash screen displaying the app’s logo. Wait a few seconds as the app loads.
    </p>
</div>



Step 2: Permission Requests
<div style="display: flex; align-items: center;">
    <img src="https://github.com/daviderota/SynternetTransactionAlert/blob/main/release/002_Permissions.png"style="width: 150px; margin-right: 20px;"/>
    <p>
        The app will ask for permission to access the camera and to send push notifications. Click Allow to grant the necessary permissions for the app to function properly.
    </p>
</div>


Step 3: App Configuration
<div style="display: flex; align-items: center;">
    <img src="https://github.com/daviderota/SynternetTransactionAlert/blob/main/release/003_Configuration.png"style="width: 150px; margin-right: 20px;"/>
    <p>
       Navigate to the configuration screen where you can enter the required details. 
       Make sure to subscribe to the 'synternet.ethereum.tx' and 'synternet.solana.tx' streams on the Synternet Portal to monitor transactions.
       The NATS URL can be found in the <a href='https://docs.synternet.com/build/dl-access-points'>official documentation</a>.
    </p>
</div>

Step 4:Service Started
<div style="display: flex; align-items: center;">
    <img src="https://github.com/daviderota/SynternetTransactionAlert/blob/main/release/005_Service_started.png"style="width: 150px; margin-right: 20px;"/>
    <p>
      Once the service has started, you will see a notification indicating that monitoring is active. The app is now listening for transactions on both the Ethereum and Solana networks.
    </p>
</div>

Step 5:  ETH Transaction Notification
<div style="display: flex; align-items: center;">
    <img src="https://github.com/daviderota/SynternetTransactionAlert/blob/main/release/004_Eth_Notification.png"style="width: 150px; margin-right: 20px;"/>
    <p>
      When the app detects an Ethereum transaction that exceeds the set threshold, you will receive a push notification with details, including the amount of ETH transferred and the address involved.
    </p>
</div>


## Testing and Results
Testing Guidelines and Results
How to Test the Project:

Set up the environment: Ensure the required dependencies are installed, and user has the necessary access token to the Synternet layer.
Install the app: Follow the installation steps to install the app on your Android device.
Configure the streams: After launching the app, set the thresholds and transaction alerts for Ethereum and Solana, as shown in the configuration screen.
Test notifications:
Simulate a transaction that exceeds the set threshold.
Confirm that a push notification is received with the transaction details.
Verify navigation: Click on the notification and ensure it redirects to the correct URL on either EthScan.io or SolScan.io.
Results:

The app should detect and alert the user of any relevant transactions.
Push notifications should contain accurate details and function as expected.
All error cases should be handled gracefully, providing feedback if streams fail or permissions aren't granted.

## Future Enhancements
- **Custom Alerts**: Add more customization for users to set different thresholds for different types of transactions.
- **Multi-chain Support**: Expand support to other blockchain networks.
- **Transaction History**: Provide a transaction history feature in-app for user reference.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments
- **Synternet SDK Team** for providing the reliable DataLayer SDK for blockchain integration.

