package com.gonexwind.myrecipes.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.gonexwind.myrecipes.core.util.Constants.DEFAULT_DIET_TYPE
import com.gonexwind.myrecipes.core.util.Constants.DEFAULT_MEAL_TYPE
import com.gonexwind.myrecipes.databinding.RecipesBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: RecipesBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val recipesViewModel: RecipesViewModel by viewModels()
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
        }

        binding.apply {
            mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
                val chip = group.findViewById<Chip>(checkedId)
                val selectedMealType = chip.text.toString().lowercase()
                mealTypeChip = selectedMealType
                mealTypeChipId = checkedId
            }
            dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
                val chip = group.findViewById<Chip>(checkedId)
                val selectedDietType = chip.text.toString().lowercase()
                dietTypeChip = selectedDietType
                dietTypeChipId = checkedId
            }
            applyBtn.setOnClickListener {
                recipesViewModel.saveMealAndDietType(
                    mealTypeChip, mealTypeChipId,
                    dietTypeChip, dietTypeChipId
                )
                findNavController().navigate(
                    RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
                )
            }
        }
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }

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