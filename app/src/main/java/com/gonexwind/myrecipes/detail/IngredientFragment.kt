package com.gonexwind.myrecipes.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gonexwind.myrecipes.core.adapter.IngredientAdapter
import com.gonexwind.myrecipes.core.model.Result
import com.gonexwind.myrecipes.core.util.Constants.RECIPE_RESULT_KEY
import com.gonexwind.myrecipes.databinding.FragmentIngredientBinding

class IngredientFragment : Fragment() {

    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        val result = args!!.getParcelable<Result>(RECIPE_RESULT_KEY) as Result

        val adapter = IngredientAdapter()
        result.extendedIngredients.let { adapter.setData(it) }
        binding.ingredientsRecyclerview.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}