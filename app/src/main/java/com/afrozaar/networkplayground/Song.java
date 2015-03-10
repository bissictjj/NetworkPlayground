package com.afrozaar.networkplayground;

/**
 * Created by jay on 2/26/15.
 */
public class Song {

    private long id;
    private String title;
    private String artist;


    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public Song(String songTitle, String songArtist){
        artist = songArtist;
        title = songTitle;

    }

}
