package com.example.myapplication

import com.example.myapplication.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("api/get/products/search/{keyword}")
    fun searchProducts(@Path("keyword") keyword: String): Call<List<Product>>

    @GET("api/get/categories")
    fun getCategories(): Call<List<Category>>

    @GET("api/get/category/{id}")
    fun getCategory(@Path("id") id: Int): Call<Category>

    @GET("api/get/product/{id}")
    fun getProduct(@Path("id") id: Int): Call<Product>

    @POST("api/review/post")
    fun postReview(@Body review: ReviewRequest): Call<Void>

    @GET("api/get/popular/products")
    fun getPopularProducts(): Call<List<Product>>
}

data class ReviewRequest(
    val ProductId: Int,
    val Rating: Int,
    val Value: String
)
