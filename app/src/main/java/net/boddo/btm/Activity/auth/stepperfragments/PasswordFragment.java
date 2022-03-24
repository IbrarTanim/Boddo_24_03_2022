package net.boddo.btm.Activity.auth.stepperfragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.badoualy.stepperindicator.StepperIndicator;

import net.boddo.btm.Activity.auth.BaseCommunicator;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordFragment extends Fragment {

    private static final String TAG = "PasswordFragment";
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.edit_text_password)
    EditText etPassword;

    public PasswordFragment() {
        // Required empty public constructor
    }

    StepperIndicator indicator;
    private BaseCommunicator communicator;

    public static Fragment newInstance() {
        return new PasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password, container, false);
        ButterKnife.bind(this,view);

        //communicator.setTitle("Password");
        indicator = getActivity().findViewById(R.id.stepper);
        indicator.setCurrentStep(2);
        return view;
    }

    @OnClick(R.id.next_button)
    public void onNextButtonClicked(){
        stepToEmailFragment();
    }

    private void stepToEmailFragment() {
        String userName = getArguments().getString(Constants.USER_NAME);
        String fullName = getArguments().getString(Constants.FULL_NAME);
        String password = etPassword.getText().toString();
        if (!password.equals("") && password.length() > 5){
            EmailFragment emailFragment = new EmailFragment();
            Bundle args = new Bundle();
            args.putString(Constants.USER_NAME, userName);
            args.putString(Constants.FULL_NAME, fullName);
            args.putString(Constants.PASSWORD, password);
            emailFragment.setArguments(args);
            this.getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                    .replace(R.id.container, emailFragment, null)
                    .addToBackStack(TAG)
                    .commit();
        }else{
            if (password.equals("")){
                etPassword.setError("Password can not be empty.");
            }
            if (password.length() < 5 ){
                etPassword.setError("Password must be above 5 characters.");
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (BaseCommunicator) context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //communicator.setTitle("Password");
        }
    }
}
