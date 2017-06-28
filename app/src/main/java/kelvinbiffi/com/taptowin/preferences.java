package kelvinbiffi.com.taptowin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 27/06/2017.
 */

public class preferences {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "tapToWinPreferences";
    public static final String KEY_RULES = "viewedRules";

    // Constructor
    public preferences(Context context){
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void readRules(){
        editor.putBoolean(KEY_RULES, true);

        // commit changes
        editor.commit();
    }

    public boolean rulesWasRead(){
        return sharedPreferences.getBoolean(KEY_RULES, false);
    }

}
