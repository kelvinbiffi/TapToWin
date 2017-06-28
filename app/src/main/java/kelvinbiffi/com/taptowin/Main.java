package kelvinbiffi.com.taptowin;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Main extends Activity {

    TextView textRules, tvCountBegin, tvPlayer1, tvPlayer2;
    Button btnStart, btnBegin, btnReset;
    ImageButton btnPlayer1, btnPlayer2;

    Random rand = new Random();
    RelativeLayout mainBackground, layoutRules, layoutGame;
    handleFonts fonts = new handleFonts();
    handleSong songs = new handleSong();

    preferences prefs;

    MediaPlayer ninja1 = new MediaPlayer();
    MediaPlayer ninja2 = new MediaPlayer();
    MediaPlayer musicGame = new MediaPlayer();

    int player1, player2;

    private void checkViewRules(){
        if (prefs.rulesWasRead()){
            displayGameObjects();
        }else{
            setFontRules();
            setStartButton();
            layoutRules.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new preferences(this);
        mainBackground = (RelativeLayout) findViewById(R.id.mainBackground);
        layoutRules = (RelativeLayout) findViewById(R.id.layoutRules);
        layoutGame = (RelativeLayout) findViewById(R.id.layoutGame);

        //Rules components
        textRules = (TextView)findViewById(R.id.textRules);
        btnStart = (Button) findViewById(R.id.btnStart);

        //Start components
        btnReset = (Button) findViewById(R.id.btnReset);
        btnBegin = (Button) findViewById(R.id.btnBegin);
        tvCountBegin = (TextView)findViewById(R.id.tvCountBegin);

        //Game Components
        tvPlayer1 = (TextView)findViewById(R.id.tvPlayer1);
        tvPlayer2 = (TextView)findViewById(R.id.tvPlayer2);
        btnPlayer1 = (ImageButton)findViewById(R.id.btnPlayer1);
        btnPlayer2 = (ImageButton)findViewById(R.id.btnPlayer2);

        backImage();
        checkViewRules();
    }

    private void backImage(){
        int i = rand.nextInt(4);
        switch (i){
            case 0:
                mainBackground.setBackgroundResource(R.drawable.back1);
                break;
            case 1:
                mainBackground.setBackgroundResource(R.drawable.back2);
                break;
            case 2:
                mainBackground.setBackgroundResource(R.drawable.back3);
                break;
            default:
                mainBackground.setBackgroundResource(R.drawable.back4);
        }
    }

    private void setFontRules(){
        textRules.setTypeface(fonts.getFont(this));
    }

    private void setStartButton(){
        btnStart.setTypeface(fonts.getFont(this));

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.readRules();
                hideRules();
            }
        });
    }

    private void hideRules(){
        layoutRules.setVisibility(View.INVISIBLE);

        displayGameObjects();
    }

    private void displayGameObjects(){
        setBeginActions();

        setResetActions();

        setPlayerSize();
        setConfigPlayerSongs();
        setPlayerActions();

        setPlayerTextPoints();

        layoutGame.setVisibility(View.VISIBLE);
    }

    private void setResetActions(){
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideGameComponents();
                startCountDownBegin();
            }
        });
    }

    private void setBeginActions(){
        btnBegin.setTypeface(fonts.getFont(this));
        btnBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBegin.setVisibility(View.INVISIBLE);
                startCountDownBegin();
            }
        });
    }

    private void playSongCount(String song){
        try{
            AssetFileDescriptor descriptor;
            musicGame = new MediaPlayer();

            descriptor = getAssets().openFd("sounds/" + song);
            musicGame.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            musicGame.prepare();
            musicGame.setVolume(1f,1f);
            musicGame.setLooping(false);
            musicGame.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Play the song of the timer to begin the game bases on the time passed by reference
     * @param time Time reference from the timer to start the game
     */
    private void playCountSong(int time){
        switch (time){
            case 4:
                tvCountBegin.setText("3");
                playSongCount("three.mp3");
                break;
            case 3:
                tvCountBegin.setText("2");
                playSongCount("two.mp3");
                break;
            case 2:
                tvCountBegin.setText("1");
                playSongCount("one.mp3");
                break;
            case 1:
                tvCountBegin.setTextSize(100);
                tvCountBegin.setText("TAP");
                playSongCount("tap.mp3");
                break;
        }
    }

    private void setPlayerTextPoints(){
        tvPlayer1.setTypeface(fonts.getFont(this));
        tvPlayer2.setTypeface(fonts.getFont(this));
    }

    /**
     * Start the counter to start the game
     */
    private void startCountDownBegin(){
        player1 = 0;
        player2 = 0;
        tvCountBegin.setTextColor(getResources().getColor(R.color.colorTamarind));
        tvCountBegin.setVisibility(View.VISIBLE);
        tvCountBegin.setTextSize(200);
        tvCountBegin.setTypeface(fonts.getFont(this));
        new CountDownTimer(5*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int time = Integer.parseInt(new SimpleDateFormat("s").format(new Date( millisUntilFinished)));
                switch (time){
                    case 4:
                        tvCountBegin.setText("3");
                        break;
                    case 3:
                        tvCountBegin.setText("2");
                        break;
                    case 2:
                        tvCountBegin.setText("1");
                        break;
                    case 1:
                        tvCountBegin.setText("0");
                        break;
                }
                playCountSong(time);
            }

            public void onFinish() {
                playSongCount(songs.returnSong());
                tvCountBegin.setVisibility(View.INVISIBLE);
                btnReset.setVisibility(View.INVISIBLE);
                showGameComponents();
                startChallenge();
            }

        }.start();
    }

    private void startChallenge(){
        tvCountBegin.setTextColor(getResources().getColor(R.color.colorLightningYellow));
        tvCountBegin.setText("");
        tvCountBegin.setVisibility(View.VISIBLE);
        new CountDownTimer(60*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int time = Integer.parseInt(new SimpleDateFormat("ss").format(new Date( millisUntilFinished)));
                tvCountBegin.setText(String.valueOf(time));
            }

            public void onFinish() {

            }

        }.start();
    }

    private void showGameComponents(){
        btnPlayer1.setVisibility(View.VISIBLE);
        btnPlayer2.setVisibility(View.VISIBLE);
        tvPlayer1.setVisibility(View.VISIBLE);
        tvPlayer2.setVisibility(View.VISIBLE);
    }

    private void hideGameComponents(){
        btnPlayer1.setVisibility(View.INVISIBLE);
        btnPlayer2.setVisibility(View.INVISIBLE);
        tvPlayer1.setVisibility(View.INVISIBLE);
        tvPlayer2.setVisibility(View.INVISIBLE);
    }

    /**
     * Config the song to each player
     */
    private void setConfigPlayerSongs(){
        try {
            float vol = 1f;

            AssetFileDescriptor descriptor;
            descriptor = getAssets().openFd("sounds/oo.mp3");
            ninja1.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            ninja1.prepare();
            ninja1.setVolume(vol, vol);
            ninja1.setLooping(false);

            descriptor = getAssets().openFd("sounds/ya.mp3");
            ninja2.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            ninja2.prepare();
            ninja2.setVolume(vol, vol);
            ninja2.setLooping(false);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * play the tap song to each player based on pre configuration songs
     * @param player boolean to identify if the is the player 1 (true) or 2 (false)
     */
    private void playTapSong(Boolean player){
        try {
            if (player){
                if (ninja1.isPlaying()){
                    ninja1.stop();
                }else{
                    ninja1.start();
                }
            }else{
                if (ninja2.isPlaying()){
                    ninja2.stop();
                }else{
                    ninja2.start();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Set the action to each time that a player tap the screen
     */
    private void setPlayerActions(){
        btnPlayer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTapSong(true);
                player1++;
                tvPlayer1.setText(String.valueOf(player1));
            }
        });

        btnPlayer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTapSong(false);
                player2++;
                tvPlayer2.setText(String.valueOf(player2));
            }
        });
    }

    /**
     * Config the button of each player based on the screen size
     */
    private void setPlayerSize(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int size = (width/2) - (width/6);

        ViewGroup.LayoutParams params;

        params = btnPlayer1.getLayoutParams();
        params.height = size;
        params.width = size;
        btnPlayer1.setLayoutParams(params);

        params = btnPlayer2.getLayoutParams();
        params.height = size;
        params.width = size;
        btnPlayer2.setLayoutParams(params);

    }
}
