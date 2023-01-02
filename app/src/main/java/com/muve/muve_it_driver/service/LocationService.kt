package com.muve.muve_it_driver.service
import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.*
import com.google.gson.Gson
import com.muve.muve_it_driver.util.NetworkUtility
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PrivateChannel
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import com.pusher.client.util.HttpAuthorizer
import com.toastfix.toastcompatwrapper.ToastHandler
import java.util.*

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val authorizer = HttpAuthorizer(NetworkUtility.authUrl)
    val options = PusherOptions().setAuthorizer(authorizer)
    lateinit var channel: PrivateChannel
    var pusher :Pusher ? =null
    var serviceId :String ? =null
    var driver_updatestatus :String ? =null
    private lateinit var locationCallback: LocationCallback

    private val locationRequest: LocationRequest = create().apply {
        interval = 3000
        fastestInterval = 3000
        priority = PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 5000
    }

    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChanel() {
        val notificationChannelId = "Location channel id"
        val channelName = "Background Service"
        val chan = NotificationChannel(notificationChannelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder =
            NotificationCompat.Builder(this, notificationChannelId)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setContentTitle("Location updates:")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        options.setCluster(NetworkUtility.cluster)
        pusher = Pusher(NetworkUtility.key, options)

        pusher!!.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i(
                    "PushersocketId",
                    "State changed from ${change.previousState} to ${change.currentState}"
                )

                if (change.currentState.toString().equals("CONNECTED", ignoreCase = true)) {
                    val socketId = pusher!!.connection.socketId
                    val channelname = "private-my-channel"
                    Log.v("socketIdService", "socketId" + socketId)
                    val parameters = HashMap<String, String>()
                    parameters["socket_id"] = socketId
                    parameters["channel_name"] = "private-my-channel"
                    authorizer.setHeaders(parameters)
                    pusher!!.unsubscribe(channelname)

                    channel = pusher!!.subscribePrivate("private-my-channel")

                }

            }

            override fun onError(message: String?, code: String?, e: java.lang.Exception?) {

                Log.i(
                    "Pusher",
                    "There was a problem connecting! code ($code), message ($message), exception($e)"
                )
            }

        }, ConnectionState.ALL)

        if (intent!!.hasExtra("id") && intent.hasExtra("status")) {
            serviceId = intent!!.getStringExtra("id")
            driver_updatestatus = intent!!.getStringExtra("status")

            Log.v("serviceId", "serviceId" + serviceId)
            Log.v("driver_updatestatus", "driver_updatestatus" + driver_updatestatus)
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) createNotificationChanel() else startForeground(1, Notification())


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val locationList = locationResult.locations
                if (locationList.isNotEmpty()) {
                    val location = locationList.last()
                   /* ToastHandler.getToastInstance(
                        this@LocationService, "Latitude: " + location.latitude.toString() + '\n' +
                                "Longitude: " + location.longitude, Toast.LENGTH_LONG
                    ).show()*/
                    Log.d("Location d", location.latitude.toString())
                    Log.i("Location i", location.longitude.toString())


                    if (serviceId!=null && !serviceId.equals("")) {

                        val req = HashMap<String, Any>()
                        val gson = Gson()
                        req["id"] = serviceId!!
                        req["driver_long"] = location.longitude.toString()!!
                        req["driver_lat"] = location.latitude.toString()!!
                        req["status"] = driver_updatestatus!!

                        val final = gson.toJson(req)
                        try {

                            if (channel == null) {

                                channel =
                                    pusher!!.subscribePrivate("private-my-channel")
                            }

                            channel.trigger("client-order-progress-status", final)

                          /*  Toast.makeText(this@LocationService, "LatitudePuja: " + location.latitude.toString() + '\n' +
                                    "LongitudePuja: "+ location.longitude, Toast.LENGTH_LONG).show()*/

                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                        Log.v(
                            "clientorderprogresttus",
                            "clientorderprogressstatus" + final
                        )
                    }

                }
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(applicationContext, "Permission required", Toast.LENGTH_LONG).show()

        }else{

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        pusher!!.disconnect()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}