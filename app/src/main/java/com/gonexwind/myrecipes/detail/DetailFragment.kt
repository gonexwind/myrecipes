package com.gonexwind.myrecipes.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.gonexwind.myrecipes.core.adapter.PagerAdapter
import com.gonexwind.myrecipes.core.util.Constants.RECIPE_RESULT_KEY
import com.gonexwind.myrecipes.databinding.FragmentDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titles = arrayListOf("Overview", "Ingredients", "Instructions")
        val fragments = arrayListOf(OverviewFragment(), IngredientFragment(), InstructionFragment())
        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)
        val pagerAdapter =
            PagerAdapter(resultBundle, fragments, requireActivity())

        binding.apply {
            viewPager.adapter = pagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}