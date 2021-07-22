package com.example.bookreview.model

import com.google.gson.annotations.SerializedName

data class BestSellerDto ( //상위 첫 번째 모델 지정
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>
    //여러 개의 책 모델을 담기 위해 List 선언

)