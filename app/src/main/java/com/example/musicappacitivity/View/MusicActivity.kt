package com.example.musicappacitivity.View

import android.app.Activity
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.musicappacitivity.Data.Song
import com.example.musicappacitivity.Network.MasterApplication
import com.example.musicappacitivity.R
import com.example.musicappacitivity.databinding.ActivityMusinBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MusicActivity : AppCompatActivity() {

    lateinit var binding: ActivityMusinBinding
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createMusic()

    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }

    fun createMusic() {
        (application as MasterApplication).service.getSongList().enqueue(
            object : Callback<ArrayList<Song>> {
                override fun onResponse(
                    call: Call<ArrayList<Song>>,
                    response: Response<ArrayList<Song>>
                ) {
                    if (response.isSuccessful) {
                        val songList = response.body()
                        val adapter = MusicAdapter(
                            songList!!,
                            LayoutInflater.from(this@MusicActivity),
                            Glide.with(this@MusicActivity)
                        )
                        binding.musicRecycler.adapter = adapter
                        binding.musicRecycler.layoutManager =
                            LinearLayoutManager(this@MusicActivity)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Song>>, t: Throwable) {

                }
            }
        )
    }

    inner class MusicAdapter(
        var songList: ArrayList<Song>,
        val inflater: LayoutInflater,
        val glide: RequestManager
    ) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView
            val thumbnail: ImageView
            val play: ImageView

            init {
                title = itemView.findViewById(R.id.song_title)
                thumbnail = itemView.findViewById(R.id.song_img)
                play = itemView.findViewById(R.id.song_play)

                play.setOnClickListener {
                    val position: Int = adapterPosition
                    val path = songList.get(position).song

                    try {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                        mediaPlayer = MediaPlayer.create(
                            this@MusicActivity,
                            Uri.parse(path)
                        )
                        mediaPlayer?.start()
                    } catch (e: Exception) {
                        Log.d("exceptionn", "error!!")
                    }
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.setText(songList.get(position).title)
            glide.load(songList.get(position).thumbnail).into(holder.thumbnail)
        }

        override fun getItemCount(): Int {
            return songList.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = inflater.inflate(R.layout.music_item_view, parent, false)
            return ViewHolder(view)
        }
    }


}