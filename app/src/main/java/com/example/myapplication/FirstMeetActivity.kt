package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class FirstMeetActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firstmeet)

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            val intent = Intent(this, GeneralMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}