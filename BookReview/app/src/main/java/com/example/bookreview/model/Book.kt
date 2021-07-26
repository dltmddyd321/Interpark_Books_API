package com.example.bookreview.model

import android.app.ActivityManager
import android.icu.text.CaseMap
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book ( //하위 부속 모델 지정
    @SerializedName("itemId") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String
    //api의 JSON 값과 변수를 연결
): Parcelable
//Class의 직렬화 가능

