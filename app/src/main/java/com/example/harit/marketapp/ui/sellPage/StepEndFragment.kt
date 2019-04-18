package com.example.harit.marketapp.ui.sellPage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.harit.marketapp.R
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import com.example.harit.marketapp.ui.adapter.ImageHolizontalAdapter
import com.example.harit.marketapp.ui.model.ChipList
import com.example.harit.marketapp.ui.model.Event.ChipListEvent
import com.example.harit.marketapp.ui.model.Event.ImageListEvent
import com.example.harit.marketapp.ui.model.FeedItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_step_end.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class StepEndFragment : Fragment() , BlockingStep {

    private val filter = hashMapOf<String,String>()
    private var user = hashMapOf<String,Any>()
    private var uriList = mutableListOf<Uri>()


    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onSelected() {

    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {

        var imageList = Array<String>(uriList.size){""}
        /*var i = 0

        while (i < uriList.size){
            val imageName = "${System.currentTimeMillis()}${uriList[i].lastPathSegment}"

            FirebaseStorage.getInstance().reference.child("images/$imageName").putFile(uriList[i])
                    .addOnSuccessListener {
                        if (i == uriList.size - 1) {
                            FirebaseStorage.getInstance()
                                    .reference.child("images/$imageName")
                                    .downloadUrl.addOnCompleteListener {
                                imageList.add(i, it.result.toString())
                                //  .put(i,it.result.toString())
                                val feedItem = FeedItem()
                                feedItem.filter = filter
                                feedItem.name = nameEdt.text.toString()
                                feedItem.description = descriptionEdt.text.toString()
                                feedItem.price = priceEdt.text.toString().toInt()
                                feedItem.status = "instock"
                                feedItem.payment = paymentEdt.text.toString()
                                feedItem.shipping = shippingEdt.text.toString()
                                feedItem.imageUrl = imageList
                                feedItem.user = user

                                FirebaseFirestore.getInstance().collection("Feed")
                                        .document().set(feedItem).addOnCompleteListener {
                                            activity?.onBackPressed()
                                        }
                            }
                        } else {
                            FirebaseStorage.getInstance()
                                    .reference.child("images/$imageName")
                                    .downloadUrl.addOnCompleteListener {
                                //imageList.put(i,it.result.toString())
                                imageList.add(i, it.result.toString())
                                i++
                                //imageList[i] = it.result.toString()
                            }
                        }
                    }
        }*/

        for ((i,uri) in uriList.withIndex()) {

            val imageName = "${System.currentTimeMillis()}${uri.lastPathSegment}"

            FirebaseStorage.getInstance().reference.child("images/$imageName").putFile(uri)
                    .addOnSuccessListener {
                        if(i == uriList.size - 1) {
                            FirebaseStorage.getInstance()
                                    .reference.child("images/$imageName")
                                    .downloadUrl.addOnCompleteListener {
                                imageList[i] = it.result.toString()
                                      //  .put(i,it.result.toString())
                                val feedItem = FeedItem()
                                feedItem.filter = filter
                                feedItem.name = nameEdt.text.toString()
                                feedItem.description = descriptionEdt.text.toString()
                                feedItem.price = priceEdt.text.toString().toInt()
                                feedItem.status = "instock"
                                feedItem.payment = paymentEdt.text.toString()
                                feedItem.shipping = shippingEdt.text.toString()
                                feedItem.imageUrl = imageList.toCollection(ArrayList())
                                feedItem.user = user

                                var docRef = FirebaseFirestore.getInstance().collection("Feed")
                                        .document()
                                feedItem.id = docRef.id

                                docRef.set(feedItem).addOnCompleteListener {
                                            activity?.onBackPressed()
                                        }
                            }
                        }else{
                            FirebaseStorage.getInstance()
                                    .reference.child("images/$imageName")
                                    .downloadUrl.addOnCompleteListener {
                                //imageList.put(i,it.result.toString())
                                imageList[i] = it.result.toString()
                                //imageList[i] = it.result.toString()
                            }
                        }
            }
        }
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

    @Subscribe
    fun onImageListEvent(event: ImageListEvent) {
        uriList = event.imageList!!
        recyclerView.adapter = ImageHolizontalAdapter(0,event.imageList!!)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    @Subscribe
    fun onChipListEvent(event: ChipListEvent) {
        name.text = event.chipList?.name.also {
            filter.put("name",it.toString())
        }
        photosetType.text = event.chipList?.photosetType.also {
            filter.put("photosetType",it.toString())
        }
        set.text = event.chipList?.set.also {
            filter.put("set",it.toString())
        }
        type.text = event.chipList?.type.also {
            filter.put("type",it.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!).get()
                .addOnCompleteListener {
                    user = it.result.data as HashMap<String, Any>
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_step_end,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

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