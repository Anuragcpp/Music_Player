package com.example.musicplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MusicPlayer)
        setContentView(R.layout.activity_playlist)
    }
}