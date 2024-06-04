package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.Category
import com.squareup.picasso.Picasso

class CategoryAdapter(private val categories: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryNameTextView.text = category.Name
//        Picasso.get().load(category.ImgUrl).into(holder.categoryImageView)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CategoryProductsActivity::class.java).apply {
                putExtra("category_id", category.ID)
                putExtra("category_name", category.Name)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        val categoryImageView: ImageView = itemView.findViewById(R.id.categoryImageView)
    }
}
