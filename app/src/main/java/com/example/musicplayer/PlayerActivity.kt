package com.example.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musicplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    companion object{
        lateinit var musicListPA : ArrayList<Music>
        var songPosition : Int = 0
        var mediaPlayer : MediaPlayer? = null
        var isPlaying : Boolean = false
    }

    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MusicPlayer)

        binding = ActivityPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        // now initializing the layout with function
        initializeLayout()

        //logic for the play pause btn
        binding.playPauseBtn.setOnClickListener {
            if (isPlaying) pauseMusic()
            else playMusic()
        }

        binding.previousBtnPA.setOnClickListener { prevNextSong(false) }
        binding.nextBtnPA.setOnClickListener {  prevNextSong(true)}

    }

    private fun setLayout(){
        Glide.with(this)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.splach_screen).centerCrop())
            .into(binding.songImgaePA)

        binding.songNamePA.text = musicListPA[songPosition].title
    }

    private fun createMediaPlayer(){
        try {

            //now the logic for the music to play, using the prebuild class MediaPlayer
            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer()
                // is the mediaPlayer object was empty initially than we are initializing it with the MediaPlayer()
            }

            //now if the mediaPlayer is not null then we will reset it,
            mediaPlayer!!.reset()

            //now we are getting the path to get the selected music
            mediaPlayer!!.setDataSource(musicListPA[songPosition].path)

            //now we are setting the mediaPlayer to play the music
            mediaPlayer!!.prepare()

            //playing the song
            mediaPlayer!!.start()

            //seting the isPlaying to true and playPauseBtn to Pause icon
            isPlaying = true
            binding.playPauseBtn.setIconResource(R.drawable.pause_icon)

        }catch (e : Exception) {return}
    }

    private fun initializeLayout(){
        // getting the value passed from the musicAdapter
        songPosition = intent.getIntExtra("index",0)

        // now I'm filtering the intent with the class it's send from, if the intent came form the musicAdapter and its name is "class"
        when(intent.getStringExtra("class")){
            "MusicAdapter" ->{
                musicListPA = ArrayList()

                // all the songs which are there in the mainActivity musicListMA now they are also in this list
                musicListPA.addAll(MainActivity.MusicListMA)

                //setting the layout of the music player activity using a function
                setLayout()
                createMediaPlayer()

            }
        }
    }

    //function for play the music
    private fun playMusic(){
        mediaPlayer!!.start()
        isPlaying = true
        binding.playPauseBtn.setIconResource(R.drawable.pause_icon)
    }

    //funciton for pause the music
    private fun pauseMusic(){
        mediaPlayer!!.pause()
        isPlaying = false
        binding.playPauseBtn.setIconResource(R.drawable.play_icon)
    }

    // function for next and previous songs
    private fun prevNextSong(increment : Boolean) {
        if(increment){
            setSongPosition(true)
            setLayout()
            createMediaPlayer()
        }else{
            setSongPosition(false)
            setLayout()
            createMediaPlayer()
        }
    }

    /// function for position the songPosition , as if the position is last  then ++ must take it to first and vice_varsa
    private fun setSongPosition(increment: Boolean) {
        if(increment){
            if (songPosition == musicListPA.size-1){
                songPosition = 0
            }else{
                ++songPosition
            }
        }else{
            if(songPosition == 0){
                songPosition = musicListPA.size -1
            }else{
                --songPosition
            }
        }
    }

}