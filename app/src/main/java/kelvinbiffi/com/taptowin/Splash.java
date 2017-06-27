package kelvinbiffi.com.taptowin;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class Splash extends Activity {

    Button btnPlay;
    MediaPlayer ninja;
    handleSong songs = new handleSong();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        configButton();

        playSong();
    }

    private void configButton(){
        btnPlay = (Button) findViewById(R.id.btnPlay);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/orchidee_medium.ttf");
        btnPlay.setTypeface(font);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGame();
            }
        });
    }

    private void openGame(){
        try {
            ninja.stop();
            ninja = new MediaPlayer();

            AssetFileDescriptor descriptor = getAssets().openFd("sounds/" + songs.returnTapSong());
            ninja.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            ninja.prepare();
            ninja.setVolume(1f,1f);
            ninja.setLooping(false);
            ninja.start();

            Intent game = new Intent(Splash.this, Main.class);
            startActivity(game);
            finish();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void playSong(){
        try {
            ninja = new MediaPlayer();

            AssetFileDescriptor descriptor = getAssets().openFd("sounds/" + songs.returnSong());
            ninja.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            ninja.prepare();
            ninja.setVolume(1f,1f);
            ninja.setLooping(true);
            ninja.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
