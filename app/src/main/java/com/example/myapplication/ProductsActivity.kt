// ProductsActivity.kt

package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.models.Product
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsActivity : AppCompatActivity() {

    private lateinit var productsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        productsContainer = findViewById(R.id.products)

        val keyword = intent.getStringExtra("keyword") ?: return

        searchProducts(keyword)

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            val intent = Intent(this, GeneralMenuActivity::class.java)
            startActivity(intent)
        }

    }

    private fun searchProducts(keyword: String) {
        RetrofitClient.instance.searchProducts(keyword).enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        displayProducts(it)
                    }
                } else {
                    Toast.makeText(this@ProductsActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@ProductsActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayProducts(products: List<Product>) {
        productsContainer.removeAllViews()
        for (product in products) {
            val productView = layoutInflater.inflate(R.layout.item_product, productsContainer, false)
            productView.findViewById<TextView>(R.id.productName).text = product.Name
            productView.findViewById<TextView>(R.id.productPrice).text = "${product.LowestPrice} €"
//            Picasso.get().load(product.ImgUrl).into(productView.findViewById(R.id.productImage))
            productView.setOnClickListener {
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("product_id", product.ID)
                startActivity(intent)
            }
            productsContainer.addView(productView)
        }
    }
}
