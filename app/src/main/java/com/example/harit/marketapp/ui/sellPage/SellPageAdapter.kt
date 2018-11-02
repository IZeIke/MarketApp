package com.example.harit.marketapp.ui.sellPage

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.example.harit.marketapp.R
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel

class SellPageAdapter(fm: FragmentManager, context: Context) : AbstractFragmentStepAdapter(fm, context) {

    override fun getCount(): Int {
        return 3
    }

    override fun createStep(position: Int): Step {
        return when (position) {
            0 -> StepPhotoFragment.newInstance()
            1 -> StepDetailFragment.newInstance()
            2 -> StepEndFragment.newInstance()
            else -> throw IllegalArgumentException("Unsupported position: $position")
        }
    }

    override fun getViewModel(position: Int): StepViewModel {
        return super.getViewModel(position)
    }
}