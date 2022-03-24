package net.boddo.btm.Activity.auth.stepperfragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
public class GenderFragment extends Fragment {

    private static final String TAG = "GenderFragment";

    public GenderFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.genderGroup)
    RadioGroup radioGroup;
    @BindView(R.id.male)
    RadioButton radioMale;

    @BindView(R.id.female)
    RadioButton radioFemale;

    String gender = "";

    String fName = "";
    String birthDate = "";
    String emailAddress = "";

    private BaseCommunicator communicator;

    StepperIndicator indicator;

    public static Fragment newInstance() {
        return new GenderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gender, container, false);
        ButterKnife.bind(this, view);

        indicator = getActivity().findViewById(R.id.stepper);
        indicator.setCurrentStep(4);
        //communicator.setTitle("Gender");
        gender = radioMale.getText().toString();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        gender = radioMale.getText().toString();
                        break;
                    case R.id.female:
                        // do operations specific to this selection
                        gender = radioFemale.getText().toString();
                        break;

                }
            }
        });
        return view;
    }

    @OnClick(R.id.next_button)
    public void onNextButtonClicked() {
        String userName = getArguments().getString(Constants.USER_NAME);
        String fullName = getArguments().getString(Constants.FULL_NAME);
        String password = getArguments().getString(Constants.PASSWORD);
        String email = getArguments().getString(Constants.EMAIL);

        if (!gender.equals("")) {
            BirthdateFragment birthdateFragment = new BirthdateFragment();
            Bundle args = new Bundle();
            args.putString(Constants.USER_NAME, userName);
            args.putString(Constants.FULL_NAME, fullName);
            args.putString(Constants.PASSWORD, password);
            args.putString(Constants.EMAIL, email);
            args.putString(Constants.GENDER, gender);
            birthdateFragment.setArguments(args);

            this.getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
                    .replace(R.id.container, birthdateFragment, null)
                    .addToBackStack(TAG)
                    .commit();
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
            //communicator.setTitle("Gender");
        }
    }

}
