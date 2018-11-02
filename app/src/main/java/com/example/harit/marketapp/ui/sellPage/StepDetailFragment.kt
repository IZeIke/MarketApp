package com.example.harit.marketapp.ui.sellPage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.model.FilterList
import com.example.harit.marketapp.ui.model.SampleSearchModel
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_step_detail.*
import android.widget.Toast
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat



class StepDetailFragment : Fragment() , BlockingStep {

    companion object {
        fun newInstance() : StepDetailFragment {
            return StepDetailFragment()
        }
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {

    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        callback?.goToNextStep()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_step_detail,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var list : FilterList? = null
        FirebaseFirestore.getInstance().collection("Filter").get().addOnCompleteListener {
            if(it.isSuccessful){
                for (document in it.result.documents){
                    //Log.d("doc id", document.id)
                    when (document.id) {
                        "filterList" -> list = document.toObject(FilterList::class.java)
                    }
                }

                try {
                    list?.set?.let {
                        for ((key, value) in it) {
                            var chip = createChip(photosetGroup.context)
                            chip.text = key
                            photosetGroup.addView(chip)
                            if(setGroup.childCount < value) {
                                for (i in (setGroup.childCount + 1)..value) {
                                    var chip = createChip(setGroup.context)
                                    chip.text = "set " + i.toString()
                                    setGroup.addView(chip)
                                }
                            }
                        }
                    }

                    list?.type?.let {
                        for(type in it){
                            var chip = createChip(typeGroup.context)
                            chip.text = type
                            typeGroup.addView(chip)
                        }
                    }


                }catch (e : Exception){

                }

                nameChip.checkedIcon = null
                nameChip.chipBackgroundColor = resources.getColorStateList(R.color.bg_chip_state_list)
                nameChip.setOnClickListener {
                    SimpleSearchDialogCompat(activity, "Search...",
                            "เลือก member", null, createSampleData(list?.nameList),
                            SearchResultListener { dialog, item, _ ->
                                /*Toast.makeText(activity, item.title,
                                        Toast.LENGTH_SHORT).show()*/
                                nameChip.isCheckable = true
                                nameChip.isChecked = true
                                nameChip.text = item.title

                                dialog.dismiss().also {
                                    nameChip.isCheckable = false
                                }
                            }).show()
                }
            }
        }

    }

    private fun createChip(context: Context): Chip{
        var chip = Chip(context)
        chip.isCheckable = true
        chip.checkedIcon = null
        chip.chipBackgroundColor = resources.getColorStateList(R.color.bg_chip_state_list)
        return chip
    }

    private fun createSampleData(nameList: Map<String, Int>?): ArrayList<SampleSearchModel> {
        val items = arrayListOf<SampleSearchModel>()
        nameList?.let {
            for ((key, _) in it)
                items.add(SampleSearchModel(key))
        }
        return items
    }


    override fun onSelected() {

    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {

    }


}