package com.mhdrk.openweather.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.mhdrk.openweather.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var currentLocation: FusedLocationProviderClient
    private var myRequestCode=1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        SplashScreenIV.alpha = 0f
        SplashScreenIV.animate().setDuration(2500).alpha(1f).withEndAction{
            val intent = Intent(this, MainActivity::class.java)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        currentLocation= LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }

//    1. location permission --> deny
//    2. location denied through settings
//    3. gps off
//    4. permission le lo

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(checkPermission()) {
            if(locationEnable()){
                currentLocation.lastLocation.addOnCompleteListener{
                        task->
                    val location:Location?=task.result
                    if(location==null)
                    {
                        newLocation()
                    }else{
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent= Intent(this, MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("lon",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        },3000)
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on your GPS location",Toast.LENGTH_LONG).show()
            }
        }else{
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun newLocation() {
        val locationRequest=LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1
        currentLocation=LocationServices.getFusedLocationProviderClient(this)
        Looper.myLooper()
            ?.let { currentLocation.requestLocationUpdates(locationRequest,locationCallback, it) }
    }
    private val locationCallback=object: LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation:Location=p0.lastLocation
        }
    }

    private fun locationEnable(): Boolean {
        val locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION),myRequestCode)
    }

    private fun checkPermission(): Boolean {
        if(
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==myRequestCode)
        {
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation()
            }
        }
    }
}