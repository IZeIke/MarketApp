package com.example.harit.marketapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.addBankAccountPage.BankAccountActivity
import com.example.harit.marketapp.ui.model.BankAccountModel
import kotlinx.android.synthetic.main.item_add_bank_account.view.*
import kotlinx.android.synthetic.main.item_bank_account.view.*

class BankPageAdapter(val listener: BankPageAdapterInterface, private val feedList: MutableList<BankAccountModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface BankPageAdapterInterface{
        fun onAddBank(model : BankAccountModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0) {
            return AddViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_add_bank_account, parent, false))
        }else{
            return BankViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bank_account, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return feedList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            0
        }else{
            1
        }
    }

    fun addList(list : BankAccountModel){

            feedList.add(list)

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is AddViewHolder){
            holder.addBtn.setOnClickListener {
                listener.onAddBank(BankAccountModel(holder.bankNameEdt.text.toString(),holder.addressEdt.text.toString()))
            }

        }else{
            if(holder is BankViewHolder){
                holder.bankName.text = "ธนาคาร "+feedList[position-1].bankName
                holder.account.text = "เลขบัญชี "+feedList[position-1].account
            }
        }
    }

    class AddViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var bankNameEdt = itemView.bankNameEdt
        var addressEdt = itemView.accountEdt
        var addBtn = itemView.addBtn
    }

    class BankViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var bankName = itemView.bankName
        var account = itemView.bankAccount
    }

}