# WebView requires @JavascriptInterface annotations at runtime.
-keepattributes RuntimeVisibleAnnotations

# JavaScript calls these methods by name.
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
