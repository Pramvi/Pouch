package com.pramvi.pouch.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by mrsinghania on 16/4/16.
 */
public class DialogUtils {
    public static void displayErrorDialog(Context context,int tit,int mess)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        String message=context.getString(mess);
        String title=context.getString(tit);
        alertDialog.setMessage(message);
        alertDialog.setTitle(title);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
