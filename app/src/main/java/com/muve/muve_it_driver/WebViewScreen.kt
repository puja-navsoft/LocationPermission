package com.muve.muve_it_driver

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.muve.muve_it_driver.util.NetworkUtility.Companion.HOW_IT_WORKS


class WebViewScreen : AppCompatActivity() {

    // var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view_screen)

        val webView = findViewById<WebView>(R.id.webview)

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        if (intent.hasExtra("driverhelp")) {
            if (intent.hasExtra("driverhelp").equals("driverhelp")) {

                val url = HOW_IT_WORKS
                assert(url != null)
                webView.loadUrl(url!!)
            }

        } else if (intent.hasExtra("drivertraining")) {

            if (intent.hasExtra("drivertraining").equals("drivertraining")) {

                val url = "https://5h1k6pjt0mo.typeform.com/to/TWy7jsGR"
                assert(url != null)
                webView.loadUrl(url!!)
            }

        }

    }
}