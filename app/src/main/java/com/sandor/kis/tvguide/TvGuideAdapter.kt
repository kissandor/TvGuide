package com.sandor.kis.tvguide

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sandor.kis.tvguide.data.model.ChannelData
import com.sandor.kis.tvguide.data.model.Event
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class TvGuideAdapter (private val dataset : ArrayList<ChannelData>) : RecyclerView.Adapter<TvGuideAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textView : TextView
        val textViewChannelName : TextView
        val textViewNextProgram : TextView
        val imageView: ImageView
        val progressBar: ProgressBar
        init{
            textView = view.findViewById(R.id.textView)
            textViewChannelName = view.findViewById(R.id.textViewChannelName)
            textViewNextProgram = view.findViewById(R.id.textViewNextProgram)
            imageView = view.findViewById(R.id.imageView)
            progressBar = view.findViewById(R.id.progressBar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.channel_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
/*
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val currentTime = System.currentTimeMillis() / 1000 // Aktuális idő másodpercben
            val closestEventIndex = getNextEventIndex(dataset[position].event, currentTime)
            holder.textViewChannelName.text = dataset[position].channelName
            holder.textView.text = convertTimestampToDate(dataset[position].event[closestEventIndex].startTime.toLong()) + ": "+ dataset[position].event[closestEventIndex].name
            holder.textViewNextProgram.text = convertTimestampToDate(dataset[position].event[closestEventIndex+1].startTime.toLong()) + ": "+ dataset[position].event[closestEventIndex+1].name
            holder.progressBar.progress = (((currentTime - dataset[position].event[closestEventIndex+1].startTime.toLong() ) /
                    (dataset[position].event[closestEventIndex+1].startTime.toLong() - dataset[position].event[closestEventIndex].startTime.toLong())) * 100).toInt()

            Glide.with(holder.itemView.context)
                .load(dataset[position].logoUrl)
                .into(holder.imageView)
        } catch (e: Exception){
            Log.d("MainActivity", "Position: $position, Channel Name: ${dataset[position].channelName}")
        }
    }

*/
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val currentTime = System.currentTimeMillis() / 1000 // Aktuális idő másodpercben
            val closestEventIndex = getNextEventIndex(dataset[position].event, currentTime)

            // Ha nincs érvényes esemény, állítsuk alapállapotba
            if (closestEventIndex == -1 || closestEventIndex + 1 >= dataset[position].event.size) {
                holder.progressBar.progress = 0
                holder.textView.text = "No current program"
                holder.textViewNextProgram.text = "No upcoming program"
                return
            }

            val currentEvent = dataset[position].event[closestEventIndex]
            val nextEvent = dataset[position].event[closestEventIndex + 1]

            holder.textViewChannelName.text = dataset[position].channelName
            holder.textView.text = "${convertTimestampToDate(currentEvent.startTime.toLong())}: ${currentEvent.name}"
            holder.textViewNextProgram.text = "${convertTimestampToDate(nextEvent.startTime.toLong())}: ${nextEvent.name}"

            //ProgressBar kiszámítása
            val eventStartTime = currentEvent.startTime.toLong()
            val eventEndTime = nextEvent.startTime.toLong()
            val elapsedTime = currentTime - eventStartTime
            val totalEventDuration = eventEndTime - eventStartTime
            val progress = ((elapsedTime.toDouble() / totalEventDuration) * 100).toInt()
            holder.progressBar.progress = progress.coerceIn(0, 100)


            // Kép betöltése Glide segítségével
            Glide.with(holder.itemView.context)
                .load(dataset[position].logoUrl)
              //.placeholder(R.drawable.placeholder_image)
                // .error(R.drawable.error_image)
                .into(holder.imageView)

        } catch (e: Exception) {
            Log.e("TvGuideAdapter", "Error in onBindViewHolder: ${e.message}")
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimestampToDate(timestamp: Long): String {
        // Létrehozzuk az Instant objektumot a timestamp alapján
        val instant = Instant.ofEpochSecond(timestamp)

        // UTC időzóna beállítása
        val zonedDateTime = instant.atZone(ZoneId.of("UTC"))

        // Formázzuk a dátumot az olvasható formátumra
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return zonedDateTime.format(formatter)
    }

    fun getNextEventIndex(events: List<Event>, currentTime: Long): Int {
        // Szűrjük ki azokat az eseményeket, amelyeknek a startTime-ja kisebb mint az aktuális idő
        val pastEvents = events.filter { it.startTime < currentTime }

        val closestEvent = pastEvents.minByOrNull { Math.abs(it.startTime - currentTime) }
        // Ha van ilyen esemény, keressük meg a legközelebbit
        return events.indexOf(closestEvent)
    }
}