//package com.example.myapplication
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.myapplication.models.Product
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class ProductSearchActivity : AppCompatActivity() {
//    private lateinit var productRecyclerView: RecyclerView
//    private lateinit var productAdapter: ProductAdapter
//    private val productList = mutableListOf<Product>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_product_search)
//
//        productRecyclerView = findViewById(R.id.productRecyclerView)
//        productRecyclerView.layoutManager = LinearLayoutManager(this)
//        productAdapter = ProductAdapter(productList)
//        productRecyclerView.adapter = productAdapter
//
//        val keyword = "example" // Замените на ваше ключевое слово
//        searchProducts(keyword)
//    }
//
//    private fun searchProducts(keyword: String) {
//        RetrofitClient.instance.searchProducts(keyword).enqueue(object : Callback<List<Product>> {
//            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        productList.clear()
//                        productList.addAll(it)
//                        productAdapter.notifyDataSetChanged()
//                    }
//                } else {
//                    Toast.makeText(this@ProductSearchActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
//                Toast.makeText(this@ProductSearchActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//}
