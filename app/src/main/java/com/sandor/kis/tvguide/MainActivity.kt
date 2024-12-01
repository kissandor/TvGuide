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
import com.sandor.kis.tvguide.data.model.ChannelData
import com.sandor.kis.tvguide.data.model.TVChannel
import com.sandor.kis.tvguide.network.RetrofitClient
import com.sandor.kis.tvguide.repository.TVChannelRepo
import com.sandor.kis.tvguide.repository.TVChannelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var adapter: TvGuideAdapter
    private lateinit var recyclerView: RecyclerView
    private var channelList: ArrayList<TVChannel> = ArrayList()
    private var channelDataList: List<List<ChannelData>> = emptyList()


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
        adapter = TvGuideAdapter(channelList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Hálózati hívás indítása
        fetchChannels()
        //fetchAllChannelData()
        //testFetch()
    }

    private fun fetchChannels() {
        lifecycleScope.launch(Dispatchers.IO) { // IO szálon
         /*   try {
                // Hálózati hívás
                val response = RetrofitClient.retrofit.getChannels()

                if (response.isSuccessful) {

                    val tvChannelResponse = response.body()
                    if (tvChannelResponse != null) {
                        // UI frissítése a fő szálon
                        launch(Dispatchers.Main) {
                            channelList.clear() // Megakadályozza a régi adatok megmaradását
                            /*for (channel in tvChannelResponse) {
                                channelList.add(channel)
                            }*/
                            channelList.addAll(tvChannelResponse)

                            adapter.notifyDataSetChanged() // Adapter frissítése
                        }
                    } else {
                        Log.e("API_ERROR", "Response body is null")
                    }
                } else {
                    Log.e("API_ERROR", "Failed to fetch channels: ${response.code()}")
                }*/
            try{
                var list = TVChannelRepo(RetrofitClient.retrofit).fetchAllChannels()

                launch(Dispatchers.Main) {
                    channelList.clear() // Megakadályozza a régi adatok megmaradását

                    if (list.isNotEmpty()) {
                        channelList.addAll(list)
                    } else {
                        // Kezelés, ha a lista üres
                        Log.w("API_WARNING", "No channels found.")
                    }

                    adapter.notifyDataSetChanged() // Adapter frissítése
                }

            } catch (e: IOException) {
                Log.e("API_ERROR", "IOException Failed to fetch channels", e)
            } catch (e: HttpException) {
                Log.e("API_ERROR", "HttpException Failed to fetch channels", e)
            }
        }
    }

    private fun fetchAllChannelData() {
        lifecycleScope.launch(Dispatchers.IO) { // IO szálon
            try {
                // Hálózati hívás
                val response = RetrofitClient.retrofit.getChannelData(562)

                if (response.isSuccessful) {

                    val channelDataResponse = response.body()
                    if (channelDataResponse != null) {


                        //channelDataList.clear() // Megakadályozza a régi adatok megmaradását

                          //  channelDataList.addAll(channelDataResponse)


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

    private fun testFetch(){
        lifecycleScope.launch(Dispatchers.IO) { // IO szálon
            try {
                channelDataList = TVChannelRepository(RetrofitClient.retrofit).fetchAllChannelData()
            }catch (e: Exception){}

        }
    }
}