package br.com.chucknorrisfacts.webservice

import android.net.ConnectivityManager

/**
 * @rodrigohsb
 */
class NetworkService (private var connectivityManager: ConnectivityManager) {

    val isConnected: Boolean
        get() {
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
}