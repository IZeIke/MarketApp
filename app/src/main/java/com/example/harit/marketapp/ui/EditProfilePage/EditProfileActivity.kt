package com.example.harit.marketapp.ui.EditProfilePage

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asksira.bsimagepicker.BSImagePicker
import com.asksira.bsimagepicker.Utils
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.Toast
import com.example.harit.marketapp.extention.setImageUri
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.ui.model.ChatListModel
import com.example.harit.marketapp.ui.model.ChatModel
import com.example.harit.marketapp.ui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ServerValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity(),BSImagePicker.OnSingleImageSelectedListener{

    var imageUrl : String? = null
    var address : String? = null
    lateinit var myUser: User
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        myUser = intent.getParcelableExtra("user")
        imageUrl = myUser.imageUrl
        address = myUser.address

        setTopBar()
        initInstance()

    }

    private fun initInstance() {

        val singleSelectionPicker = BSImagePicker.Builder("com.example.harit.marketapp.fileprovider")
                .setMaximumDisplayingImages(24) //Default: Integer.MAX_VALUE. Don't worry about performance :)
                .setSpanCount(3) //Default: 3. This is the number of columns
                .setGridSpacing(Utils.dp2px(2)) //Default: 2dp. Remember to pass in a value in pixel.
                .setPeekHeight(Utils.dp2px(360)) //Default: 360dp. This is the initial height of the dialog.
                //Default: show. Set this if you don't want to further let user select from a gallery app. In such case, I suggest you to set maximum displaying images to Integer.MAX_VALUE.
                .build()

        imageView.setOnClickListener {
            singleSelectionPicker.show(supportFragmentManager, "picker")
        }

        editBtn.setOnClickListener {
            FirebaseFirestore.getInstance().collection("Users").document(uid!!).update("imageUrl",imageUrl,
                    "name",nameEdt.text.toString(),
                    "address",if(addressEdt.text.isNullOrEmpty()) null else addressEdt.text.toString()).addOnSuccessListener {
                "Update Complete".Toast(this)
            }
        }

        nameEdt.setText(myUser.name)
        myUser.address?.let {
            addressEdt.setText(it)
        }
        myUser.imageUrl?.let {
            imageView.setImageUrl(it)
        }
    }

    override fun onSingleImageSelected(uri: Uri?) {
        uri?.let {
            imageView.setImageUri(uri)
            val imageName = "${System.currentTimeMillis()}${uri.lastPathSegment}"
            FirebaseStorage.getInstance().reference.child("images/$imageName").putFile(uri)
                    .addOnSuccessListener {
                        FirebaseStorage.getInstance()
                                .reference.child("images/$imageName")
                                .downloadUrl.addOnCompleteListener {
                            val url = it.result.toString()
                            imageUrl = url
                        }
                    }
        }
    }

    private fun setTopBar() {
        topBar.setText("Edit Profile")
        topBar.haveSearch(false)
        topBar.haveNoti(false)
        topBar.haveBack(true)
        topBar.haveChat(false)
        topBar.getBackHolder()?.setOnClickListener {
            onBackPressed()
        }
    }
}