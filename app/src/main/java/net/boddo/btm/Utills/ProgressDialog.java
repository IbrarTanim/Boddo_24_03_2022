package net.boddo.btm.Utills;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.appcompat.widget.AppCompatImageView;

import net.boddo.btm.R;


public class ProgressDialog {

    public static Dialog dialog;

    public static void show(Context context) {

        dialog = new Dialog(context);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setContentView(R.layout.dialog_loader);

        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

        AppCompatImageView loaderIV = dialog.findViewById(R.id.progress_loader_iv);

        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(3000);

        loaderIV.setAnimation(rotateAnimation);
        loaderIV.startAnimation(rotateAnimation);

    }

    public static void cancel() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
