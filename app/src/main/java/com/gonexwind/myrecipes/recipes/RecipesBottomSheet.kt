package com.gonexwind.myrecipes.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gonexwind.myrecipes.databinding.RecipesBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecipesBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}