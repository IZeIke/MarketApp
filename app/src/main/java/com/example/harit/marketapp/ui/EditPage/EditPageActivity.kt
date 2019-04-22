package com.example.harit.marketapp.ui.EditPage

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.adapter.ImageHolizontalAdapter
import com.example.harit.marketapp.ui.model.FeedModel
import com.google.firebase.firestore.FirebaseFirestore
import com.robertlevonyan.components.picker.set
import kotlinx.android.synthetic.main.fragment_step_end.*

class EditPageActivity : AppCompatActivity() {

    lateinit var item : FeedModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_step_end)

        item = intent.extras.getParcelable<FeedModel>("model")

        initInstance()
    }

    private fun initInstance() {

        setTopBar()

        nameEdt.setText(item.name!!)
        descriptionEdt.setText(item.description!!)
        priceEdt.setText(item.price.toString())
        shippingEdt.setText(item.shipping!!)
        paymentEdt.setText(item.payment!!)
        summitButton.setOnClickListener {
            FirebaseFirestore.getInstance().collection("Feed").document(item.id!!)
                    .update("name",nameEdt.text.toString(),
                            "description",descriptionEdt.text.toString(),
                            "price",priceEdt.text.toString().toInt(),
                            "shipping",shippingEdt.text.toString(),
                            "payment", paymentEdt.text.toString()
                    ).addOnSuccessListener {
                        onBackPressed()
                    }
        }

        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = ImageHolizontalAdapter(0,null,item.imageUrl)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun setTopBar() {
        editBtnHolder.visibility = View.VISIBLE
        top.visibility = View.GONE
        topBarHolder.visibility = View.VISIBLE
        topBar.setText("Edit")
        topBar.haveBack(true)
        topBar.haveChat(false)
        topBar.haveNoti(false)
        topBar.haveSearch(false)
        topBar.getBackHolder()?.setOnClickListener {
            onBackPressed()
        }
    }


}