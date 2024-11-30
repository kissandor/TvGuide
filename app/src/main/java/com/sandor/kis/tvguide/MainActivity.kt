package com.sandor.kis.tvguide

import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sandor.kis.tvguide.data.model.TVChannel
import com.sandor.kis.tvguide.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var adapter: TvGuideAdapter
    private lateinit var recyclerView: RecyclerView
    private val list: ArrayList<TVChannel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // WindowInsets kezelés
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Toolbar inicializálása
        toolbar = findViewById(R.id.topToolBar)
        setSupportActionBar(toolbar)

        // RecyclerView adapter inicializálása
        recyclerView = findViewById(R.id.recyclerView)
        adapter = TvGuideAdapter(list)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Hálózati hívás indítása
       fetchChannels()
   }

    private fun fetchChannels() {
        lifecycleScope.launch(Dispatchers.IO) { // IO szálon
            try {
                // Hálózati hívás
                val response = RetrofitClient.retrofit.getChannels()

                if (response.isSuccessful) {

                    val tvChannelResponse  = response.body()
                    if (tvChannelResponse  != null) {
                        // UI frissítése a fő szálon
                        launch(Dispatchers.Main) {
                            list.clear() // Megakadályozza a régi adatok megmaradását
                            for (channel in tvChannelResponse) {
                                list.add(channel)
                            }
                            adapter.notifyDataSetChanged() // Adapter frissítése
                        }
                    } else {
                        Log.e("API_ERROR", "Response body is null")
                    }
                } else {
                    Log.e("API_ERROR", "Failed to fetch channels: ${response.code()}")
                }
            } catch (e: IOException) {
                Log.e("API_ERROR", "IOException Failed to fetch channels", e)
            } catch (e: HttpException) {
                Log.e("API_ERROR", "HttpException Failed to fetch channels", e)
            }
        }
    }
}
