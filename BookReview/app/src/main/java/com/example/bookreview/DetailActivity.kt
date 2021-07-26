package com.example.bookreview

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.bookreview.databinding.ActivityDetailBinding
import com.example.bookreview.model.Book
import com.example.bookreview.model.Review

class DetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()
        //Database 등록 및 사용

        val model = intent.getParcelableExtra<Book>("bookModel")
        //intent를 통해 Main에서부터 넘어온 직렬화된 데이터 값을 변수에 저장

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()
        //Nullable한 모델 데이터를 text를 통해 보이도록 연결
        Glide.with(binding.coverImgView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImgView)
        //이미지 데이터 또한 Glide를 통해 연결

        Thread {
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            runOnUiThread{
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }.start() //기존 리뷰가 있다면 데이터 가져오기

        binding.saveBtn.setOnClickListener {
            Thread{
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?: 0,
                    binding.reviewEditText.text.toString())
                )
            }.start()
            //저장하기 버튼 클릭 시 사용자가 입력한 문구 데이터가 ReView DB를 통해 저장
        }

    }
}