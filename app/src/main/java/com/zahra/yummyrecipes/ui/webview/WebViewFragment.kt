package com.zahra.yummyrecipes.ui.webview

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zahra.yummyrecipes.R
import com.zahra.yummyrecipes.databinding.FragmentWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    //Binding
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    //Other
    private val args: WebViewFragmentArgs by navArgs()
    private lateinit var link: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Args
            args.let {
                link = it.url
            }
            //Back
            baclImg.setOnClickListener {
                findNavController().popBackStack()
            }
            //Web view
            link.let { link ->
                webView.apply {
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.builtInZoomControls = false
                    settings.domStorageEnabled = true
                    webView.webViewClient = WebViewClient()
                    webView.isVerticalScrollBarEnabled = true
                    webView.isHorizontalScrollBarEnabled = true
                    webView.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            if (newProgress < 100 && webViewLoading.visibility == View.GONE) {
                                webViewLoading.isVisible = true
                            }
                            webViewLoading.progress = newProgress
                            if (newProgress == 100) {
                                webViewLoading.isVisible = false
                            }
                        }
                    }
                    webView.loadUrl(link)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

