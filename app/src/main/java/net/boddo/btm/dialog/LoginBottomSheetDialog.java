package net.boddo.btm.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginBottomSheetDialog extends BottomSheetDialogFragment implements Constants {

    private LoginBottomSheetListener loginBottomSheetListener;
    @BindView(R.id.text_view_create_new_account)
    TextView createNewAccount;
    @BindView(R.id.text_view_forgot_password)
    TextView forgotPassword;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_option_bottom_sheet_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    public interface LoginBottomSheetListener{
        void onItemClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            loginBottomSheetListener = (LoginBottomSheetListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement loginBottomSheetListener");
        }
    }
    @OnClick(R.id.text_view_create_new_account)
    public void onCreatenewAccountClicked(){
        loginBottomSheetListener.onItemClicked(CREATE_NEW_ACCOUNT);
        dismiss();
    }
    @OnClick(R.id.text_view_forgot_password)
    public void onForgotPasswordClicked(){
        loginBottomSheetListener.onItemClicked(FORGOT_PASSWORD);
        dismiss();
    }
}
