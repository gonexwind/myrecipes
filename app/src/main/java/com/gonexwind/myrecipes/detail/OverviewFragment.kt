package com.gonexwind.myrecipes.detail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.gonexwind.myrecipes.R
import com.gonexwind.myrecipes.core.util.Constants.RECIPE_RESULT_KEY
import com.gonexwind.myrecipes.databinding.FragmentOverviewBinding
import com.gonexwind.myrecipes.core.model.Result

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val result: Result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result

        binding.apply {
            mainImageView.load(result.image)
            titleTextView.text = result.title
            likesTextView.text = result.aggregateLikes.toString()
            timeTextView.text = result.readyInMinutes.toString()
            summaryTextView.text = Html.fromHtml(result.summary)
            updateColors(result.vegetarian, vegetarianTextView, vegetarianImageView)
            updateColors(result.vegan, veganTextView, veganImageView)
            updateColors(result.cheap, cheapTextView, cheapImageView)
            updateColors(result.dairyFree, dairyFreeTextView, dairyFreeImageView)
            updateColors(result.glutenFree, glutenFreeTextView, glutenFreeImageView)
            updateColors(result.veryHealthy, healthyTextView, healthyImageView)
        }
    }

    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}