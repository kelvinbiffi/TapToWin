package kelvinbiffi.com.taptowin;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class Main extends Activity {

    TextView textRules;
    Button btnStart;
    ImageButton btnPlayer1, btnPlayer2;

    Random rand = new Random();
    RelativeLayout mainBackground, layoutRules, layoutGame;
    handleFonts fonts = new handleFonts();

    preferences prefs;

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


        backImage();
        checkViewRules();
    }

    private void backImage(){
        Bitmap bmp;
        int i = rand.nextInt(4);
        switch (i){
            case 0:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.back1);
                break;
            case 1:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.back2);
                break;
            case 2:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.back3);
                break;
            default:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.back4);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels * 3;
        int width = height + (height/2);

        bmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
        bitmapDrawable.setGravity(Gravity.CENTER|Gravity.RIGHT);
        mainBackground.setBackgroundDrawable(bitmapDrawable);

    }

    private void setFontRules(){
        textRules = (TextView)findViewById(R.id.textRules);
        textRules.setTypeface(fonts.getFont(this));
    }

    private void setStartButton(){
        btnStart = (Button) findViewById(R.id.btnStart);
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
        btnPlayer1 = (ImageButton)findViewById(R.id.btnPlayer1);
        btnPlayer2 = (ImageButton)findViewById(R.id.btnPlayer2);

        setPlayerSize();

        layoutGame.setVisibility(View.VISIBLE);
    }

    private void setPlayerSize(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int size = ((width*2)/2) - 50;

        btnPlayer1.setMinimumHeight(size);
        btnPlayer1.setMinimumWidth(size);

        btnPlayer2.setMinimumHeight(size);
        btnPlayer2.setMinimumWidth(size);

    }
}
