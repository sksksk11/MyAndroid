package com.example.mymusic.myimplements;

import com.example.mymusic.data.Song;

public class musicImpl {

    public interface onSongChangeListener{

        void onSongChange(Song song);  //变更歌曲时，传出新歌曲信息

    }

}
