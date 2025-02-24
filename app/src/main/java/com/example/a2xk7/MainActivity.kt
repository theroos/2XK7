package com.example.a2xk7

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.SupabaseClientBuilder
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        checkSupabaseConnection(this)
    }

    private fun getData(){
        lifecycleScope.launch {
            val client = getClient()
            //val supabaseResponse = client.postgrest["cities"].select()
        }
    }

    object SupabaseClientManager{
        private const val SUPABASE_URL = "https://aaukmjyjnmgsbgrtwzho.supabase.co"
        private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFhdWttanlqbm1nc2JncnR3emhvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzY2OTY4MTMsImV4cCI6MjA1MjI3MjgxM30.R6Y-P3L2LAwf9UtmolyRI2tUfwO0zpQvlT6HARO8r6Y"

        //val client = SupabaseClient(SUPABASE_URL, SUPABASE_KEY)

        val client: SupabaseClient = createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY
        ) {
            install(Postgrest)
        }

        suspend fun getUsers(): List<Cities> {
            return withContext(Dispatchers.IO) {
                try {
                    client.postgrest["cities"].select().decodeList<Cities>()
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }

    }

    private fun getClient(){
        val client = createSupabaseClient(
            supabaseUrl = "https://aaukmjyjnmgsbgrtwzho.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFhdWttanlqbm1nc2JncnR3emhvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzY2OTY4MTMsImV4cCI6MjA1MjI3MjgxM30.R6Y-P3L2LAwf9UtmolyRI2tUfwO0zpQvlT6HARO8r6Y"
        ){
            install(Postgrest)
        }
    }

    fun checkSupabaseConnection(context : Context){

        val client = HttpClient()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: HttpResponse =
                    client.get("https://aaukmjyjnmgsbgrtwzho.supabase.co")
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"Supabase Connection Successful!",Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"Supabase Connection is not Successful!",Toast.LENGTH_LONG).show()
                }
            } finally {
                client.close()
            }
        }
    }
}

data class Cities(
    val num: Int,
    val cities: String,
    val states: String
)