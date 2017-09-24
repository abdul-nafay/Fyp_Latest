package com.sourcey.materiallogindemo.Utility;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by Abdul Nafay Waseem on 9/24/2017.
 */

public class MemorizerUtil {

    public static void displayToast(Context context, String message)
    {
        Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    public static void hideSoftInput(View view, Context ctx)
    {try {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    catch (Exception e){}
    }

    public static void showSoftInput(Context ctx)
    {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }

}
