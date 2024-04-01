package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.MusicViewBinding

class MusicAdapter (private val context : Context, private val musicList : ArrayList<Music>) : RecyclerView.Adapter<MusicAdapter.MyHolder> (){
    inner class MyHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val duration = binding.songDuration
        val image = binding.imageMv
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatDuration(musicList[position].duration)
        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.splach_screen).centerCrop())
            .into(holder.image)

        holder.root.setOnClickListener {
            val intent = Intent(context,PlayerActivity::class.java)

            // sending the index that is positon of click
            intent.putExtra("index",position)
            // sending the class also as, plyerActivity will recive intent from different class, it will be easy for us to filter
            intent.putExtra("class" , "MusicAdapter")
            //we just can't start the activity by calling styartActivity(intent), here we have do this
            ContextCompat.startActivity(context,intent,null)
        }

    }

}