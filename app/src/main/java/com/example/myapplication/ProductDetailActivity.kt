package com.example.myapplication

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.models.Product
import com.example.myapplication.models.Store
import com.example.myapplication.database.ProductEntity
import com.example.myapplication.models.Review
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productNameTextView: TextView
    private lateinit var productImageView: ImageView
    private lateinit var productPriceTextView: TextView
    private lateinit var productQuantity: TextView
    private lateinit var productInfoText: TextView
    private lateinit var likeButton: ImageButton
    private lateinit var commentEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var commentsContainer: LinearLayout
    private lateinit var storesContainer: LinearLayout
    private var productId: Int = -1
    private var isLiked: Boolean = false
    private var product_alco_percent = ""
    private var product_volume = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        productNameTextView = findViewById(R.id.productNameTextView)
        productImageView = findViewById(R.id.productImageView)
        productPriceTextView = findViewById(R.id.productPriceTextView)
        productQuantity = findViewById(R.id.quantityText)
        productInfoText = findViewById(R.id.infoText)
        likeButton = findViewById(R.id.likeButton)
        commentEditText = findViewById(R.id.commentEditText)
        sendButton = findViewById(R.id.sendButton)
        commentsContainer = findViewById(R.id.commentsContainer)
        storesContainer = findViewById(R.id.storesContainer)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        productId = intent.getIntExtra("product_id", -1)
        if (productId != -1) {
            getProductDetails(productId)
            checkIfProductLiked(productId)
            loadComments(productId)
        }

        likeButton.setOnClickListener {
            toggleLikeProduct()
        }

        sendButton.setOnClickListener {
            submitComment()
        }

    }

    private fun getProductDetails(productId: Int) {
        RetrofitClient.instance.getProduct(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    response.body()?.let { product ->
                        productNameTextView.text = product.Name
                        if (product.Stores[0].Qty > 10) {
                            productQuantity.setTextColor(Color.parseColor("#006400"))
                            productQuantity.text = "${product.Stores[0].Qty} atlikuši krājumos"
                        } else {
                            productQuantity.text = "${product.Stores[0].Qty} atlikuši krājumos"
                        }
                        for (attribute in product.ProductAttributes) {
                            if (attribute.Identifier == "volume") {
                                product_volume = attribute.Value
                            } else if (attribute.Identifier == "alco_percent") {
                                product_alco_percent = attribute.Value
                            }
                        }
                        productInfoText.text = "${product_volume} Con | ${product_alco_percent}"
                        productPriceTextView.text = "${product.LowestPrice} €"
                        Picasso.get().load(product.ImgUrl).into(productImageView)
                        // Set other product details

                        displayStores(product.Stores)
                        displayComments(product.Reviews)
                    }
                } else {
                    Toast.makeText(this@ProductDetailActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

//    private fun displayStoreInfo(stores: List<Store>) {
//        productDescriptionLayout.removeAllViews()
//        for (store in stores) {
//            val storeInfoView = LayoutInflater.from(this).inflate(R.layout.item_store, productDescriptionLayout, false)
//            val storeNameTextView = storeInfoView.findViewById<TextView>(R.id.storeName)
//            //val storeImage = storeInfoView.findViewById<TextView>(R.id.storeImage)
//            val storePriceTextView = storeInfoView.findViewById<TextView>(R.id.storePrice)
//
//            storeNameTextView.text = store.Name
//            storePriceTextView.text = "${store.Price} €"
//            // Picasso.get().load(store.ImgUrl).into(productImageView)
//
//            productDescriptionLayout.addView(storeInfoView)
//        }
//    }

    private fun displayStores(stores: List<Store>) {
        storesContainer.removeAllViews()
        for (store in stores) {
            val storeView = LayoutInflater.from(this).inflate(R.layout.item_store, storesContainer, false)
            storeView.findViewById<TextView>(R.id.storeNameTextView).text = store.Name
            storeView.findViewById<TextView>(R.id.storePriceTextView).text = "${store.Price} €"
            Picasso.get().load(store.ImgUrl).into(storeView.findViewById<ImageView>(R.id.storeImageView))
            storesContainer.addView(storeView)
        }
    }

    private fun checkIfProductLiked(productId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val product = db.productDao().getProductById(productId)
            isLiked = product != null
            runOnUiThread {
                updateLikeButton()
            }
        }
    }

    private fun toggleLikeProduct() {
        if (isLiked) {
            removeProductFromDatabase()
        } else {
            saveProductToDatabase()
        }
    }

    private fun saveProductToDatabase() {
        if (productId != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val productEntity = ProductEntity(
                    id = productId
                )
                db.productDao().insertProduct(productEntity)
                isLiked = true
                runOnUiThread {
                    updateLikeButton()
                    Toast.makeText(this@ProductDetailActivity, "Product liked!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun removeProductFromDatabase() {
        if (productId != -1) {
            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val productEntity = db.productDao().getProductById(productId)
                productEntity?.let {
                    db.productDao().deleteProduct(it)
                    isLiked = false
                    runOnUiThread {
                        updateLikeButton()
                        Toast.makeText(this@ProductDetailActivity, "Product unliked!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun updateLikeButton() {
        if (isLiked) {
            likeButton.setColorFilter(Color.RED)
        } else {
            likeButton.setColorFilter(Color.BLACK)
        }
    }

    private fun submitComment() {
        val commentText = commentEditText.text.toString()
        if (commentText.length >= 5) {
            val review = ReviewRequest(
                ProductId = productId,
                Rating = 4,
                Value = commentText
            )
            RetrofitClient.instance.postReview(review).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProductDetailActivity, "Comment submitted!", Toast.LENGTH_SHORT).show()
                        commentEditText.text.clear()
                        loadComments(productId)
                    } else {
                        Toast.makeText(this@ProductDetailActivity, "Failed to submit comment: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ProductDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Comment must be at least 5 characters long", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadComments(productId: Int) {
        RetrofitClient.instance.getProduct(productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    response.body()?.let { product ->
                        displayComments(product.Reviews)
                    }
                } else {
                    Toast.makeText(this@ProductDetailActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@ProductDetailActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayComments(reviews: List<Review>) {
        commentsContainer.removeAllViews()
        for (review in reviews) {
            val commentView = layoutInflater.inflate(R.layout.item_comment, commentsContainer, false)
            commentView.findViewById<TextView>(R.id.commentUserName).text = review.User.Email
            commentView.findViewById<TextView>(R.id.commentText).text = review.Value
            commentsContainer.addView(commentView)
        }
    }
}
