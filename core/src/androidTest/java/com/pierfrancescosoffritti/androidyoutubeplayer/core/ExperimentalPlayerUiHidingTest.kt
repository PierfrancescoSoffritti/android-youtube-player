package com.pierfrancescosoffritti.androidyoutubeplayer.core

import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import androidx.webkit.ScriptHandler
import androidx.webkit.WebViewFeature
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.ExperimentalPlayerUiHider
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayInputStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/** No YouTube requests: the WebView renders a small intercepted iframe fixture. */
@RunWith(AndroidJUnit4::class)
class ExperimentalPlayerUiHidingTest {
  private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
  private val context get() = instrumentation.targetContext

  @Test fun defaultAndControlsZeroDoNotEnableExperimentalHiding() {
    assertFalse(IFramePlayerOptions.getDefault(context).isExperimentalPlayerUiHidingEnabled())
    assertFalse(IFramePlayerOptions.Builder(context).controls(0).build().isExperimentalPlayerUiHidingEnabled())
  }

  @Test fun optInIsClientSideAndDoesNotAddAYouTubeParameter() {
    val ordinary = IFramePlayerOptions.Builder(context).controls(0).build()
    val experimental = IFramePlayerOptions.Builder(context).controls(0)
      .experimentalPlayerUiHiding(true).build()
    assertTrue(experimental.isExperimentalPlayerUiHidingEnabled())
    assertEquals(ordinary.toString(), experimental.toString())
    assertEquals(0, JSONObject(experimental.toString()).getInt("controls"))
  }

  @Test fun builderOrderAndDisablingArePredictable() {
    val flagFirst = IFramePlayerOptions.Builder(context).experimentalPlayerUiHiding(true).controls(0).build()
    val controlsFirst = IFramePlayerOptions.Builder(context).controls(0).experimentalPlayerUiHiding(true).build()
    assertTrue(flagFirst.isExperimentalPlayerUiHidingEnabled())
    assertTrue(controlsFirst.isExperimentalPlayerUiHidingEnabled())
    assertFalse(IFramePlayerOptions.Builder(context).experimentalPlayerUiHiding(true)
      .controls(1).build().isExperimentalPlayerUiHidingEnabled())
    assertFalse(IFramePlayerOptions.Builder(context).controls(0).experimentalPlayerUiHiding(true)
      .experimentalPlayerUiHiding(false).build().isExperimentalPlayerUiHidingEnabled())
  }

  @Test fun reusingBuilderDoesNotAlterPreviouslyBuiltOptions() {
    val builder = IFramePlayerOptions.Builder(context).controls(0).experimentalPlayerUiHiding(true)
    val first = builder.build()
    val firstJson = first.toString()
    val second = builder.controls(1).experimentalPlayerUiHiding(false).build()
    assertTrue(first.isExperimentalPlayerUiHidingEnabled())
    assertEquals(firstJson, first.toString())
    assertFalse(second.isExperimentalPlayerUiHidingEnabled())
    assertEquals(1, JSONObject(second.toString()).getInt("controls"))
  }

  @Test fun hidesClassicAndMobileSiblingControlsInBothYouTubeOrigins() {
    supportedWebView()
    for (origin in listOf("https://www.youtube.com", "https://www.youtube-nocookie.com")) {
      val result = render(origin, inspectScript)
      for (id in controls) assertEquals("$origin: $id", "none", result.getString(id))
      assertContentPreserved(result)
    }
  }

  @Test fun adStateRetainsControlsAndReturningToContentHidesThemAgain() {
    supportedWebView()
    val script = """
      const player = document.getElementById('player');
      const result = {};
      for (const state of ['ad-showing','ad-interrupting','']) {
        player.className = 'html5-video-player ' + state;
        result[state || 'content'] = inspect();
      }
      return result;
    """.trimIndent()
    val result = render("https://www.youtube.com", script)
    for (state in listOf("ad-showing", "ad-interrupting")) {
      val ad = result.getJSONObject(state)
      for (id in controls) assertNotEquals("$state: $id", "none", ad.getString(id))
      assertContentPreserved(ad)
    }
    for (id in controls) assertEquals("none", result.getJSONObject("content").getString(id))
  }

  @Test fun unrelatedOriginsAreNotModified() {
    supportedWebView()
    val result = render("https://example.test", inspectScript)
    for (id in controls) assertNotEquals(id, "none", result.getString(id))
    assertContentPreserved(result)
  }

