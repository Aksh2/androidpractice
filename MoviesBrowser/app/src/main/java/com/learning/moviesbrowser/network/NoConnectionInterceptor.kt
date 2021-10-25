package com.learning.moviesbrowser.network

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class NoConnectionInterceptor @Inject constructor(private val application: Application) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if(!isConnectionOn()){
            throw NoConnectivityException()
        } else if(!isInternetAvailable()){
            throw NoInternetException()
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun isConnectionOn() : Boolean{
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)
            return connection !=null && (connection.hasTransport(TRANSPORT_VPN) ||
                    connection.hasTransport(TRANSPORT_WIFI) ||
                    connection.hasTransport(TRANSPORT_CELLULAR))
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null){
                return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            }
            return false
        }
    }

    private fun isInternetAvailable() : Boolean {
        return try{
            val timeoutMs = 1500
            val sock = Socket()
            val socketaddr = InetSocketAddress("8.8.8.8", 53)

            sock.connect(socketaddr, timeoutMs)
            sock.close()
            return true
        } catch (e: IOException){
            false
        }
    }


    class NoInternetException(): IOException(){
        override val message: String?
            get() = "No Internet available, please check your connected WIFI or Data"
    }

    class NoConnectivityException: IOException(){
        override val message: String?
            get() = "No network available, please check your WIFI or Data connection"
    }
}