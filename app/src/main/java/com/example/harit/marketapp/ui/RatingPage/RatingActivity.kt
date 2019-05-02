package com.example.harit.marketapp.ui.RatingPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.harit.marketapp.R
import com.example.harit.marketapp.ui.adapter.RatingPageAdapter
import com.example.harit.marketapp.ui.model.RatingModel
import com.example.harit.marketapp.ui.model.ScoreModel
import com.example.harit.marketapp.ui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_bank_account.*

class RatingActivity : AppCompatActivity(),RatingPageAdapter.RatingPageAdapterInterface{

    val uid = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var scoreModel: ScoreModel
    lateinit var myUser : User
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_account)

        scoreModel = intent.getParcelableExtra("model")
        myUser = intent.getParcelableExtra("myUser")
        user = intent.getParcelableExtra("user")

        initInstance()
    }

    private fun initInstance() {

        setTopBar()

        getData()

    }

    private fun getData() {

        val bankList = mutableListOf<RatingModel>()
        FirebaseFirestore.getInstance().collection("Rating").document("reviewList")
                .collection(user.id!!).get().addOnCompleteListener {task ->
                    if(task.isSuccessful){
                        for (doc in task.result){
                            val model = doc.toObject(RatingModel::class.java)
                            bankList.add(model)
                        }
                        setRecyclerView(bankList)
                    }

                }
    }

    private fun setRecyclerView(list: MutableList<RatingModel>) {
        recyclerView?.let {
            var layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = RatingPageAdapter(this,myUser,scoreModel ,list)
            recyclerView.adapter?.notifyDataSetChanged()
            //stopLoading()
        }
    }

    private fun setTopBar() {
        topBar.setText("Rating & Review")
        topBar.haveBack(true)
        topBar.haveChat(false)
        topBar.haveNoti(false)
        topBar.haveSearch(false)
    }

    override fun onUpdateReview(ratingModel: RatingModel) {
        getData()
    }

    override fun onAddReview(ratingModel: RatingModel) {

        val batch = FirebaseFirestore.getInstance().batch()

        val ref1 = FirebaseFirestore.getInstance().collection("Rating").document("reviewList")
                .collection(user.id!!).document()

        batch.set(ref1,ratingModel)

        if(scoreModel.number == null){
            scoreModel.number = (1).toDouble()
            scoreModel.scoreSum = ratingModel.score
        }else{
            scoreModel.number = scoreModel.number!! + 1
            scoreModel.scoreSum = scoreModel.scoreSum!! + ratingModel.score!!
        }

        val ref2 = FirebaseFirestore.getInstance().collection("Rating").document("score")
                .collection(user.id!!).document("score")

        batch.set(ref2,scoreModel)

        batch.commit().addOnCompleteListener {
            getData()
        }
    }


}