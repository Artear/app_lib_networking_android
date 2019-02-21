package com.artear.networking.model

import retrofit2.Call
import retrofit2.http.GET

interface ApiModel {

    @GET("/path/test")
    fun getTestModelTest() : Call<Model>
}
