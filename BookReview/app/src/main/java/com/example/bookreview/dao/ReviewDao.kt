package com.example.bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreview.model.Review

//리뷰 저장 DB에 대한 Dao 선언
@Dao
interface ReviewDao {

    @Query( "SELECT * FROM review WHERE id == :id")
    fun getOneReview(id: Int): Review
    //지정 id에 대한 Review 데이터 호출

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)
    //새로운 값이 기존의 값을 대체하여 삽입
    //어떤 것의 내용을 추가 및 변경할 때 사용
}