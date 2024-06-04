package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.models.Product
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFavouritesActivity : AppCompatActivity() {

    private lateinit var favouritesContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_favourites)

        favouritesContainer = findViewById(R.id.products)

        loadFavouriteProducts()

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadFavouriteProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val favouriteProducts = db.productDao().getAllProducts()
            if (favouriteProducts.isNotEmpty()) {
                val ids = favouriteProducts.map { it.id }
                loadProductsFromApi(ids)
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@UserFavouritesActivity, "No favourite products found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadProductsFromApi(ids: List<Int>) {
        val apiService = RetrofitClient.instance
        for (id in ids) {
            apiService.getProduct(id).enqueue(object : Callback<Product> {
                override fun onResponse(call: Call<Product>, response: Response<Product>) {
                    if (response.isSuccessful) {
                        val product = response.body()
                        product?.let {
                            displayProduct(it)
                        }
                    } else {
                        Log.e("UserFavouritesActivity", "Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Product>, t: Throwable) {
                    Log.e("UserFavouritesActivity", "Failure: ${t.message}")
                }
            })
        }
    }

    private fun displayProduct(product: Product) {
        val productView = layoutInflater.inflate(R.layout.item_popular_product, favouritesContainer, false)
        val productNameTextView = productView.findViewById<TextView>(R.id.productNameTextView)
        val productPriceTextView = productView.findViewById<TextView>(R.id.productPriceTextView)
        val productImageView = productView.findViewById<ImageView>(R.id.productImageView)
        val productVolTextView = productView.findViewById<TextView>(R.id.productVolTextView)
        val productQty = productView.findViewById<TextView>(R.id.productQty)
        var volume = ""
        var alco_percent = ""

        productNameTextView.text = product.Name
        productPriceTextView.text = "${product.LowestPrice} €"
        Picasso.get().load(product.ImgUrl).into(productImageView)
        for (attr in product.ProductAttributes) {
            if (attr.Identifier == "volume") {
                volume = attr.Value
            } else if (attr.Identifier == "alco_percent") {
                alco_percent = attr.Value
            }
        }

        productVolTextView.text = "${volume} Con | ${alco_percent}"
        if (product.Stores[0].Qty > 10) {
            productQty.setTextColor(Color.parseColor("#006400"))
            productQty.text = "${product.Stores[0].Qty} atlikuši krājumos"
        } else {
            productQty.text = "${product.Stores[0].Qty} atlikuši krājumos"
        }

        productView.setOnClickListener {
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("product_id", product.ID)
            }
            startActivity(intent)
        }

        favouritesContainer.addView(productView)
    }
}
