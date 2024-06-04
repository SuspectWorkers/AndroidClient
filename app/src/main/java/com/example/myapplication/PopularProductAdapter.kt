package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.Product
import com.squareup.picasso.Picasso

class PopularProductAdapter(private val products: List<Product>) : RecyclerView.Adapter<PopularProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_popular_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.productNameTextView.text = product.Name
        holder.productPriceTextView.text = "${product.LowestPrice} €"
        Picasso.get().load(product.ImgUrl).into(holder.productImageView)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                putExtra("product_id", product.ID)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        val productPriceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
    }
}
