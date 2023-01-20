package com.ggb.nirvanaclub.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*
import android.os.Build
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.Gravity
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.ggb.nirvanaclub.MainActivity
import com.ggb.nirvanaclub.R
import com.ggb.nirvanaclub.constans.C
import com.ggb.nirvanaclub.event.StepRefreshEvent
import com.ggb.nirvanaclub.net.GGBContract
import com.ggb.nirvanaclub.net.GGBPresent
import com.ggb.nirvanaclub.utils.ShakeDetector
import com.ggb.nirvanaclub.utils.SharedPreferencesUtil
import com.yanzhenjie.permission.AndPermission
import org.jetbrains.anko.toast


class StepCalculationService : Service() , GGBContract.View , ShakeDetector.OnShakeListener{

    private lateinit var present: GGBPresent

    private lateinit var mShakeDetector :ShakeDetector

    private var mSensorManager: SensorManager? = null

    private var mLocationClient: AMapLocationClient? = null

    private var isStart = true

    private var lat = "0.0"
    private var lon  = "0.0"


    override fun onCreate() {
        super.onCreate()
        present = GGBPresent(this)
        isStart = true

        Log.e("TAG", "service启动了=========>: ", )
        startStepTask()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel("step_service","主服务",NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(false)
            channel.setShowBadge(false)
            channel.setSound(null, null)
            channel.enableVibration(false)
            channel.description = "123456"
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            manager.createNotificationChannel(channel)

            val pIntent = PendingIntent.getActivity(this,0,Intent(Intent(this, MainActivity::class.java)),0)
            val notification = Notification.Builder(this)
                .setChannelId("step_service")
                .setAutoCancel(true)
                .setContentTitle("牛蛙呐")
                .setContentText("牛蛙呐正在运行中...")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.logo)
                .setOngoing(true)
                .setContentIntent(pIntent)
                .build()
            startForeground(1,notification)
        }

        mShakeDetector = ShakeDetector(this)
        mShakeDetector.registerOnShakeListener(this)
        mShakeDetector.start()

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            checkPermission()
        }else{
            mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            mSensorManager?.registerListener(mSensorListener, mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_NORMAL)
        }

        AMapLocationClient.setApiKey(C.A_MAP_API)
        mLocationClient = AMapLocationClient(applicationContext)
        val mLocationOption = AMapLocationClientOption()
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        mLocationOption.isNeedAddress = true
        mLocationOption.interval = 3*60*1000
        mLocationClient?.setLocationOption(mLocationOption)
        mLocationClient?.setLocationListener(localListener)

        getLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun checkPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.ACTIVITY_RECOGNITION)
            .onGranted { permissions ->
                mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
                mSensorManager?.registerListener(mSensorListener, mSensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_NORMAL)
            }
            .onDenied { permissions ->
                toast("不赋于该权限应用将无法正常计步").setGravity(Gravity.CENTER, 0, 0)
            }
            .start()
    }

    private fun getLocationPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION )
            .onGranted { permissions ->
                mLocationClient?.startLocation()
            }
            .onDenied { permissions ->
                toast("不赋于定位权限将无法正常使用车行记录").setGravity(Gravity.CENTER, 0, 0)
            }
            .start()
    }

    private val localListener = AMapLocationListener {
        if(it.errorCode == 0){
            lat = it.latitude.toString()
            lon = it.longitude.toString()
            Log.e("Test","address ----->"+lat+"--->"+lon)
        }
    }


    private val mSensorListener = object : SensorEventListener{
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            val sdf = getSharedPreferences(SharedPreferencesUtil.SP_COMMON_NAME,Context.MODE_PRIVATE)
            val editor = sdf.edit()
            val todayStep = sdf.getInt("todayStep",0)
            Log.e("Test","todayStep----------------->"+todayStep)
            if(todayStep>0){
                var minStep = sdf.getInt("minStep",0)
                minStep += (event.values[0] - todayStep).toInt()
                if(minStep>0){
                    editor.putInt("minStep",minStep)
                    Log.e("Test","minStep----------------->"+minStep)
                }
            }
            editor.putInt("todayStep",event.values[0].toInt())
            editor.apply()
            onStepChange()
        }
    }


    private fun onStepChange(){
        val step = SharedPreferencesUtil.getCommonInt(this,"step")
        val minStep = SharedPreferencesUtil.getCommonInt(this,"minStep")
        EventBus.getDefault().post(StepRefreshEvent(step+minStep))
    }

    override fun onShake() {
        if(C.USER_ID.isNotEmpty()){
            val sdf = getSharedPreferences(SharedPreferencesUtil.SP_COMMON_NAME,Context.MODE_PRIVATE)
            val editor = sdf.edit()
            var step = sdf.getInt("step",0)
            val minStep = sdf.getInt("minStep",0)
            step++
            editor.putInt("step",step)
            editor.apply()
            Log.e("TAG", "onShake:手机正在被晃动 " )
            Log.e("TAG", "当前的userID---》 "+C.USER_ID )
            EventBus.getDefault().post(StepRefreshEvent(step+minStep))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isStart = false
        mShakeDetector.stop()
        mLocationClient?.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun startStepTask(){
        Thread {
            while (isStart){
                synchronized(this){
                    if(C.USER_ID.isNotEmpty()){
                        val sdf = getSharedPreferences(SharedPreferencesUtil.SP_COMMON_NAME,Context.MODE_PRIVATE)
                        val localDate = sdf.getString("localDate","")
                        val newDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
                        Log.e("TAG", "newDate---->: "+newDate)
                        Log.e("TAG", "Date的值---->: "+Date())
                        if(localDate != newDate){
                            val editor = sdf.edit()
                            editor.putString("localDate",newDate)
                            editor.putInt("step",0)
                            editor.putInt("minStep",0)
                            editor.putInt("todayStep",0)
                            editor.apply()
                        }
                        uploadStepAndLocation()
                    }
                    Thread.sleep(30000)
                }
            }
        }.start()
    }

    private fun uploadStepAndLocation(){
        val step = SharedPreferencesUtil.getCommonInt(this,"step")
        val minStep= SharedPreferencesUtil.getCommonInt(this,"minStep")
//        present.getRiceFromStep(C.USER_ID,"123",lat,lon,minStep.toString(),step.toString())
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
//                GGBContract.GETRICEFROMSTEP -> {
//                    data?.let {
//                        data as StepRiceBean
//                        if(C.USER_ID.isNotEmpty()){
//                            val sdf = getSharedPreferences(SharedPreferencesUtil.SP_COMMON_NAME,Context.MODE_PRIVATE)
//                            val editor = sdf.edit()
//                            var step = sdf.getInt("step",0)
//                            var minStep = sdf.getInt("minStep",0)
//                            if(data.rotateMinStep>step){
//                                step = data.rotateMinStep
//                                editor.putInt("step",data.rotateMinStep)
//                            }
//                            if(data.minStep>minStep){
//                                minStep = data.minStep
//                                editor.putInt("minStep",data.minStep)
//                            }
//                            editor.apply()
//                            EventBus.getDefault().post(StepRefreshEvent(step+minStep))
//                            EventBus.getDefault().post(RiceRefreshEvent(data.minStep,data.walkRiceGrains,data.drivingRiceGrains,data.rotateMinStep,data.targetWalk,data.mileage,data.targetDriving))
//                        }
//                    }
//                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {}

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {}

}
