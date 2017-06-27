package kelvinbiffi.com.taptowin;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

public class Splash extends Activity {

    Button btnPlay;
    MediaPlayer ninja;

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
    }

    private void playSong(){
        try {
            ninja = new MediaPlayer();

            AssetFileDescriptor descriptor = getAssets().openFd("sounds/ninja_star_inn.mp3");
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
