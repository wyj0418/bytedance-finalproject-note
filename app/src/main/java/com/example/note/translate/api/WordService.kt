package com.example.note.translate.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://movie.querydata.org/api?id=25845392

interface WordService {
    @GET("api/translation.php")
    fun getMovieInfo(@Query("info") id: String): Call<WordBean>
}