  @Test fun removedHandlerDoesNotInjectOnTheNextNavigation() {
    supportedWebView()
    val result = render("https://www.youtube.com", inspectScript, removeBeforeLoad = true)
    for (id in controls) assertNotEquals(id, "none", result.getString(id))
    assertContentPreserved(result)
  }

  private fun supportedWebView() {
    assumeTrue(WebViewFeature.isFeatureSupported(WebViewFeature.DOCUMENT_START_SCRIPT))
  }

  private fun assertContentPreserved(result: JSONObject) {
    for (id in listOf("video", "captions", "error")) assertNotEquals(id, "none", result.getString(id))
    assertTrue(result.getBoolean("videoPresent"))
  }

  private fun render(origin: String, expression: String, removeBeforeLoad: Boolean = false): JSONObject {
    val received = CountDownLatch(1)
    val value = AtomicReference<String>()
    var web: WebView? = null
    var handler: ScriptHandler? = null
    val fixture = fixture(expression)
    try {
      instrumentation.runOnMainSync {
        web = WebView(context).apply {
          settings.javaScriptEnabled = true
          addJavascriptInterface(object {
            @JavascriptInterface fun report(json: String) {
              if (value.compareAndSet(null, json)) received.countDown()
            }
          }, "FixtureAssertions")
          webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse {
              // The iframe keeps the chosen origin but never contacts it.
              val html = if (request.url.toString() == "$origin/embed/ui-fixture") fixture else ""
              return WebResourceResponse("text/html", "UTF-8", ByteArrayInputStream(html.toByteArray(Charsets.UTF_8)))
            }
          }
          handler = ExperimentalPlayerUiHider.install(this)
          if (removeBeforeLoad) {
            handler?.remove()
            handler = null
          }
          val parent = "<html><body><iframe src=\"$origin/embed/ui-fixture\"></iframe></body></html>"
          loadDataWithBaseURL("https://app.example/", parent, "text/html", "UTF-8", null)
        }
      }
      assertTrue("The intercepted iframe did not report its computed styles", received.await(15, TimeUnit.SECONDS))
      val result = JSONObject(value.get())
      assertFalse("Fixture script failed: $result", result.has("failure"))
      return result
    } finally {
      instrumentation.runOnMainSync {
        handler?.remove()
        web?.removeJavascriptInterface("FixtureAssertions")
        web?.destroy()
      }
    }
  }

  private fun fixture(expression: String): String = """
    <!doctype html><html><head><meta name="viewport" content="width=device-width"></head><body>
    <div id="player" class="html5-video-player">
      <video id="video"></video>
      <div id="classic-header" class="ytp-chrome-top">Title</div>
      <div id="classic-pause" class="ytp-bezel">Pause</div>
      <div id="classic-progress" class="ytp-chrome-bottom">Progress</div>
      <div id="captions" class="ytp-caption-window-container">Captions</div>
      <div id="error" class="ytp-error">An error</div>
    </div>
    <div class="ytPlayerControlsContainerHost">
      <div id="mobile-header" class="ytwPlayerTopControlsHost">Mobile title</div>
      <div id="mobile-pause" class="ytwPlayerMiddleControlsHost">Mobile pause</div>
      <div id="mobile-progress" class="ytPlayerProgressBarHost">Mobile progress</div>
      <div id="mobile-gradient" class="player-controls-background-container">Gradient</div>
      <div id="mobile-action-gradient" class="ytmWatchPlayerControlsBackgroundActionItems">Action gradient</div>
    </div>
    <script>
    function inspect() {
      const result = {};
      for (const id of $inspectedIdsJson) result[id] = getComputedStyle(document.getElementById(id)).display;
      result.videoPresent = !!document.querySelector('video#video');
      return result;
    }
    window.addEventListener('load', () => {
      try { FixtureAssertions.report(JSON.stringify((() => { $expression })())); }
      catch (error) { FixtureAssertions.report(JSON.stringify({failure:String(error)})); }
    });
    </script></body></html>
  """.trimIndent()

  companion object {
    private val controls = listOf("classic-header", "classic-pause", "classic-progress", "mobile-header",
      "mobile-pause", "mobile-progress", "mobile-gradient", "mobile-action-gradient")
    private val inspected = controls + listOf("video", "captions", "error")
    private val inspectedIdsJson = inspected.joinToString(prefix = "[", postfix = "]") { "'$it'" }
    private const val inspectScript = "return inspect();"
  }
}
