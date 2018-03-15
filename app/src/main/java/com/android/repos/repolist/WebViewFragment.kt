package com.android.repos.repolist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

import com.android.repos.R
import timber.log.Timber
import android.webkit.WebChromeClient
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.fragment_webview.view.*


class WebViewFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var progress: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val url = arguments?.getString(KEY_SELECTED_URL)
        val name = arguments?.getString(KEY_SELECTED_TITLE)
        activity?.let {
            (it as AppCompatActivity).supportActionBar?.apply {
                setDisplayShowHomeEnabled(true)
                setDisplayHomeAsUpEnabled(true)
                name?.let {
                    this.title = name
                }
                show()
            }
        }


        val v = inflater.inflate(R.layout.fragment_webview, container, false)
        Timber.d("onlcick Url %s", url)
        webView = v.web_view
        progress = v.progress_bar
        webView.loadUrl(url)

        // Enable Javascript
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Force links and redirects to open in the WebView instead of in a browser
        webView.webChromeClient = Client()

        return v
    }

    private inner class Client : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (newProgress >= 90) {
                progress.visibility = View.GONE
            }
            progress.setProgress(newProgress)
            super.onProgressChanged(view, newProgress)
        }
    }

    companion object {

        private const val KEY_SELECTED_URL = "selected_url"
        private const val KEY_SELECTED_TITLE = "selected_title"

        fun newInstance(url: String, title: String): WebViewFragment? {
            val args = Bundle()
            args.putString(KEY_SELECTED_URL, url)
            args.putString(KEY_SELECTED_TITLE, title)
            val fragment = WebViewFragment()
            fragment.arguments = args
            return fragment
        }
    }
}