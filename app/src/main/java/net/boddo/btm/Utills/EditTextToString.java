package net.boddo.btm.Utills;

import android.widget.EditText;

public class EditTextToString {
    public static String etToString(EditText editText){
        String str = editText.getText().toString().trim();
        return str;
    }
}
