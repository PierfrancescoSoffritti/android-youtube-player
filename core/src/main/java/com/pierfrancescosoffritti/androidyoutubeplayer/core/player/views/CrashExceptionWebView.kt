package com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.WebView
import java.util.*

open class CrashExceptionWebView : WebView {
    /***
     * If a webview related crash occurs, the system webview is being updated or does not exist (this problem may exist on some models)
     * @author bill   ls9421@vip.qq.com
     */
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    override fun setOverScrollMode(mode: Int) {
        try {
            super.setOverScrollMode(mode)
        } catch (e: Exception) {
            //If a webview related crash occurs, the system webview is being updated or does not exist (this problem may exist on some models)
            if ((!TextUtils.isEmpty(e.message)) && e.message!!.lowercase(Locale.getDefault())
                    .contains("webview")
            ) {
                e.printStackTrace()
            } else {
                throw e
            }
        }
    }
}