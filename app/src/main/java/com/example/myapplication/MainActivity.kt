package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.content.Context
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import android.content.Intent

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.reg_button).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.log_button).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

//data class AlcoholPrice(
//    val name: String,
//    val price: Double,
//    val store: String
//)
//
//interface ApiService {
//    @GET("/list")
//    suspend fun getAlcoholPrices(): List<AlcoholPrice>
//}
//
//object RetrofitClient {
//    private const val BASE_URL = "https://all-powerful-confes.000webhostapp.com"
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .connectTimeout(5, TimeUnit.SECONDS)
//        .readTimeout(5, TimeUnit.SECONDS)
//        .writeTimeout(5, TimeUnit.SECONDS)
//        .build()
//
//    val apiService: ApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiService::class.java)
//    }
//}
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyApp()
//        }
//    }
//}
//
//@Composable
//fun MyApp() {
//    val navController = rememberNavController()
//    val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            // Пользователь уже аутентифицирован, переход к основному экрану
//            navController.navigate("main") {
//                popUpTo("choose") { inclusive = true }
//            }
//        }
//    }
//
//    NavHost(navController = navController, startDestination = "choose") {
//        composable("choose") { ChooseScreen(navController) }
//        composable("login") { LoginScreen(navController, context) }
//        composable("register") { RegistrationScreen(navController, context) }
//        composable("main") { MainScreen() }
//    }
//}
//
//@Composable
//fun ChooseScreen(navController: NavHostController) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
//    ) {
//        Button(
//            onClick = { navController.navigate("login") },
//            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
//        ) {
//            Text(text = "Login", color = Color.White)
//        }
//        Button(
//            onClick = { navController.navigate("register") },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = "Register", color = Color.White)
//        }
//    }
//}
//
//@Composable
//fun LoginScreen(navController: NavHostController, context: Context) {
//    val context = LocalContext.current
//    val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var showPassword by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Login",
//            fontSize = 20.sp,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("E-PASTA ADRESE") },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Next
//            ),
//            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
//        )
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("PAROLE") },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Done
//            ),
//            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
//            trailingIcon = {
//                IconButton(onClick = { showPassword = !showPassword }) {
//                    Icon(
//                        painter = painterResource(id = if (showPassword) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
//                        contentDescription = if (showPassword) "Hide password" else "Show password"
//                    )
//                }
//            },
//            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
//        )
//        Button(
//            onClick = {
//                auth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
//                            navController.navigate("main") {
//                                popUpTo("choose") { inclusive = true }
//                            }
//                        } else {
//                            Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//        ) {
//            Text(text = "Login", color = Color.White)
//        }
//    }
//}
//
//@Composable
//fun RegistrationScreen(navController: NavHostController, context: Context) {
//    val context = LocalContext.current
//    val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    val firestore = FirebaseFirestore.getInstance()
//    var firstName by remember { mutableStateOf("") }
//    var lastName by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var showPassword by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Lūdzu, sniedziet mums vēl dažas detaļas",
//            fontSize = 20.sp,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//        OutlinedTextField(
//            value = firstName,
//            onValueChange = { firstName = it },
//            label = { Text("Vārds") },
//            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
//        )
//        OutlinedTextField(
//            value = lastName,
//            onValueChange = { lastName = it },
//            label = { Text("Uzvārds") },
//            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
//        )
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("E-PASTA ADRESE") },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Next
//            ),
//            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
//        )
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("PAROLE") },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                keyboardType = KeyboardType.Password,
//                imeAction = ImeAction.Done
//            ),
//            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
//            trailingIcon = {
//                IconButton(onClick = { showPassword = !showPassword }) {
//                    Icon(
//                        painter = painterResource(id = if (showPassword) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
//                        contentDescription = if (showPassword) "Hide password" else "Show password"
//                    )
//                }
//            },
//            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
//        )
//        Button(
//            onClick = {
//                auth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val user = auth.currentUser
//                            val userId = user?.uid ?: ""
//                            val userData = hashMapOf(
//                                "firstName" to firstName,
//                                "lastName" to lastName,
//                                "email" to email
//                            )
//                            firestore.collection("users").document(userId).set(userData)
//                                .addOnSuccessListener {
//                                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
//                                    navController.navigate("main") {
//                                        popUpTo("choose") { inclusive = true }
//                                    }
//                                }
//                                .addOnFailureListener { e ->
//                                    Toast.makeText(context, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
//                                }
//                        } else {
//                            Toast.makeText(context, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
//        ) {
//            Text(text = "Turpināt", color = Color.White)
//        }
//    }
//}
//
////@Composable
////fun MainScreen() {
////    val alcoholPrices = remember { mutableStateOf(listOf<AlcoholPrice>()) }
////
////    // Fetch data from your API (here using Retrofit or another method)
////    // For demonstration purposes, we'll use dummy data
////    LaunchedEffect(Unit) {
////        // Simulate network call
////        alcoholPrices.value = listOf(
////            AlcoholPrice("Whiskey", 20.0, "Store A"),
////            AlcoholPrice("Vodka", 15.0, "Store B"),
////            AlcoholPrice("Beer", 5.0, "Store C")
////        )
////    }
////
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .padding(16.dp)
////    ) {
////        Text(
////            text = "List of Alcohol Products",
////            fontSize = 24.sp,
////            modifier = Modifier.padding(bottom = 16.dp)
////        )
////        LazyColumn {
////            items(alcoholPrices.value) { price ->
////                AlcoholPriceItem(price)
////            }
////        }
////    }
////}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MyApp()
//}
//
//@Composable
//fun MyApp(content: @Composable () -> Unit) {
//    MaterialTheme {
//        Surface(modifier = Modifier.fillMaxSize()) {
//            content()
//        }
//    }
//}
//
//@Composable
//fun MainScreen() {
//    val context = LocalContext.current
//    var prices by remember { mutableStateOf(listOf<AlcoholPrice>()) }
//
//    LaunchedEffect(Unit) {
//        fetchAlcoholPrices { result ->
//            prices = result
//        }
//    }
//
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        items(prices) { price ->
//            AlcoholPriceItem(price)
//        }
//    }
//}
//
//@Composable
//fun AlcoholPriceItem(price: AlcoholPrice) {
//    Column(modifier = Modifier.padding(8.dp)) {
//        Text(text = "Name: ${price.name}", style = MaterialTheme.typography.headlineLarge)
//        Text(text = "Price: ${price.price}", style = MaterialTheme.typography.bodyLarge)
//        Text(text = "Store: ${price.store}", style = MaterialTheme.typography.bodyMedium)
//    }
//}
//
//fun fetchAlcoholPrices(onResult: (List<AlcoholPrice>) -> Unit) {
//    CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val prices = RetrofitClient.apiService.getAlcoholPrices()
//            withContext(Dispatchers.Main) {
//                onResult(prices)
//            }
//        } catch (e: Exception) {
//            withContext(Dispatchers.Main) {
//                // Handle error
//            }
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MyApp {
//        MainScreen(Firebase.auth)
//    }
//}