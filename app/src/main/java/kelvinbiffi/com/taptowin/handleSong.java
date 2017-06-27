package kelvinbiffi.com.taptowin;

import java.util.Random;

/**
 * Created by User on 27/06/2017.
 */

public class handleSong {

    Random rand = new Random();

    public String returnTapSong(){
        int i = rand.nextInt(2);
        return (i == 0 ? "yaoo.mp3" : "wooo.mp3");
    }

    public String returnSong(){
        int i = rand.nextInt(4);
        String song;
        switch (i){
            case 0:
                song = "ninja_star_inn.mp3";
                break;
            case 1:
                song = "ninja_master.mp3";
                break;
            case 2:
                song = "samurai_warrior.mp3";
                break;
            default:
                song = "shogun.mp3";
        }
        return song;
    }

}
