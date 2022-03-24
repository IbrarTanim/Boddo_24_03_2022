package net.boddo.btm.Utills;

import android.content.Context;
import android.content.Intent;

import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.R;
import net.boddo.btm.interfaces.ActivityCloseListener;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class DialogPretty {
    private Context context;
    private PrettyDialog dialog;
    public DialogPretty(Context context){
        this.context = context;
        dialog = new PrettyDialog(context);
    }
    private ActivityCloseListener listener;
    public void setWantToCloseListener(ActivityCloseListener listener){
        this.listener = listener;
    }
    public void showDialog(String title,String message){

        dialog.setTitle(title)
                .setTitleColor(R.color.pdlg_color_blue)
                .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                .setMessage("Discover many peoples from around the world!\n" +
                        "\n" +
                        "Chat with thousands of cool peoples who chatting in global chat room from around the world\n" +
                        "\n" +
                        "Cost of joining global chat room 20 credits")
                .setMessageColor(R.color.pdlg_color_black)
                .setAnimationEnabled(true)
                .addButton(
                        "Buy Credits",                    // button text
                        R.color.pdlg_color_white,        // button text color
                        R.color.colorPrimary,        // button background color
                        new PrettyDialogCallback() {        // button OnClick listener
                            @Override
                            public void onClick() {
                                Intent intent = new Intent(context, BuyCreditActivity.class);
                                context.startActivity(intent);
                                dialog.dismiss();
                                if(listener != null)
                                    listener.onActivityClose();
                            }
                        }
                ).addButton("Not Now", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        dialog.dismiss();
                        if(listener != null)
                            listener.onActivityClose();

                    }
                }
        ).show();
    }
}
