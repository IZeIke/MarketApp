package com.example.harit.marketapp.ui.settingPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.harit.marketapp.R
import com.example.harit.marketapp.extention.setImageUrl
import com.example.harit.marketapp.ui.chatPage.ChatListActivity
import com.example.harit.marketapp.ui.feedPage.MainActivity
import com.example.harit.marketapp.ui.loginPage.LoginActivity
import com.example.harit.marketapp.ui.model.SearchModel
import com.example.harit.marketapp.ui.model.User
import com.example.harit.marketapp.ui.searchPage.FilterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_setting.*
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import com.example.harit.marketapp.ui.EditProfilePage.EditProfileActivity
import com.example.harit.marketapp.ui.HistoryPage.HistoryActivity
import com.example.harit.marketapp.ui.NotiPage.NotiActivity
import com.example.harit.marketapp.ui.addBankAccountPage.BankAccountActivity


class SettingFragment : Fragment() {

    lateinit var myUser : User

    companion object {
        fun newInstance(bundle: Bundle): SettingFragment {
            val fragment = SettingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myUser = arguments?.getParcelable("myUser")!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_setting,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopBar()

        /*FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().currentUser?.uid!!).get()
                .addOnCompleteListener {
                    var myUser = it.result.toObject(User::class.java)*/
                    initInstance(myUser)
                //}

        logOutBtn.setOnClickListener {
            showAlertDialogButtonClicked()
        }

        historyBtn.setOnClickListener {
            startActivity(Intent(context,HistoryActivity::class.java))
        }

        profileBtn.setOnClickListener {
            startActivity(Intent(context,EditProfileActivity::class.java).putExtra("user",myUser))
        }

        bankHolder.setOnClickListener {
            startActivity(Intent(context,BankAccountActivity::class.java))
        }
    }

    private fun initInstance(myUser: User?) {
        userName.text = myUser?.name
        myUser?.imageUrl?.let {
            profile_image.setImageUrl(it)
        }
    }

    private fun setTopBar() {
        topBar.setText("Setting")
        topBar.setChatNoti("0")
        topBar.setNoti("0")
        topBar.getSearchHolder()?.setOnClickListener {
            var bundle = Bundle().also { bundle ->
                bundle.putParcelable("model", SearchModel())
                bundle.putInt("tag",1)
            }
            startActivity(Intent(context, FilterActivity::class.java)
                    .putExtras(bundle))
        }

        topBar.getChatHolder()?.setOnClickListener {
            startActivity(Intent(context, ChatListActivity::class.java))
        }

        topBar.getNotiHolder()?.setOnClickListener {
            startActivity(Intent(context, NotiActivity::class.java))
        }
    }

    @SuppressLint("ResourceAsColor")
    fun showAlertDialogButtonClicked() {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle("ออกจากระบบ")
        builder.setMessage("ต้องการออกจากระบบ")
        // add the buttons
        builder.setPositiveButton("Log out") { dialog, which ->
            FirebaseAuth.getInstance().signOut().also {
                startActivity(Intent(activity, LoginActivity::class.java))
                (activity as MainActivity).finish()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        // create and show the alert dialog
        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.black)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.black) }
        dialog.show()
    }
}