package com.keerthi.securityevaluation

import android.content.Context
import android.os.Bundle
import android.content.Context.FINGERPRINT_SERVICE
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure
import android.content.ContentResolver
import android.app.KeyguardManager
import android.widget.Toast
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var finalScore:Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val one:TextView = findViewById(R.id.one)
        val two:TextView = findViewById(R.id.two)
        val three:TextView = findViewById(R.id.three)
        val four:TextView = findViewById(R.id.four)
        val five:TextView = findViewById(R.id.five)
        val six:TextView = findViewById(R.id.six)
        val finScore:TextView = findViewById(R.id.finscore)



        if(checkDevPerm()!=0){
            one.setText("1.Developer Permissions Enabled, No Changes to score")
            Toast.makeText(applicationContext,"Developer Permissions Enabled, No Changes to score",Toast.LENGTH_LONG).show()
        }else{
            finalScore= finalScore+1.0f
            one.setText("1.Developer Permissions Disabled, Adding 1 to score.")
            Toast.makeText(applicationContext,"Developer Permissions Disabled, Adding 1 to score.",Toast.LENGTH_LONG).show()
        }

        finalScore+=checkOS(two)
        finalScore+=checkLock()
        when(checkLock()){
            1.0f-> {
                three.setText("3.Device has FingerPrint & Enabled. Adding 1 to Score.")
                Toast.makeText(applicationContext,"Device has FingerPrint & Enabled. Adding 1 to Score.",Toast.LENGTH_LONG).show()
            }
            0.0f->{
                three.setText("3.Device doesn't have any authentication, Adding 0 to score")
                Toast.makeText(applicationContext,"Device doesn't have any authentication, Adding 0 to score",Toast.LENGTH_LONG).show()
            }
            0.8f->{
                three.setText("3.Device has Pin or Pattern set, Adding 0.8 to score.")
                Toast.makeText(applicationContext,"Device has Pin or Pattern set, Adding 0.8 to score.",Toast.LENGTH_LONG).show()
            }
        }

