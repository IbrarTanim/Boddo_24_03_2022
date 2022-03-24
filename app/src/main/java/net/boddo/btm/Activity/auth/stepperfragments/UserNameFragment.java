package net.boddo.btm.Activity.auth.stepperfragments;

import static net.boddo.btm.Utills.Constants.SECRET_KEY;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.badoualy.stepperindicator.StepperIndicator;

import net.boddo.btm.Activity.auth.BaseCommunicator;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserNameFragment extends Fragment {

    private static final String TAG = "UserNameFragment";
    private BaseCommunicator communicator;

    public UserNameFragment() {
        // Required empty public constructor
    }
    public static Fragment newInstance() {
        return new UserNameFragment();
    }

    @BindView(R.id.edit_text_userName)
    EditText etUserName;
    @BindView(R.id.next_button)
    Button nextButton;
    private String userName;
    ApiInterface apiInterface;
    StepperIndicator indicator;
    boolean userNameExist = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_name, container, false);
        ButterKnife.bind(this, view);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //communicator.setTitle("Username");
        indicator = getActivity().findViewById(R.id.stepper);
        indicator.setCurrentStep(0);
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                userName = etUserName.getText().toString();

                if (userName.length() >= 3) {
                    if (isUserNameValid(userName)) {
                        Call<String> call = apiInterface.checkUserNameExist(userName, SECRET_KEY);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = response.body();
                                if (result.equals("User Name Already Exist")) {
                                   // etUserName.setError(getResources().getString(R.string.user_name_exist));
                                    Toast.makeText(getActivity().getApplicationContext(), "This username is already taken", Toast.LENGTH_LONG).show();
                                    userNameExist = true;
                                } else {
                                    userNameExist = false;
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d(TAG, "" + t.getMessage());
//                      Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Username is not valid", Toast.LENGTH_SHORT).show();
                    }
                } else {
                   // etUserName.setError(getResources().getString(R.string.enter_user_name_limite));
                  //  Toast.makeText(getActivity().getApplicationContext(), "Username must be not less than 3 letters", Toast.LENGTH_SHORT).show();
                    userNameExist = true;
                }
                }
        });
        return view;
    }
    @OnClick(R.id.next_button)
    void onNext(){
         if (!userNameExist && etUserName.getText().toString().length() > 2){
            stepToFullName();
        }else if(userNameExist==true &&  etUserName.getText().toString().length() < 3){
            Toast.makeText(getActivity().getApplicationContext(), "Username must be not less than 3 letters", Toast.LENGTH_LONG).show();
        }else if(etUserName.getText().toString().length() == 0){
            Toast.makeText(getActivity().getApplicationContext(), "You need a username", Toast.LENGTH_LONG).show();
        }else if(userNameExist==true){
            Toast.makeText(getActivity().getApplicationContext(), "This username is already taken", Toast.LENGTH_LONG).show();
        }
    }
    private void stepToFullName() {
        FullNameFragment fullNameFragment = new FullNameFragment();
        Bundle args = new Bundle();
        args.putString(Constants.USER_NAME, userName);
        fullNameFragment.setArguments(args);
        this.getFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_enter,R.anim.fragment_exit,R.anim.fragment_pop_enter,R.anim.fragment_pop_exit)
                .replace(R.id.container,fullNameFragment, null)
                .addToBackStack(TAG)
                .commit();
    }
    public boolean isUserNameValid(String userName) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9_.-]+");
        return (userName != null) && pattern.matcher(userName).matches();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (BaseCommunicator) context;
    }
}
