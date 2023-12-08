package com.ingilizceevi.wordsinorder

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.widget.Toast
import java.io.IOException


class SoundPlayer(c: Context) { private val context = c
    var mMediaPlayer : MediaPlayer? = null
    private var playIndex = 0
    private lateinit var sounds : Uri

    fun setupSoundSequence(s:MutableList<Uri>){

        // sounds = s
        playIndex=0
    }
    fun startSound(s:Uri):MediaPlayer {
        if(mMediaPlayer!=null) {
            if (mMediaPlayer!!.isPlaying) {
                mMediaPlayer!!.stop()
            }
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        mMediaPlayer = MediaPlayer()
        mMediaPlayer!!.setDataSource(context, s)
        mMediaPlayer!!.prepare()
        mMediaPlayer!!.setOnCompletionListener(completionListener)
        return mMediaPlayer!!
    }

    val completionListener = OnCompletionListener { mediaPlayer ->
//        if (playIndex < sounds.size) {
//            try {
//                startSound()
//            } catch (e: IOException) { }
//        } else { }
    }
}