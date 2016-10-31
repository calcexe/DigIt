# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Mateusz\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes EnclosingMethod
-keepattributes InnerClasses
-dontoptimize

#111 warnings
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**

#72 warnings
-keep class com.squareup.picasso.** { *; }
-dontwarn java.nio.file.**

#62 warnings
-keep class squareup.picasso.OkHttpDownloader.** { *; }
-dontwarn com.squareup.okhttp.**

#31 warnings
-keep class rx.internal.util.unsafe.** { *; }
-dontwarn sun.misc.**

#19 warnings
-keep class retrofit2.Platform$Java8.** { *; }
-dontwarn java.lang.invoke.**

#5 warnings
-keep class okio.DeflaterSink.** { *; }
-keep class okio.Okio.** { *; }
-keep class retrofit2.Platform$Java8.** { *; }
-dontwarn org.codehaus.mojo.animal_sniffer.**

-dontwarn java.beans.ConstructorProperties


