package com.muve.muve_it_driver

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.muve.muve_it_driver.util.NetworkUtility.Companion.TERMS_CONDITIONS_HAULERS_URL

class WebViewScreenTermsCondition : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_screen_terms_condition)

        val i = intent
        val url = TERMS_CONDITIONS_HAULERS_URL
        val webView = findViewById<WebView>(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        assert(url != null)
        webView.loadUrl(url!!)
    }
}