package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.Category
import com.example.myapplication.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryProductsActivity : AppCompatActivity() {

    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_products)

        productsRecyclerView = findViewById(R.id.productsRecyclerView)
        productsRecyclerView.layoutManager = LinearLayoutManager(this)

        val categoryId = intent.getIntExtra("category_id", -1)
        val categoryName = intent.getStringExtra("category_name")
        title = categoryName

        if (categoryId != -1) {
            loadProducts(categoryId)
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun loadProducts(categoryId: Int) {
        RetrofitClient.instance.getCategory(categoryId).enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful) {
                    val category = response.body()
                    val products = category?.Products ?: emptyList()
                    productAdapter = ProductAdapter(products)
                    productsRecyclerView.adapter = productAdapter
                } else {
                    Toast.makeText(this@CategoryProductsActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Toast.makeText(this@CategoryProductsActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }
}
