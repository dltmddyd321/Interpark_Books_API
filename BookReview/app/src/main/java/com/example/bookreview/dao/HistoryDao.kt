package com.example.bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bookreview.model.History

//검색 기록 저장 DB에 대한 Dao 선언
@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    fun getAlld(): List<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM history WHERE keyword == :keyword")
    fun delete(keyword: String)
    //keyword가 일치하면 삭제
}