package com.example.myapplication

import android.util.Log
import com.google.android.gms.tasks.Tasks
import okhttp3.Interceptor
import okhttp3.Response
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestBuilder = request.newBuilder()

        // Получаем текущего пользователя Firebase
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            try {
                val task = user.getIdToken(false)
                val tokenResult = Tasks.await(task, 10, TimeUnit.SECONDS)
                val idToken = tokenResult.token
                if (idToken != null) {
                    newRequestBuilder.addHeader("Authorization", "Bearer $idToken")
                }
            } catch (e: InterruptedException) {
                Log.e("AuthInterceptor", "Error getting Firebase token", e)
            }
        }

        val newRequest = newRequestBuilder.build()
        Log.d("AuthInterceptor", request.headers.toString())
        Log.d("AuthInterceptor", newRequest.headers.toString())

        return chain.proceed(newRequest)
    }
}
