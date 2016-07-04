package com.maudit.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.maudit.database.DataBaseAdpter;
import com.maudit.main.LoginActivity;
import com.maudit.main.R;
import com.maudit.model.MediaModel;
import com.maudit.model.QuestionsModel;

import java.util.List;

/**
 * Created by Mohankumar on 1/4/2016.
 */
public class CommonMethod {
    Context mContext;
    private Session mSession;
    private DataBaseAdpter mDataBaseAdpter;

    public CommonMethod(Context context){
        mContext=context;
        mDataBaseAdpter=new DataBaseAdpter(context);
    }

    public void alertDialog(String message, final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);
        closeKeyPad(view);
        // set title
        alertDialogBuilder.setTitle(mContext.getResources().getString(R.string.title));
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        if (view != null) {
                            view.requestFocus();
                            openKeyPad(view);
                        }
                    }
                });
                /*.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
*/
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    public void alertDialogMessage(final String message, final Context context,final Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);
        // set title
        alertDialogBuilder.setTitle(mContext.getResources().getString(R.string.title));
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        Log.i("class name-->", "" + mContext.getClass().getSimpleName());
                        if(mContext.getClass().getSimpleName().equals("SectionListActivity")){
                            mDataBaseAdpter.deleteSection(0);
                            if(message.equalsIgnoreCase("Do you want to logout?")){
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                activity.finish();
                            }else {
                                activity.finish();
                            }
                        }else{
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            activity.finish();
                        }

                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    //Opening device Keypad
    public void openKeyPad(View view) {
        if(view!=null){
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    //Close device Keypad
    public void closeKeyPad(View view) {
        if(view!=null){
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    view.getWindowToken(), 0);
        }

    }
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
    public void alertRemoveImage(String message, final Context context,QuestionsModel questionsModel,MediaModel mediaModel) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                mContext);
        // set title
        alertDialogBuilder.setTitle(mContext.getResources().getString(R.string.title));
        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing

                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
