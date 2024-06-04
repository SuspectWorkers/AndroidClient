package com.example.myapplication.models

data class Product(
    val ID: Int,
    val Name: String,
    val LowestPrice: Double,
    val ImgUrl: String,
    val Stores: List<Store>,
    val ProductAttributes: List<Attribute>,
    val Reviews: List<Review>
)

data class Category(
    val ID: Int,
    val Name: String,
    val ImgUrl: String,
    val CategoryLevel: Int,
    val ChildCategories: List<Category>,
    val CategoryAttributes: List<Attribute>,
    val Products: List<Product>
)

data class Attribute(
    val ID: Int,
    val Value: String,
    val Identifier: String,
    val Name: String,
    val AttrType: String
)

data class Store(
    val ID: Int,
    val Name: String,
    val StoreLink: String,
    val ImgUrl: String,
    val Price: Double,
    val Barcode: String,
    val Qty: Int
)

data class Review(
    val ID: Int,
    val Value: String,
    val Rating: Int,
    val AddetAt: String,
    val User: User
)

data class User(
    val Email: String = "",
    val firstName: String = "",
    val lastName: String = ""
)
