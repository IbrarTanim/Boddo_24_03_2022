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
public class FullNameFragment extends Fragment {


    private static final String TAG = "FullNameFragment";

    public FullNameFragment() {// Required empty public constructor
    }
    public static Fragment newInstance() {
        return new FullNameFragment();
    }

    StepperIndicator indicator;
    @BindView(R.id.edit_text_fullname)
    EditText etFullName;
    @BindView(R.id.next_button)
    Button nextButton;
    String user_name;
    private BaseCommunicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_name, container, false);
        ButterKnife.bind(this,view);

        //communicator.setTitle("Full name");

        user_name = getArguments().getString(Constants.USER_NAME);
        indicator = getActivity().findViewById(R.id.stepper);
        indicator.setCurrentStep(1);
        return view;
    }

    @OnClick(R.id.next_button)
    void stepToNextFragment(){
        String fullName = etFullName.getText().toString();
        if (fullName.length() > 5){

            PasswordFragment fullNameFragment = new PasswordFragment();
            Bundle args = new Bundle();
            args.putString(Constants.USER_NAME, user_name);
            args.putString(Constants.FULL_NAME, fullName);
            fullNameFragment.setArguments(args);

            this.getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_enter,R.anim.fragment_exit,R.anim.fragment_pop_enter,R.anim.fragment_pop_exit)
                    .replace(R.id.container, fullNameFragment, null)
                    .addToBackStack(TAG)
                    .commit();
        }else{
            etFullName.setError("Full Name is too short");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (BaseCommunicator) context;
    }
}
