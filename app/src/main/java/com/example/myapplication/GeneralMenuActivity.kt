package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GeneralMenuActivity : AppCompatActivity() {

    private lateinit var popularProductsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_menu)

//        productsContainer = findViewById(R.id.products)

        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)?.addOnSuccessListener { result ->
            val idToken = result.token
            if (idToken != null) {
                Log.d("GeneralMenuActivity", idToken.toString())
            }
        }?.addOnFailureListener {
            // Обработка ошибки получения токена
        }

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val intent = Intent(this@GeneralMenuActivity, ProductsActivity::class.java)
                    intent.putExtra("keyword", it)
                    startActivity(intent)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        popularProductsContainer = findViewById(R.id.popularProductsContainer)

        loadPopularProducts()

        findViewById<Button>(R.id.button_all_categories).setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_user_profile).setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadPopularProducts() {
        RetrofitClient.instance.getPopularProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    displayPopularProducts(products)
                } else {
                    Toast.makeText(this@GeneralMenuActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@GeneralMenuActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayPopularProducts(products: List<Product>) {
        popularProductsContainer.removeAllViews()
        for (product in products) {
            val productView = layoutInflater.inflate(R.layout.item_popular_product, popularProductsContainer, false)
            val productNameTextView = productView.findViewById<TextView>(R.id.productNameTextView)
            val productPriceTextView = productView.findViewById<TextView>(R.id.productPriceTextView)
            val productImageView = productView.findViewById<ImageView>(R.id.productImageView)

            productNameTextView.text = product.Name
            productPriceTextView.text = "${product.LowestPrice} €"
            //Picasso.get().load(product.ImgUrl).into(productImageView)

            productView.setOnClickListener {
                val intent = Intent(this, ProductDetailActivity::class.java).apply {
                    putExtra("product_id", product.ID)
                }
                startActivity(intent)
            }

            popularProductsContainer.addView(productView)
        }
    }
}
