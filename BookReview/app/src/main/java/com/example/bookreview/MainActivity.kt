package com.example.bookreview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.bookreview.adapter.BookAdapter
import com.example.bookreview.adapter.HistoryAdapter
import com.example.bookreview.api.BookService
import com.example.bookreview.databinding.ActivityMainBinding
import com.example.bookreview.model.BestSellerDto
import com.example.bookreview.model.History
import com.example.bookreview.model.SearchBookDto
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: BookAdapter

    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var bookService: BookService

    private lateinit var db: AppDatabase
    //Database 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, Loading::class.java)
        startActivity(intent)

        initBookRecyclerView()
        inithistoryRecyclerView()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()
        //Database 등록 및 사용

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            //Retrofit 사용 선언
            //기본 Url과 연결 및 생성

        bookService = retrofit.create(BookService::class.java)
        //retrofit으로 bookservice에 선언된 api 형식을 저장

        //사용자 API 키를 인자로 받음
        bookService.getBestSellerBooks(getString(R.string.interparkAPI))
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
                        adapter.submitList(it.books)
                        //currentList가 bookList로 변경
                    }
                }

                override fun onFailure( //API 요청 실패 시
                    call: Call<BestSellerDto>,
                    t: Throwable) {
                    Log.e(TAG, t.toString())
                }
            })

    }

    private fun search(keyword: String) {
        bookService.getBooksByName(getString(R.string.interparkAPI),keyword)
            .enqueue(object : Callback<SearchBookDto> { //데이터를 큐의 형태로 저장 및 사용
                override fun onResponse( //API 요청 성공 시
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {

                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    if(response.isSuccessful.not()) {
                        Log.e(TAG, "Not Success!")
                        return
                    }
                    adapter.submitList(response.body()?.books.orEmpty())
                    //currentList가 bookList로 변경
                }

                override fun onFailure( //API 요청 실패 시
                    call: Call<SearchBookDto>,
                    t: Throwable) {

                    hideHistoryView()
                    Log.e(TAG, t.toString())
                }
            })
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter(itemClickedListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        })

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
        //바인딩을 통한 RecyclerView 초기화
    }

    private fun inithistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickedListener = {
            deleteSearchKeyword(it)
        }) //delete 버튼을 눌러 데이터 삭제 함수 호출

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
        //반영할 layout과 adapter 연결
        initSearchEditText()
    }

    private fun initSearchEditText() {
        binding.searchEdt.setOnKeyListener{ v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) { //Enter가 눌렸을 때
                search(binding.searchEdt.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.searchEdt.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun showHistoryView() {
        Thread{
            val keywords = db.historyDao().getAlld().reversed()
            //DB의 모든 단어 데이터를 담기 위한 변수

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
                //가져온 DB 데이터를 submitList로 추가
            }
        }.start()
        binding.historyRecyclerView.isVisible = true
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null,keyword))
        }.start()
        //Thread를 이용하여 history DB에 keyword를 저장
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            //delete 이후에는 view 갱신 필요
            showHistoryView()
        }.start()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}