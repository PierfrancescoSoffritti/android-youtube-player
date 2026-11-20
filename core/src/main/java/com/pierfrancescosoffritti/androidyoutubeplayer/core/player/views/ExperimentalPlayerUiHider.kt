package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.util.Log
import android.webkit.WebView
import androidx.webkit.ScriptHandler
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature

/** Optional presentation only: never exposes a native bridge or changes video/network responses. */
internal object ExperimentalPlayerUiHider {
  private const val TAG = "AYP.ExperimentalUi"

  private val script = """
    (() => {
      const css = '.html5-video-player:not(.ad-showing):not(.ad-interrupting) :is(' +
        '.ytp-chrome-top,.ytp-bezel,.ytp-large-play-button,.ytp-pause-overlay,' +
        '.ytp-chrome-bottom,.ytp-gradient-top,.ytp-gradient-bottom)' +
        ',body:has(.html5-video-player:not(.ad-showing):not(.ad-interrupting)) ' +
        '.ytPlayerControlsContainerHost :is(.ytwPlayerTopControlsHost,.ytwPlayerMiddleControlsHost,' +
        '.ytwPlayerBottomControlsHost,.ytPlayerProgressBarHost,.ytwPlayerFullscreenControlsHost,' +
        '.fullscreen-action-menu,.ytFullscreenVideoRecommendationsHost,.ytmCuedOverlayHost,' +
        '.player-controls-background-container,.player-controls-background,' +
        '.ytmWatchPlayerControlsBackgroundActionItems)' +
        '{display:none!important;pointer-events:none!important}';
      let adopted = false;
      try {
        const sheet = new CSSStyleSheet();
        sheet.replaceSync(css);
        document.adoptedStyleSheets = [...document.adoptedStyleSheets, sheet];
        adopted = true;
      } catch (unavailable) {}
      if (adopted) return;
      const style = document.createElement('style');
      style.id = 'ayp-experimental-player-ui';
      style.textContent = css;
      const observer = new MutationObserver(install);
      let root = null, parent = null;
      function install() {
        const nextRoot = document.documentElement;
        const nextParent = document.head || nextRoot;
        if (style.isConnected && root === nextRoot && parent === nextParent) return;
        observer.disconnect();
        root = nextRoot; parent = nextParent;
        if (parent) parent.appendChild(style);
        observer.observe(document, {childList:true});
        if (root) observer.observe(root, {childList:true});
        if (parent && parent !== root) observer.observe(parent, {childList:true});
      }
      install();
    })();
  """.trimIndent()

  fun install(webView: WebView): ScriptHandler? {
    try {
      if (WebViewFeature.isFeatureSupported(WebViewFeature.DOCUMENT_START_SCRIPT)) {
        return WebViewCompat.addDocumentStartJavaScript(
          webView, script, setOf("https://www.youtube.com", "https://www.youtube-nocookie.com")
        )
      }
    } catch (_: RuntimeException) {
      // Cosmetic styling must not prevent ordinary playback on a broken/unsupported provider.
      Log.w(TAG, "Player UI styling could not be registered")
    }
    return null
  }
}
