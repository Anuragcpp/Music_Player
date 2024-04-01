package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media.ALBUM
import android.provider.MediaStore.Audio.Media.ALBUM_ID
import android.provider.MediaStore.Audio.Media.ARTIST
import android.provider.MediaStore.Audio.Media.DATA
import android.provider.MediaStore.Audio.Media.DATE_ADDED
import android.provider.MediaStore.Audio.Media.DURATION
import android.provider.MediaStore.Audio.Media.IS_MUSIC
import android.provider.MediaStore.Audio.Media.TITLE
import android.provider.MediaStore.Audio.Media._ID
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var shuffleBtn : Button
    private lateinit var favoriteBtn : Button
    private lateinit var playListBtn : Button
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView : NavigationView
    private lateinit var musicAdapter: MusicAdapter


    companion object{
        lateinit var MusicListMA : ArrayList<Music>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        init()




        shuffleBtn.setOnClickListener {
            val intent = Intent(this,PlayerActivity::class.java).setAction("your.custom.action")
            startActivity(intent)
        }

        playListBtn.setOnClickListener {
            val intent = Intent(this,PlaylistActivity::class.java).setAction("your.custom.action")
            startActivity(intent)
        }

        favoriteBtn.setOnClickListener {
            val intent = Intent(this,FavoriteActivity::class.java).setAction("your.custom.action")
            startActivity(intent)
        }

        navView.setNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navFeedback -> Toast.makeText(this,"FeedBack",Toast.LENGTH_SHORT).show()

                R.id.navAbout -> Toast.makeText(this,"About",Toast.LENGTH_SHORT).show()

                R.id.navSetting -> Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show()

                R.id.navFeedback -> exitProcess(1)
            }

            // need to return true otherwise error
            true
        }
    }

    fun init(){

        requestRuntimePermission()
        setTheme(R.style.Theme_MusicPlayer)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        shuffleBtn = binding.shuffleBtn
        favoriteBtn =binding.favoriteBtn
        playListBtn = binding.playlistBtn
        navView = binding.navView

        //code for recycler view
//        MusicListMA = getAllAudio()
        MusicListMA = getAllAudio()

//        val musicList = ArrayList<String>()
//        musicList.add("gana 1")
//        musicList.add("gana 2")
//        musicList.add("gana 3")
//        musicList.add("gana 4")
//        musicList.add("gana 5")
//        musicList.add("gana 6")
//        musicList.add("gana 7")


        //it will make the number of object it requierd
//        binding.musicRV.apply {
//            setHasFixedSize(true)
//            setItemViewCacheSize(13)
//            layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
//            musicAdapter = MusicAdapter(this@MainActivity,MusicListMA)
//        }
//        lifecycleScope.apply {
//
//        }
        binding.musicRV.setHasFixedSize(true)
        binding.musicRV.setItemViewCacheSize(13)
        binding.musicRV.layoutManager = LinearLayoutManager(this)
        musicAdapter = MusicAdapter(this, MusicListMA)
        binding.musicRV.adapter = musicAdapter
        binding.totalSongs.text = "Total Songs : ${musicAdapter.itemCount}"


        //for navigation drawyer
        // Find the Toolbar view by its ID
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        // Set the Toolbar as the support action bar
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this,binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.drawer_icon)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    //request permission
    private fun requestRuntimePermission(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
        }

    }


    /// with read external storage
//    private fun requestRuntimePermission(){
//        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),13)
//        }
//
//    }


    //has to override this prebuuild function to get the permession
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if(requestCode == 13){
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this,"Permission granted" , Toast.LENGTH_SHORT).show()
//            }else{
//                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),13)
//            }
//        }
//    }



    //with read external storage
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if(requestCode == 13){
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this,"Permission granted" , Toast.LENGTH_SHORT).show()
//            }else{
//                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),13)
//            }
//        }
//    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Recycle", "Range")
    private fun getAllAudio () : ArrayList<Music>{
        val tempList = ArrayList<Music>()

        val selection = "$IS_MUSIC!=0"
        val projection = arrayOf(
            _ID,
            TITLE,
            ALBUM,
            ARTIST,
            DURATION,
            DATE_ADDED,
            DATA,
            ALBUM_ID
        )

//        val cursor: Cursor? = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,
//            DATE_ADDED + "DESC",null
//        )

        val cursor: Cursor? = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,
            null,null
        )

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(TITLE))
                    val idC = cursor.getString(cursor.getColumnIndex(_ID))
                    val albumC = cursor.getString(cursor.getColumnIndex(ALBUM))
                    val artistC = cursor.getString(cursor.getColumnIndex(ARTIST))
                    val pathC = cursor.getString(cursor.getColumnIndex(DATA))
                    val durationC = cursor.getLong(cursor.getColumnIndex(DURATION))
                    val albumIdC = cursor.getLong(cursor.getColumnIndex(ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri,albumIdC).toString()

                    val music = Music(
                        id = idC,
                        title = titleC,
                        album = albumC,
                        artist = artistC,
                        duration = durationC,
                        path = pathC,
                        artUri = artUriC
                        )

                    val file = File(music.path)
                    if(file.exists()){
                        tempList.add(music)
                    }

                }while (cursor.moveToNext())

                cursor.close()
            }
        }

        return tempList
    }


}