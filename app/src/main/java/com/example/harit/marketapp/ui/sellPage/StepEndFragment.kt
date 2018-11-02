package com.example.harit.marketapp.ui.sellPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.harit.marketapp.R
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

class StepEndFragment : Fragment() , BlockingStep {

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onSelected() {

    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {

    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {

    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {

    }

    companion object {
        fun newInstance() : StepEndFragment{
            return StepEndFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_step_end,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}