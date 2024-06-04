package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_products")
data class ProductEntity(
    @PrimaryKey val id: Int
)
