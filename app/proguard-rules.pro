# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Aturan dasar ProGuard
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Service

# Jika menggunakan library pihak ketiga, tambahkan aturan yang relevan
-keep class com.squareup.** { *; } # Contoh untuk Retrofit
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.airbnb.android.lottie.** { *; }
#-keep class com.github.bumptech.glide.** { *; }
-keep class okhttp3.logging.** { *; }

# Keep OneSignal classes
-keep class com.onesignal.** { *; }
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.onesignal.** *;
}