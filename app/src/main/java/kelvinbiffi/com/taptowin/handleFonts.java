package kelvinbiffi.com.taptowin;

import android.app.Activity;
import android.graphics.Typeface;

/**
 * Created by User on 27/06/2017.
 */

public class handleFonts {

    public Typeface getFont(Activity activity){
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/orchidee_medium.ttf");
        return font;
    }

}
