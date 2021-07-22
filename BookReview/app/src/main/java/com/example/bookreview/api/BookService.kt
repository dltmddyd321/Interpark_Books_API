package com.example.bookreview.api

import retrofit2.Call
import com.example.bookreview.model.BestSellerDto
import com.example.bookreview.model.SearchBookDto
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSellerDto>
    //API 서비스를 통해 데이터를 받아오고 쿼리문의 값을 변수로 저장
    //Call을 통해 지정한 모델을 받기
}