package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity() {

    private lateinit var fullNameTextView: TextView
    private lateinit var fullEmailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        fullNameTextView = findViewById(R.id.profileName)
        loadUserProfile(0)

        findViewById<LinearLayout>(R.id.linearLayout_favourite).setOnClickListener {
            val intent = Intent(this, UserFavouritesActivity::class.java)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.linearLayout_info).setOnClickListener {
            setContentView(R.layout.activity_user_info)
            fullNameTextView = findViewById(R.id.profileName)
            fullEmailTextView = findViewById(R.id.profileEmail)
            loadUserProfile(1)
            findViewById<ImageButton>(R.id.backButton).setOnClickListener {
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        findViewById<LinearLayout>(R.id.linearLayout_help).setOnClickListener {
            setContentView(R.layout.activity_help)
            findViewById<ImageButton>(R.id.backButton).setOnClickListener {
                val intent = Intent(this, UserProfileActivity::class.java)
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.button_home).setOnClickListener {
            val intent = Intent(this, GeneralMenuActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadUserProfile(mode: Int = 0) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(currentUser.uid)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            fullNameTextView.text = "${user.firstName} ${user.lastName}"
                            if (mode != 0) {
                                fullEmailTextView.text = user.Email
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("UserProfileActivity", "get failed with ", exception)
                }
        }
    }
}
