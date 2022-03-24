package net.boddo.btm.Activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.Objects;
import java.util.regex.Pattern;

import am.appwise.components.ni.NoInternetDialog;
import net.boddo.btm.Adepter.ViewPageAdepter;
import net.boddo.btm.Callbacks.ApiClient;
import net.boddo.btm.Callbacks.ApiInterface;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Connectivity;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Helper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static net.boddo.btm.Utills.Constants.SECRET_KEY;

public class RegistrationFirstStepActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationFirstStepAc";
    ViewPageAdepter viewPageAdepter;
    String userName = "";
    String password = "";
    String confirmPassword = "";
    ApiInterface apiInterface;
    private boolean isFormValid = false;
    private boolean userNameExist = false;
    NoInternetDialog noInternetDialog;
    @BindView(R.id.next_button)
    Button nextButton;
    @BindView(R.id.edit_text_userName)
    EditText  editTextUserName;
    @BindView(R.id.edit_text_password)
    EditText editTextPassword;
    @BindView(R.id.edit_text_confirm_password)
    EditText editTextConfirmPassword;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_first_step);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);// action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Create New Account");
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        viewPageAdepter = new ViewPageAdepter(getSupportFragmentManager());
        editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (Connectivity.isConnected(RegistrationFirstStepActivity.this) || Connectivity.isConnectedWifi(RegistrationFirstStepActivity.this) && checkUserName()) {
                    userName = editTextUserName.getText().toString().trim();
                    if (userName.length() > 3) {
                        if (isUserNameValid(userName)) {
                            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<String> call = apiInterface.checkUserNameExist(userName, SECRET_KEY);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String result = response.body();
                                    if (result.equals("User Name Already Exist")) {
                                        editTextUserName.setError(getResources().getString(R.string.user_name_exist));
                                        userNameExist = true;
                                    } else {
                                        userNameExist = false;
                                    }
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d(TAG, "" + t.getMessage());
//                                Toast.makeText(getActivity(), "" + t, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegistrationFirstStepActivity.this, "user Name not valid", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        editTextUserName.setError(getResources().getString(R.string.enter_user_name_limite));
                    }
                }
            }
        });
    }

    private boolean isUserNameValid(String userName) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9_.-]+");
        return (userName != null) && pattern.matcher(userName).matches();
    }

    private void switchToSecondStep(String userName, String password) {
        Intent intent = new Intent(RegistrationFirstStepActivity.this,RegistrationSecondStepActivity.class);
        intent.putExtra(Constants.USER_NAME,userName);
        intent.putExtra(Constants.PASSWORD,password);
        startActivity(intent);
    }

    public String textToString(EditText editText) {
        String string = editText.getText().toString();
        return string;
    }

    @OnClick(R.id.next_button)
    public void onNextButtonClicked(){
        password = textToString(editTextPassword);
        confirmPassword = textToString(editTextConfirmPassword);
        if (userName.length() >= 3 && !userNameExist && password.length() >= 5 && confirmPassword.length() >= 5) {
            if (password.equals(confirmPassword)) {
                switchToSecondStep(userName, password);
            } else {
                MDToast mdToast = MDToast.makeText(RegistrationFirstStepActivity.this, getResources().getString(R.string.password_does_not_matched), MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
                mdToast.show();
            }
        } else {
            if (userName.equals("")) {
                editTextUserName.setError(getResources().getString(R.string.enter_user_name));
            }
            if (password.length() < 5) {
                editTextPassword.setError(getResources().getString(R.string.password_too_short));
            }
            if (confirmPassword.length() < 5)
                editTextConfirmPassword.setError(getResources().getString(R.string.confirm_password_too_short));
        }
    }

    public Boolean checkUserName() {
        userName = editTextUserName.getText().toString();
        Helper helper = new Helper();
        if (userName.length() == 0) {
            editTextUserName.setError(getString(R.string.enter_user_name));
            return false;
        }
        if (userName.length() < 3) {
            editTextUserName.setError(getString(R.string.user_name_too_short));
            return false;
        }
        if (!helper.isValidLogin(userName)) {
            editTextUserName.setError(getString(R.string.error_wrong_format));
            return false;
        }
        editTextUserName.setError(null);
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
