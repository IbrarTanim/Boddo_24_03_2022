package net.boddo.btm.Fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import net.boddo.btm.Adepter.ViewPageAdepter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Connectivity;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.EditTextToString;
import net.boddo.btm.Utills.Helper;
import net.boddo.btm.ViewPager.CustomViewPager;
import net.boddo.btm.test.TestAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupFragment_2 extends Fragment implements Constants {

    String age;
    Button nextButton;
    EditText editTextFirstName, editTextEmail;
    EditText dateOfBirth;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale, radioOthers;
    View view;

    String gender = "";
    String fName = "";
    String birthDate = "";
    String emailAddress = "";

    ApiInterface apiInterface;
    CustomViewPager pager;
    ViewPageAdepter viewPageAdepter;

    Calendar calendar;
    DatePickerDialog datePickerDialog;
    boolean isEmailExist = false;
//     mToDateCalanderBTN


    public SignupFragment_2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup_fragment_2, container, false);
        initView();

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int current_month = month + 1;
                        String currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
                        age = dayOfMonth + "/" + current_month + "/" + year;
                        dateOfBirth.setText(age);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }

        });



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
                    case R.id.other:
                        gender = radioOthers.getText().toString();
                        // do operations specific to this selection
                        break;
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(Helper.getAge(age)) >= 14) {
                    fName = EditTextToString.etToString(editTextFirstName);
                    birthDate = EditTextToString.etToString(dateOfBirth);
                    emailAddress = EditTextToString.etToString(editTextEmail);

                    if (!fName.equals("") && !birthDate.equals("") && !emailAddress.equals("")) {
                        if (fName.length() >= 5) {
                            if (isValidEmail(emailAddress) && isEmailExist == false) {
                                pager.setEnableSwipe(true);

                                goToNextFragment(fName, birthDate, emailAddress, gender);
                            } else {
                                editTextEmail.setError("Email is not valid");
                            }
                        } else {
                            if (fName.length() < 5)
                                editTextFirstName.setError("First Name is too short");
                        }
                    } else {
                        if (fName.equals(""))
                            editTextFirstName.setError("First Name is Empty");
                        if (birthDate.equals(""))
                            dateOfBirth.setError("Birth Date is Empty");
                        if (emailAddress.equals(""))
                            editTextEmail.setError("Email is empty");
                    }


                } else {
                    showDailog();
                }

            }
        });
        return view;
    }

    private void showDailog() {

        TestAlertDialog testAlertDialog = new TestAlertDialog();
        testAlertDialog.show(getFragmentManager(), "testAlertDialog");
    }

    private void goToNextFragment(String fName, String birthDate, String emailAddress, String gender) {
        SignupFragment_3 nextFrag = new SignupFragment_3();
        String user_name = getArguments().getString(USER_NAME);
        String password = getArguments().getString(PASSWORD);

        Bundle args = new Bundle();
        args.putString(USER_NAME, user_name);
        args.putString(PASSWORD, password);
        args.putString(FIRST_NAME, fName);
        args.putString(DATE_OF_BIRTH, birthDate);
        args.putString(GENDER, gender);
        args.putString(EMAIL, emailAddress);
        nextFrag.setArguments(args);

        this.getFragmentManager().beginTransaction()
                .replace(R.id.container, nextFrag, null)
                .addToBackStack(null)
                .commit();
    }

    private void initView() {
        nextButton = view.findViewById(R.id.next_button);
        editTextFirstName = view.findViewById(R.id.edit_text_firstName);
        editTextEmail = view.findViewById(R.id.edit_text_email);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                emailAddress = editTextEmail.getText().toString().trim();
                if (Connectivity.isConnected(getActivity().getBaseContext()) || Connectivity.isConnectedWifi(getActivity().getBaseContext()) && isValidEmail(emailAddress)) {
                    if (emailAddress.length() >= 10) {
                        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<String> call = apiInterface.checkUserEmailExist(emailAddress, SECRET_KEY);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String result = response.body();
                                if (result.equals("Email Already Exist")) {
                                    isEmailExist = true;
                                    editTextEmail.setError(getResources().getString(R.string.email_already_exist));
                                } else {
                                    isEmailExist = false;
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d(getActivity().getClass().getSimpleName(), t.getMessage().toString());
                            }
                        });
                    }

                }
            }
        });
        dateOfBirth = view.findViewById(R.id.edit_text_dateOfBirth);
        radioGroup = view.findViewById(R.id.genderGroup);
        radioMale = view.findViewById(R.id.male);
        radioFemale = view.findViewById(R.id.female);
        radioOthers = view.findViewById(R.id.other);
        viewPageAdepter = new ViewPageAdepter(getActivity().getSupportFragmentManager());

        pager = new CustomViewPager(getActivity());


    }


    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


}