//        SafetyNet.getClient(this)
//            .isVerifyAppsEnabled
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    if (task.result.isVerifyAppsEnabled) {
//                        Log.d("MY_APP_TAG", "The Verify Apps feature is enabled.")
//                    } else {
//                        Log.d("MY_APP_TAG", "The Verify Apps feature is disabled.")
//                    }
//                } else {
//                    Log.e("MY_APP_TAG", "A general error occurred.")
//                }
//            }
//
//        SafetyNet.getClient(application)
//            .isVerifyAppsEnabled
//            .addOnCompleteListener { task ->
//                if (task.result.isVerifyAppsEnabled) {
//                    four.setText("4.Play Protect Feature Enabled, Adding 1 to score")
//                    Toast.makeText(applicationContext,"Play Protect Feature Enabled, Adding 1 to score",Toast.LENGTH_LONG).show()
//                    finalScore +=1.0f
//                    finalScore+=checktotalApps(five)
//                    finalScore+=isSimSupport(six)
//                    finScore.setText(finalScore.toString()+"/6")
//                } else {
//                    four.setText("4.Play Protect Disbaled, No Changes to score")
//                    Toast.makeText(applicationContext,"Play Protect Disbaled, No Changes to score",Toast.LENGTH_LONG).show()
//                    finalScore+=checktotalApps(five)
//                    finalScore+=isSimSupport(six)
//                    finScore.setText(finalScore.toString()+"/6")
//
//                }
//            }








    }

    fun checktotalApps(textView: TextView):Float{

        // Indicates the amount of data shared with other apps. So if more apps are installed then more data is exposed. Thus increasing security threat

        var numberOfInstalledApps = getPackageManager().getInstalledApplications(0).size
        if(numberOfInstalledApps>200){
            val a ="5.This device has "+numberOfInstalledApps.toString()+" applications installed. No Changes to score."
            textView.setText(a)
            Toast.makeText(applicationContext,a,Toast.LENGTH_LONG).show()
            return 0.0f
        }else if(numberOfInstalledApps>100 && numberOfInstalledApps<=200){
            val a  = "5.This device has "+numberOfInstalledApps.toString()+" applications installed. Adding 0.5 to score"
            textView.setText(a)
            Toast.makeText(applicationContext,"This device has "+numberOfInstalledApps.toString()+" applications installed. Adding 0.5 to score",Toast.LENGTH_LONG).show()
            return 0.5f
        }else{
            val a  = "5.This device has "+numberOfInstalledApps.toString()+"  applications installed. Adding 1 to score."
            textView.setText(a)
            Toast.makeText(applicationContext,"This device has "+numberOfInstalledApps.toString()+" applications installed. Adding 1 to score.",Toast.LENGTH_LONG).show()
            return 1.0f
        }
    }


    fun checkDevPerm():Int{

        val adb = Secure.getInt(
            this.contentResolver,
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
        )
        return adb
    }

    fun checkOS(textView: TextView):Float{
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            textView.setText("2.Android Pi Running, Adding 1 to score")
            Toast.makeText(applicationContext,"Android Pi Running, Adding 1 to score",Toast.LENGTH_LONG).show()
            return 1.0f
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            textView.setText("2.Android Oreo Running, Adding 0.8 to score")
            Toast.makeText(applicationContext,"Android Oreo Running, Adding 0.8 to score",Toast.LENGTH_LONG).show()
            return 0.8f
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            textView.setText("2.Android Nought Running, Adding 0.6 to score")
            Toast.makeText(applicationContext,"Android Nought Running, Adding 0.6 to score",Toast.LENGTH_LONG).show()
            return 0.6f
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            textView.setText("2.Android Marshmallow Running, Adding 0.4 to score")
            Toast.makeText(applicationContext,"Android Marshmallow Running, Adding 0.4 to score",Toast.LENGTH_LONG).show()
            return 0.4f
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            textView.setText("2.Android Lollipop Running, Adding 0.2 to score")
            Toast.makeText(applicationContext,"Android Lollipop Running, Adding 0.2 to score",Toast.LENGTH_LONG).show()
            return 0.2f
        }else{
            textView.setText("2.Android not running recent OS, No Changes to score")
            Toast.makeText(applicationContext,"Android not running recent OS, No Changes to score",Toast.LENGTH_LONG).show()
            return 0.0f
        }
    }

    fun checkLock():Float{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val fingerprintManager = applicationContext.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            if (!fingerprintManager.isHardwareDetected) {
                var score:Float
                if(passpin() || pattern()){
                    score = 0.8f
                }else{
                    score = 0.0f
                }
                return score
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                return 0.0f
            } else {
                return 1.0f
            }
        }
        return 0.0f
    }

    fun passpin(): Boolean {
        val keyguardManager = applicationContext.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager //api 16+
        return keyguardManager.isKeyguardSecure
    }

    fun pattern(): Boolean {
        try {
            val lockPatternEnable = Secure.getInt(applicationContext.contentResolver, Secure.LOCK_PATTERN_ENABLED)
            return lockPatternEnable == 1
        } catch (e: Settings.SettingNotFoundException) {
            return false
        }
    }


    fun isSimSupport(textView: TextView): Float {
        val telephony = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager  //gets the current TelephonyManager
        if(telephony.simState == TelephonyManager.SIM_STATE_ABSENT){
            textView.setText("6.The Phone Doesn't have Sim Card. No Changes to score.")
            Toast.makeText(applicationContext,"The Phone Doesn't have Sim Card. No Changes to score.",Toast.LENGTH_LONG).show()
            return 0.0f
        }else{
            textView.setText("6.The Phone has Sim Card. Adding 1 to score.")
            Toast.makeText(applicationContext,"The Phone has Sim Card. Adding 1 to score.",Toast.LENGTH_LONG).show()
            return 1.0f
        }
    }
}
