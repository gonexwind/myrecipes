package com.gonexwind.myrecipes.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.gonexwind.myrecipes.core.model.Result
import com.gonexwind.myrecipes.core.util.Constants
import com.gonexwind.myrecipes.databinding.FragmentInstructionBinding

class InstructionFragment : Fragment() {

    private var _binding : FragmentInstructionBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val result = args!!.getParcelable<Result>(Constants.RECIPE_RESULT_KEY) as Result
        binding.instructionsWebView.apply {
            webViewClient = object  : WebViewClient() {}
            loadUrl(result.sourceUrl)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstructionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}