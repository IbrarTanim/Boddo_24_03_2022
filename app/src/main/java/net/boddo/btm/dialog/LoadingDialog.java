package net.boddo.btm.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import net.boddo.btm.R;

import java.util.Objects;

public class LoadingDialog {

    Dialog dialog;
    private Context context;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    public void showDialog() {
        dialog = new Dialog(context);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loading_animation_google_search);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    public void hideDialog() {
        if (dialog !=null)
        dialog.hide();
    }
}