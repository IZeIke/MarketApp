package com.example.harit.marketapp.ui.sellPage

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.harit.marketapp.R
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.Step
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_photo_choose.*
import android.provider.MediaStore
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import com.asksira.bsimagepicker.Utils.dp2px
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.robertlevonyan.components.picker.ItemModel
import com.robertlevonyan.components.picker.PickerDialog
import com.example.harit.marketapp.ui.feedPage.MainActivity
import com.robertlevonyan.components.picker.Str
import com.example.harit.marketapp.ui.model.SampleSearchModel




class StepPhotoFragment : Fragment() , BlockingStep , BSImagePicker.OnMultiImageSelectedListener {

    companion object {
        fun newInstance() : StepPhotoFragment {
            return StepPhotoFragment()
        }
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {

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
        return LayoutInflater.from(context).inflate(R.layout.fragment_photo_choose,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickerDialog = PickerDialog.Builder(this)// Activity or Fragment
                .setListType(PickerDialog.TYPE_GRID)       // Type of the picker, must be PickerDialog.TYPE_LIST or PickerDialog.TYPE_Grid
                .setItems(arrayListOf(ItemModel(ItemModel.ITEM_CAMERA,"Take Photo",0,true,0,0)
                        ,ItemModel(ItemModel.ITEM_GALLERY,"Open Gallery",0,true,0,0)) )         // List of ItemModel-s which should be in picker
                .setDialogStyle(PickerDialog.DIALOG_MATERIAL)    // PickerDialog.DIALOG_STANDARD (square corners) or PickerDialog.DIALOG_MATERIAL (rounded corners)
                .create()

        val multiSelectionPicker = BSImagePicker.Builder("com.example.harit.marketapp.provider")
                .isMultiSelect //Set this if you want to use multi selection mode.
                .setMinimumMultiSelectCount(1)
                .setMaximumMultiSelectCount(9) //Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                .setMultiSelectDoneTextColor(R.color.colorAccent) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                .disableOverSelectionMessage() //You can also decide not to show this over select message.
                .build()

        pickerDialog.setPickerCloseListener { type, _ ->
            when (type) {
                ItemModel.ITEM_GALLERY -> {
                    multiSelectionPicker.show(childFragmentManager, "picker")
                }
            }
        }

        imageAddButton.setOnClickListener {
            //pickerDialog.show()
            val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery")
            val builder = this.activity?.let { it1 -> AlertDialog.Builder(it1) }
            builder?.let {
                it.setTitle("Add Photo")
                it.setItems(options) { dialog, which ->
                    if(options[which] == "Take Photo"){
                        openCamera()
                    }else{
                        multiSelectionPicker.show(childFragmentManager, "picker")
                    }
                }
            }?.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onMultiImageSelected(uriList: MutableList<Uri>?) {
        uriList?.let {
            var count = 1
            for (image in it){
                when (count){
                    1 -> image1.setImageURI(image)
                    2 -> image2.setImageURI(image)
                    3 -> image3.setImageURI(image)
                    4 -> image4.setImageURI(image)
                    5 -> image5.setImageURI(image)
                    6 -> image6.setImageURI(image)
                    7 -> image7.setImageURI(image)
                    8 -> image8.setImageURI(image)
                    9 -> image9.setImageURI(image)
                }
                count++
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }


    override fun onSelected() {

    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {

    }

    private fun openCamera() {
        val fileName = (System.currentTimeMillis() / 1000).toString() + ".jpg"

        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, fileName)
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "marketApp")
        val uri = context!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        startActivityForResult(takePhoto, 1)

    }
}