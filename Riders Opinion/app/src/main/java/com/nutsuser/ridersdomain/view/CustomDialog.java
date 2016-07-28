package com.nutsuser.ridersdomain.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;

/**
 * Created by admin on 15-03-2016.
 */
public class CustomDialog {

    public static void showProgressDialog(Activity context,String message) {
        new AlertDialog.Builder(context).setTitle("Message")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();


//        // custom dialog
//        dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(R.layout.custom_dialog_layout);
//
//      //  dialog.getWindow().
//
//        // set the custom dialog components - text, image and button
//        TextView textTitle = (TextView) dialog.findViewById(R.id.txtDialogHeader);
//        textTitle.setText("Message");
//        TextView textMessage = (TextView) dialog.findViewById(R.id.txtDialogMessage);
//        textMessage.setText(message);
//        TextView txtDialogOk = (TextView) dialog.findViewById(R.id.txtDialogOk);
//
//        // if button is clicked, close the custom dialog
//        txtDialogOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
    }

    public static void JoinedshowProgressDialog(Context context,String message) {
        new AlertDialog.Builder(context).setTitle("Joined")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();


//        // custom dialog
//        dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        dialog.setContentView(R.layout.custom_dialog_layout);
//        //  dialog.getWindow().
//
//        // set the custom dialog components - text, image and button
//        TextView textTitle = (TextView) dialog.findViewById(R.id.txtDialogHeader);
//        textTitle.setText("Joined");
//        TextView textMessage = (TextView) dialog.findViewById(R.id.txtDialogMessage);
//        textMessage.setText(message);
//        TextView txtDialogOk = (TextView) dialog.findViewById(R.id.txtDialogOk);
//
//        // if button is clicked, close the custom dialog
//        txtDialogOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
    }

    public static void FavshowProgressDialog(Context context,String message) {
        new AlertDialog.Builder(context).setTitle("Favourite")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                }).show();

//        // custom dialog
//        dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(R.layout.custom_dialog_layout);
//        //  dialog.getWindow().
//
//        // set the custom dialog components - text, image and button
//        TextView textTitle = (TextView) dialog.findViewById(R.id.txtDialogHeader);
//        textTitle.setText("Favourite");
//        TextView textMessage = (TextView) dialog.findViewById(R.id.txtDialogMessage);
//        textMessage.setText(message);
//        TextView txtDialogOk = (TextView) dialog.findViewById(R.id.txtDialogOk);
//
//        // if button is clicked, close the custom dialog
//        txtDialogOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
    }

//    public  static  void dismisDialog(){
//        if (dialog != null) {
//            dialog.dismiss();
//            dialog = null;
//        }
//    }
}
