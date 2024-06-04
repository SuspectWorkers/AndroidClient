package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        findViewById<Button>(R.id.registerButton).setOnClickListener {
            val firstName = findViewById<EditText>(R.id.firstName).text.toString()
            val lastName = findViewById<EditText>(R.id.lastName).text.toString()
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid ?: ""
                        val userData = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "Email" to email
                        )
                        firestore.collection("users").document(userId).set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HelloActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
