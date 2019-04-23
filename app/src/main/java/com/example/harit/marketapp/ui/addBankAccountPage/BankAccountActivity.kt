package com.example.harit.marketapp.ui.addBankAccountPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.adapter.BankPageAdapter
import com.example.harit.marketapp.ui.model.BankAccountModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_bank_account.*

class BankAccountActivity : AppCompatActivity(),BankPageAdapter.BankPageAdapterInterface {

    val uid = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_account)

        initInstance()
    }

    private fun initInstance() {

        setTopBar()

        val bankList = mutableListOf<BankAccountModel>()

        FirebaseFirestore.getInstance().collection("BankAccount").document("list")
                .collection(uid!!).get().addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        for (doc in task.result){
                            val model = doc.toObject(BankAccountModel::class.java)
                            bankList.add(model)
                        }

                        setRecyclerView(bankList)
                    }

                }

    }

    private fun setRecyclerView(bankList: MutableList<BankAccountModel>) {
        recyclerView?.let {
            var layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = BankPageAdapter(this, bankList)
            recyclerView.adapter?.notifyDataSetChanged()
            //stopLoading()
        }
    }

    override fun onAddBank(model: BankAccountModel) {
        FirebaseFirestore.getInstance().collection("BankAccount").document("list")
                .collection(uid!!).document().set(model).addOnSuccessListener {
                    (recyclerView.adapter as BankPageAdapter).addList(model)
                }
    }

    private fun setTopBar() {
        topBar.setText("Add Bank Account")
        topBar.haveBack(true)
        topBar.haveChat(false)
        topBar.haveNoti(false)
        topBar.haveSearch(false)
    }

}