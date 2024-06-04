package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HelloActivity : AppCompatActivity() {

    private lateinit var greetingText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        greetingText = findViewById(R.id.greetingText)
        loadUserProfile()

        RetrofitClient.instance.postAccount().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, GeneralMenuActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    private fun loadUserProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(currentUser.uid)

            userRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            greetingText.text = "Sveiki, ${user.firstName} ${user.lastName}"
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("UserProfileActivity", "get failed with ", exception)
                }
        }
    }
}
