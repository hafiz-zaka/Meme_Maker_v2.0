/*
Developer Name : Rafaqat Mehmood
Designation   : Sr. Anroid Developer
Whatsapp No  : 0310-1025532
 */

object Version {
    const val appCompact ="1.6.1"
    const val material ="1.8.0"
    const val constraintLayout ="2.1.4"
    const val firebaseDatabase ="20.2.2"
    const val firebaseConfig ="21.3.0"
    const val firebaseAuth ="21.3.0"
    const val firebaseCrashlytics ="18.4.3"
    const val firebaseAnalytics ="21.3.0"
    const val firebaseStorage="20.2.1"
    const val junit ="4.13.2"
    const val test_ext_junit ="1.1.5"
    const val espressoCore ="3.5.1"
    const val sdp ="1.1.0"
    const val ssp ="1.1.0"
    const val glide ="4.13.2"
    const val glideCompiler ="4.13.2"
    const val play_services_ads ="22.4.0"
    const val lifecycleExtensions ="2.2.0"
    const val imagePicker ="2.3.22"
    const val spotsLoadingDialog ="0.7@aar"
    const val colorPicker ="2.0.1"
    const val lottie ="5.2.0"
    const val updateDialog ="1.10.3"
}

object MainDep{
    const val appCompact="androidx.appcompat:appcompat:${Version.appCompact}"
    const val material="com.google.android.material:material:${Version.material}"
    const val constraintLayout="androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
}

object Firebase{
    const val firebaseDatabase="com.google.firebase:firebase-database:${Version.firebaseDatabase}"
    const val firebaseConfig="com.google.firebase:firebase-config:${Version.firebaseConfig}"
//    const val firebaseAuth="com.google.firebase:firebase-auth:${Version.firebaseAuth}"
    const val firebaseCrashlytics="com.google.firebase:firebase-crashlytics:${Version.firebaseCrashlytics}"
    const val firebaseAnalytics="com.google.firebase:firebase-analytics:${Version.firebaseAnalytics}"
    const val firebaseStorage="com.google.firebase:firebase-storage:${Version.firebaseStorage}"

}
object Glide{
    const val glideMain="com.github.bumptech.glide:glide:${Version.glide}"
    const val glideCompiler="com.github.bumptech.glide:compiler:${Version.glideCompiler}"

}
object ResponsiveUI{
    const val sdp="com.intuit.sdp:sdp-android:${Version.sdp}"
    const val ssp="com.intuit.ssp:ssp-android:${Version.ssp}"
}

object TestImplementation{
    const val junit="junit:junit:${Version.junit}"
}
object AndroidTestImplement{
    const val test_ext_junit="androidx.test.ext:junit:${Version.test_ext_junit}"
    const val espressoCore="androidx.test.espresso:espresso-core:${Version.espressoCore}"
}

object AdmobAd{
    const val play_services_ads="com.google.android.gms:play-services-ads:${Version.play_services_ads}"
}

object ImagePicker{
    const val imagePicker="com.github.Drjacky:ImagePicker:${Version.imagePicker}"
}
object ColorPicker{
    const val colorPicker="com.github.yukuku:ambilwarna:${Version.colorPicker}"
}
object LottieAnimation{
    const val lottieAnimation="com.airbnb.android:lottie:${Version.lottie}"
}
object SpotLoadingDialog{
    const val loadingDialog="com.github.d-max:spots-dialog:${Version.spotsLoadingDialog}"
}
//object LiveCycleExtension{
//    const val lifeCycleExtension="androidx.lifecycle:lifecycle-extensions:${Version.lifecycleExtensions}"
//}
object UpdateDialog{
    const val updateDialog="com.google.android.play:core:${Version.updateDialog}"
}