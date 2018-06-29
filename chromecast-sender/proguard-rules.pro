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

-keep public class com.pierfrancescosoffritti.androidyoutubeplayer.** {
   public *;
}
-keepnames class com.pierfrancescosoffritti.androidyoutubeplayer.*

-keep public class kotlin.jvm.internal.Intrinsics
-keep public class kotlin.Pair
-keep public class kotlin.TuplesKt
-keep public class org.jetbrains.annotations.NotNull
-keep public class kotlin.Metadata
-keep public class kotlin.text.StringsKt
-keep public class kotlin.collections.CollectionsKt
-keep public class kotlin.TypeCastException
-keep public class kotlin.jvm.functions.Function0
-keep public class kotlin.Unit
-keep public class kotlin.jvm.JvmStatic