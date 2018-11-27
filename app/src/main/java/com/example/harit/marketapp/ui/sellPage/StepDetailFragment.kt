package com.example.harit.marketapp.ui.sellPage

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.harit.marketapp.ui.model.ChipList
import com.example.harit.marketapp.ui.model.Event.ChipListEvent
import com.example.harit.marketapp.ui.model.Event.ImageListEvent
import ir.mirrajabi.searchdialog.core.SearchResultListener
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class StepDetailFragment : Fragment() , BlockingStep {

    var photosetType : String? = null
    var set : String? = null
    var type : String? = null
    var imageList : MutableList<Uri>? = null

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
        if(nameChip.text != "Search Member Name" && set != null && type != null && photosetType != null){
            EventBus.getDefault().post(ChipListEvent(ChipList(nameChip.text.toString(), photosetType.toString(), set.toString(), type.toString())))
            imageList?.let {
                EventBus.getDefault().post(ImageListEvent(it))
            }
            callback?.goToNextStep()
        }
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

        photosetGroup.setOnCheckedChangeListener { chipGroup, i ->
            if(i == -1){
                photosetType = null
            }else {
                Log.d("photoset_chip", chipGroup.findViewById<Chip>(i).text.toString())
                photosetType = chipGroup.findViewById<Chip>(i).text.toString()
            }
        }

        setGroup.setOnCheckedChangeListener { chipGroup, i ->
            if(i == -1){
                set = null
            }else {
                Log.d("photoset_chip", chipGroup.findViewById<Chip>(i).text.toString())
                set = chipGroup.findViewById<Chip>(i).text.toString()
            }

        }

        typeGroup.setOnCheckedChangeListener { chipGroup, i ->
            if(i == -1){
                type = null
            }else {
                Log.d("photoset_chip", chipGroup.findViewById<Chip>(i).text.toString())
                type = chipGroup.findViewById<Chip>(i).text.toString()
            }
        }

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


                }catch (e : Exception){

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

    @Subscribe
    fun onImageListEvent(event: ImageListEvent) {
        imageList = event.imageList
    }


    override fun onSelected() {

    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

}