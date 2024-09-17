
# Ethereum & Solana Transaction Alert App

This is a Kotlin-based Android application developed using **Clean Architecture** principles. The app listens to blockchain transaction streams (`synternet.ethereum.tx` and `synternet.solana.tx`) and sends push notifications when an Ethereum (ETH) or Solana (SOL) transaction above a user-defined threshold is detected. Clicking on the notification opens the corresponding transaction on **EthScan.io** or **Solscan.io** in the user's default browser.

## Features
- **Blockchain Monitoring**: Listens to ETH and SOL transaction streams from the **Synternet** network.
- **User Notifications**: Sends push notifications when an ETH or SOL transaction surpasses a specific threshold.
- **Browser Integration**: Clicking a notification redirects the user to the transaction details on **EthScan.io** or **Solscan.io**.
- **Custom Threshold**: Users can define their own ETH or SOL transaction threshold.

## Technologies & Libraries Used
### 1. **Clean Architecture**
   - This app follows **Clean Architecture**, promoting separation of concerns and ensuring scalability, testability, and maintainability.

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
   git clone https://github.com/your-repo/your-project.git
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
4. **Run the App**: Launch the app in Android Studio.

## Usage
1. On first launch, the app will request a user-defined transaction threshold for both Ethereum and Solana.
2. The app will start monitoring blockchain activity in the background.
3. If a transaction surpasses the threshold, a notification will alert the user.
4. Clicking the notification opens a browser with the transaction details.

## Future Enhancements
- **Custom Alerts**: Add more customization for users to set different thresholds for different types of transactions.
- **Multi-chain Support**: Expand support to other blockchain networks.
- **Transaction History**: Provide a transaction history feature in-app for user reference.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments
- **Synternet SDK Team** for providing the reliable DataLayer SDK for blockchain integration.

