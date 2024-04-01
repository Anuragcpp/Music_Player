package com.example.musicplayer

import java.util.concurrent.TimeUnit

data class Music(
    val id : String,
    val title : String,
    val album : String,
    val artist : String,
    val duration : Long = 0,
    val path : String,
    val artUri : String
)

fun formatDuration (duration: Long) : String{
    val minute = TimeUnit.MINUTES.convert(duration,TimeUnit.MILLISECONDS)

    //the term which I am subsituting is for  - giving me the seconds remaining after the minutes are converted
    val seconds = TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS) -minute*TimeUnit.SECONDS.convert(1,TimeUnit.MINUTES)

    // %2d -> d is specifing that the value is an integer, and %2 is specifing that value can have at most 2 integers
    return String.format("%02d:%02d",minute,seconds)
}
