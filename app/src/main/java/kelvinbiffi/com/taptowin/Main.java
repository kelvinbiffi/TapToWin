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

    //Screen objects
    RelativeLayout mainBackground, layoutRules, layoutGame;
    TextView textRules, tvCountBegin, tvPlayer1, tvPlayer2, tvPlayer1Wins, tvPlayer2Wins;
    Button btnStart, btnBegin, btnReset;
    ImageButton btnPlayer1, btnPlayer2;
    ImageView winnerImage, tvShurikenPlayer1, tvShurikenPlayer2;

    //Geme utils
    Random rand = new Random();
    handleFonts fonts = new handleFonts();
    handleSong songs = new handleSong();
    preferences prefs;

    //Game medias
    MediaPlayer ninja1 = new MediaPlayer();
    MediaPlayer ninja2 = new MediaPlayer();
    MediaPlayer musicGame = new MediaPlayer();

    MediaPlayer mpTap = new MediaPlayer();
    MediaPlayer mpOne = new MediaPlayer();
    MediaPlayer mpTwo = new MediaPlayer();
    MediaPlayer mpThree = new MediaPlayer();
    MediaPlayer mpPlayer1 = new MediaPlayer();
    MediaPlayer mpPlayer2 = new MediaPlayer();
    MediaPlayer mpDraw = new MediaPlayer();

    //Game data
    CountDownTimer game;
    int player1, player2, stageGame = 4;
    int[] playerWins = {0, 0};

    /**
     * Check if the user have already read the rules or not
     */
    private void checkViewRules(){
        if (prefs.rulesWasRead()){
            configGameObjects();
            btnBegin.setVisibility(View.VISIBLE);
        }else{
            setStartButton();
            layoutRules.setVisibility(View.VISIBLE);
        }
    }

    /**
     * On create function (Native function)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new preferences(this);//Create game preferences object
        setGameEffectObjects();
        setContextObjects();
        setFonts();
        backImage();
        checkViewRules();
    }

    /**
     * Prepare game effects object
     */
    private void setGameEffectObjects(){

        try{

            /**
             * Generic media player error listener object
             */
            MediaPlayer.OnErrorListener effectErrorListener = new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    System.out.println("Effects: what-> " + String.valueOf(what) + " extra-> " + String.valueOf(what));
                    System.out.println(mp.isPlaying());
                    return false;
                }
            };

            AssetFileDescriptor descriptor;
            float vol = 1f;

            // Tap effect
            mpTap = new MediaPlayer();
            descriptor = getAssets().openFd("sounds/tap.mp3");
            mpTap.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mpTap.setVolume(vol,vol);
            mpTap.setLooping(false);
            mpTap.prepare();
            mpTap.setOnErrorListener(effectErrorListener);

            //one effect
            mpOne = new MediaPlayer();
            descriptor = getAssets().openFd("sounds/one.mp3");
            mpOne.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mpOne.setVolume(vol,vol);
            mpOne.setLooping(false);
            mpOne.prepare();
            mpOne.setOnErrorListener(effectErrorListener);

            //two effect
            mpTwo = new MediaPlayer();
            descriptor = getAssets().openFd("sounds/two.mp3");
            mpTwo.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mpTwo.setVolume(vol,vol);
            mpTwo.setLooping(false);
            mpTwo.prepare();
            mpTwo.setOnErrorListener(effectErrorListener);

            //three effect
            mpThree = new MediaPlayer();
            descriptor = getAssets().openFd("sounds/three.mp3");
            mpThree.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mpThree.setVolume(vol,vol);
            mpThree.setLooping(false);
            mpThree.prepare();
            mpThree.setOnErrorListener(effectErrorListener);

            //Player 1 victory effect
            mpPlayer1 = new MediaPlayer();
            descriptor = getAssets().openFd("sounds/player1.mp3");
            mpPlayer1.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mpPlayer1.setVolume(vol,vol);
            mpPlayer1.setLooping(false);
            mpPlayer1.prepare();
            mpPlayer1.setOnErrorListener(effectErrorListener);

            //three effect
            mpPlayer2 = new MediaPlayer();
            descriptor = getAssets().openFd("sounds/player2.mp3");
            mpPlayer2.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mpPlayer2.setVolume(vol,vol);
            mpPlayer2.setLooping(false);
            mpPlayer2.prepare();
            mpPlayer2.setOnErrorListener(effectErrorListener);

            //three effect
            mpDraw = new MediaPlayer();
            descriptor = getAssets().openFd("sounds/draw.mp3");
            mpDraw.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mpDraw.setVolume(vol,vol);
            mpDraw.setLooping(false);
            mpDraw.prepare();
            mpDraw.setOnErrorListener(effectErrorListener);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Context class object from layout objects
     */
    private void setContextObjects(){
        //Layouts components
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
        tvShurikenPlayer1 = (ImageView) findViewById(R.id.tvShurikenPlayer1);
        tvShurikenPlayer2 = (ImageView) findViewById(R.id.tvShurikenPlayer2);
        tvPlayer1 = (TextView)findViewById(R.id.tvPlayer1);
        tvPlayer2 = (TextView)findViewById(R.id.tvPlayer2);
        tvPlayer1Wins = (TextView)findViewById(R.id.tvPlayer1Wins);
        tvPlayer2Wins = (TextView)findViewById(R.id.tvPlayer2Wins);
        btnPlayer1 = (ImageButton)findViewById(R.id.btnPlayer1);
        btnPlayer2 = (ImageButton)findViewById(R.id.btnPlayer2);
    }

    /**
     * Random a image to background from 0 to 3 sum 4 options
     */
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

    /**
     * Set the game font
     *
     * Obs.: To each new text object put it here the set the font
     */
    private void setFonts(){
        textRules.setTypeface(fonts.getFont(this));
        btnStart.setTypeface(fonts.getFont(this));
        btnBegin.setTypeface(fonts.getFont(this));
        tvPlayer1.setTypeface(fonts.getFont(this));
        tvPlayer2.setTypeface(fonts.getFont(this));
        tvCountBegin.setTypeface(fonts.getFont(this));
    }

    /**
     * Set start rules button
     */
    private void setStartButton(){
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.readRules();
                layoutRules.setVisibility(View.INVISIBLE);
                btnBegin.setVisibility(View.VISIBLE);
                configGameObjects();
            }
        });
    }

    /**
     * Config the game components and show them
     */
    private void configGameObjects(){
        setBeginActions();
        setResetActions();
        setPlayerSize();
        setConfigPlayerSongs();
        setPlayerActions();
        layoutGame.setVisibility(View.VISIBLE);
    }

    /**
     * set reset actions from game object
     */
    private void setResetActions(){
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //In case of the game is running erase
                if (game != null){
                    game.cancel();
                }
                musicGame.stop();
                winnerImage.setVisibility(View.INVISIBLE);
                backImage();
                hideGameComponents();
                startCountDownBegin();
            }
        });
    }

    /**
     * Set begin action from game object
     */
    private void setBeginActions(){
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

    /**
     * Play songs based on the string passed by reference
     *
     * @param song song to be executed
     */
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

            musicGame.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

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

    /**
     * Play the song of the timer to begin the game bases on the time passed by reference
     * @param time Time reference from the timer to start the game
     */
    private void playCountSong(int time){
        switch (time){
            case 4:
                tvCountBegin.setText("3");
                if (mpThree.isPlaying())
                    mpThree.stop();
                mpThree.start();
                break;
            case 3:
                tvCountBegin.setText("2");
                if (mpTwo.isPlaying())
                    mpTwo.stop();
                mpTwo.start();
                break;
            case 2:
                tvCountBegin.setText("1");
                if (mpOne.isPlaying())
                    mpOne.stop();
                mpOne.start();
                break;
            case 1:
                tvCountBegin.setTextSize(100);
                tvCountBegin.setText("TAP");
                if (mpTap.isPlaying())
                    mpTap.stop();
                mpTap.start();
                break;
        }
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

    /**
     * Start the challenge, begin the timer based on the game already played
     */
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
                        if (mpThree.isPlaying())
                            mpThree.stop();
                        mpThree.start();
                        break;
                    case 2:
                        if (mpTwo.isPlaying())
                            mpTwo.stop();
                        mpTwo.start();
                        break;
                    case 1:
                        if (mpOne.isPlaying())
                            mpOne.stop();
                        mpOne.start();
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

    /**
     * Check who is the winner, show the iamge from it and play the song victory or draw if was the case
     */
    private void checkWinner(){
        musicGame.stop();
        hideGameComponents();
        if(player1 > player2){
            //Decrease time
            if(stageGame > 1) stageGame--;

            //Increase a win to player 1
            playerWins[0]++;
            tvPlayer1Wins.setText(String.valueOf(playerWins[0]));

            if (mpPlayer1.isPlaying())
                mpPlayer1.stop();
            mpPlayer1.start();

            //Show the player 1 image victory
            winnerImage.setImageResource(R.drawable.player1win);
        } else if(player1 < player2){
            //Decrease time
            if(stageGame > 1) stageGame--;

            //Increase a win to player 2
            playerWins[1]++;
            tvPlayer2Wins.setText(String.valueOf(playerWins[1]));

            if (mpPlayer2.isPlaying())
                mpPlayer2.stop();
            mpPlayer2.start();

            //Show the player 2 image victory
            winnerImage.setImageResource(R.drawable.player2win);
        } else {
            if (mpDraw.isPlaying())
                mpDraw.stop();
            mpDraw.start();

            //Show the draw image game
            winnerImage.setImageResource(R.drawable.draw);
        }
        winnerImage.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.VISIBLE);
    }

    /**
     * Show game components
     */
    private void showGameComponents(){
        btnReset.setVisibility(View.VISIBLE);
        btnPlayer1.setVisibility(View.VISIBLE);
        btnPlayer2.setVisibility(View.VISIBLE);
        tvPlayer1.setVisibility(View.VISIBLE);
        tvPlayer2.setVisibility(View.VISIBLE);
        tvPlayer1Wins.setVisibility(View.VISIBLE);
        tvPlayer2Wins.setVisibility(View.VISIBLE);
        tvShurikenPlayer1.setVisibility(View.VISIBLE);
        tvShurikenPlayer2.setVisibility(View.VISIBLE);
    }

    /**
     * Hide game components
     */
    private void hideGameComponents(){
        btnReset.setVisibility(View.INVISIBLE);
        btnPlayer1.setVisibility(View.INVISIBLE);
        btnPlayer2.setVisibility(View.INVISIBLE);
        tvPlayer1.setVisibility(View.INVISIBLE);
        tvPlayer2.setVisibility(View.INVISIBLE);
        tvPlayer1Wins.setVisibility(View.INVISIBLE);
        tvPlayer2Wins.setVisibility(View.INVISIBLE);
        tvShurikenPlayer1.setVisibility(View.INVISIBLE);
        tvShurikenPlayer2.setVisibility(View.INVISIBLE);
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
     * Config the button of each player and the winnerImage based on the screen size
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
