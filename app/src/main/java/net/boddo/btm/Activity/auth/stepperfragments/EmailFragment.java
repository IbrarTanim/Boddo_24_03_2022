package net.boddo.btm.Activity.auth.stepperfragments;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
public class EmailFragment extends Fragment {

    private static final String TAG = "EmailFragment";


    public EmailFragment() {
        // Required empty public constructor
    }
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.edit_text_email)
    EditText etEmail;

    StepperIndicator indicator;
    private BaseCommunicator communicator;

    public static Fragment newInstance() {
        return new EmailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email, container, false);
        ButterKnife.bind(this,view);

        //communicator.setTitle("Email");
        indicator = getActivity().findViewById(R.id.stepper);
        indicator.setCurrentStep(3);
        return view;
    }


    @OnClick(R.id.next_button)
    public void onNextButtonCliceked(){
        String userName = getArguments().getString(Constants.USER_NAME);
        String fullName = getArguments().getString(Constants.FULL_NAME);
        String password = getArguments().getString(Constants.PASSWORD);
        String email = etEmail.getText().toString();
        if (isValidEmail(email))
        {
            GenderFragment genderFragment= new GenderFragment();
            Bundle args = new Bundle();
            args.putString(Constants.USER_NAME, userName);
            args.putString(Constants.FULL_NAME, fullName);
            args.putString(Constants.PASSWORD, password);
            args.putString(Constants.EMAIL, email);
            genderFragment.setArguments(args);
            this.getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                    .replace(R.id.container, genderFragment, null)
                    .addToBackStack(TAG)
                    .commit();
        }else{
            if (etEmail.equals("")){
                etEmail.setError("Email can not be Empty");
            }else{
                etEmail.setError("Invalid  email..");
            }

        }
    }
    private  boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
            //communicator.setTitle("Email");
        }
    }


}
