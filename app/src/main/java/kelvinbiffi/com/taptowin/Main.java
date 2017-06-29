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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Main extends Activity {

    TextView textRules, tvCountBegin, tvPlayer1, tvPlayer2;
    Button btnStart, btnBegin, btnReset;
    ImageButton btnPlayer1, btnPlayer2;
    ImageView winnerImage;

    Random rand = new Random();
    RelativeLayout mainBackground, layoutRules, layoutGame;
    handleFonts fonts = new handleFonts();
    handleSong songs = new handleSong();

    preferences prefs;

    MediaPlayer ninja1 = new MediaPlayer();
    MediaPlayer ninja2 = new MediaPlayer();
    MediaPlayer musicGame = new MediaPlayer();
    MediaPlayer effects = new MediaPlayer();

    CountDownTimer game;

    int player1, player2, stageGame = 3;

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
        winnerImage = (ImageView) findViewById(R.id.winnerImage);
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
                if (game != null){
                    game.cancel();
                }
                musicGame.stop();
                winnerImage.setVisibility(View.INVISIBLE);
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
                winnerImage.setVisibility(View.INVISIBLE);
                hideGameComponents();
                startCountDownBegin();
            }
        });
    }

    private void playSong(String song){
        try{
            AssetFileDescriptor descriptor;
            if (musicGame != null && musicGame.isPlaying()){
                musicGame.stop();
                musicGame.release();
            }
            musicGame = new MediaPlayer();

            descriptor = getAssets().openFd("sounds/" + song);
            musicGame.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            musicGame.setVolume(1f,1f);
            musicGame.setLooping(false);
            musicGame.prepare();
            musicGame.start();

            musicGame.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    System.out.println("Music: what-> " + String.valueOf(what) + " extra-> " + String.valueOf(what));
                    System.out.println(mp.isPlaying());
                    return false;
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void playSongEffect(String song){
        try{
            AssetFileDescriptor descriptor;
            if (effects != null && effects.isPlaying()){
                effects.stop();
                effects.release();
            }
            effects = new MediaPlayer();

            descriptor = getAssets().openFd("sounds/" + song);
            effects.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            effects.setVolume(1f,1f);
            effects.setLooping(false);
            effects.prepare();
            effects.start();

            effects.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    System.out.println("Effects: what-> " + String.valueOf(what) + " extra-> " + String.valueOf(what));
                    System.out.println(mp.isPlaying());
                    return false;
                }
            });

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
                playSongEffect("three.mp3");
                break;
            case 3:
                tvCountBegin.setText("2");
                playSongEffect("two.mp3");
                break;
            case 2:
                tvCountBegin.setText("1");
                playSongEffect("one.mp3");
                break;
            case 1:
                tvCountBegin.setTextSize(100);
                tvCountBegin.setText("TAP");
                playSongEffect("tap.mp3");
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
                tvCountBegin.setVisibility(View.INVISIBLE);
                btnReset.setVisibility(View.INVISIBLE);
                showGameComponents();
                startChallenge();
            }

        }.start();

    }

    private void startChallenge(){
        player1 = 0;
        tvPlayer1.setText("0");
        player2 = 0;
        tvPlayer2.setText("0");

        tvCountBegin.setTextColor(getResources().getColor(R.color.colorGoldenBell));
        tvCountBegin.setText("");
        tvCountBegin.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.VISIBLE);

        int gameTime = (stageGame *5) + 1;

        playSong(songs.returnSong());

        game = new CountDownTimer(gameTime*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int time = Integer.parseInt(new SimpleDateFormat("ss").format(new Date( millisUntilFinished)));
                tvCountBegin.setText(String.valueOf(time));
                switch (time){
                    case 3:
                        tvCountBegin.setTextSize(200);
                        tvCountBegin.setTextColor(getResources().getColor(R.color.colorCrimson));
                        playSongEffect("three.mp3");
                        break;
                    case 2:
                        playSongEffect("two.mp3");
                        break;
                    case 1:
                        playSongEffect("one.mp3");
                        break;
                }
            }

            public void onFinish() {
                tvCountBegin.setVisibility(View.INVISIBLE);
                tvCountBegin.setText("");
                checkWinner();
            }

        };
        game.start();
    }

    private void checkWinner(){
        musicGame.stop();
        hideGameComponents();
        if(player1 > player2){
            if(stageGame > 1) stageGame--;
            playSongEffect("player1.mp3");
            winnerImage.setImageResource(R.drawable.player1);
        } else if(player1 < player2){
            if(stageGame > 1) stageGame--;
            playSongEffect("player2.mp3");
            winnerImage.setImageResource(R.drawable.player2);
        } else {
            playSongEffect("draw.mp3");
            winnerImage.setImageResource(R.drawable.draw);
        }
        winnerImage.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.VISIBLE);
    }

    private void showGameComponents(){
        btnReset.setVisibility(View.VISIBLE);
        btnPlayer1.setVisibility(View.VISIBLE);
        btnPlayer2.setVisibility(View.VISIBLE);
        tvPlayer1.setVisibility(View.VISIBLE);
        tvPlayer2.setVisibility(View.VISIBLE);
    }

    private void hideGameComponents(){
        btnReset.setVisibility(View.INVISIBLE);
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
                if(!ninja1.isPlaying()) {
                    ninja1.start();
                }
            }else{
                if(!ninja2.isPlaying()) {
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

        params = winnerImage.getLayoutParams();
        params.height = (width/2);
        params.width = (width/2);
        winnerImage.setLayoutParams(params);

    }
}
