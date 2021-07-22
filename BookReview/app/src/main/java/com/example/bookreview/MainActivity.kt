package com.example.bookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bookreview.api.BookService
import com.example.bookreview.model.BestSellerDto
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            //Retrofit 사용 선언
            //기본 Url과 연결 및 생성

        val bookService = retrofit.create(BookService::class.java)
        //retrofit으로 bookservice에 선언된 api 형식을 저장

        //사용자 API 키를 인자로 받음
        bookService.getBestSellerBooks("44D3A5ADD6155B28743F4BE92399C8FC546C37C2571EEDB40C378F3A2391FD57")
            .enqueue(object : Callback<BestSellerDto> { //데이터를 큐의 형태로 저장 및 사용
                override fun onResponse( //API 요청 성공 시
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if(response.isSuccessful.not()) {
                        Log.e(TAG, "Not Success!")
                        return
                    }
                    response.body()?.let {  //body에 BestSellerDto가 저장되어 있음
                        Log.d(TAG, it.toString())

                        it.books.forEach { book -> //body에 저장된 책 데이터를 하나씩 확인
                            Log.d(TAG, book.toString())
                        }
                    }
                }

                override fun onFailure( //API 요청 실패 시
                    call: Call<BestSellerDto>,
                    t: Throwable) {
                    Log.e(TAG, t.toString())
                }
            })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}