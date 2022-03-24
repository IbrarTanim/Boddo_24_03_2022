package net.boddo.btm.dialogfragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import net.boddo.btm.Activity.BuyCreditActivity;
import net.boddo.btm.Activity.DashBoadActivity;
import net.boddo.btm.Activity.HotlistActivityNew;
import net.boddo.btm.R;
import net.boddo.btm.Utills.Constants;
import net.boddo.btm.Utills.Data;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class HotlistDialogFragment extends DialogFragment {

    private static final String TAG = "HotlistDialogFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    HotlistActivityNew activity;

    @BindView(R.id.editTextBidAmount)
    EditText editTextBidAmount;

    @BindView(R.id.submit_button)
    Button submitButton;

    TextView tvCredits;

    Button cancelButton;

    public HotlistDialogFragment(HotlistActivityNew activity) {
        this.activity = activity;
    }

    public interface HotlistListener {
        void onSuccessListener(int credits);
    }

    private HotlistListener listener;

    public void setOnHotlistListener(HotlistListener hotlistListener) {
        this.listener = hotlistListener;
    }

    int balance;
    int bidAmount;
    int previousBidAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_hotlist_dialog, container);

        submitButton = v.findViewById(R.id.submit_button);
        cancelButton = v.findViewById(R.id.cancelButton);
        tvCredits = v.findViewById(R.id.tvCredits);
        editTextBidAmount = v.findViewById(R.id.editTextBidAmount);
        editTextBidAmount.requestFocus();
        editTextBidAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submitButton.setTextColor(getResources().getColor(R.color.white));
                submitButton.setBackground(getResources().getDrawable(R.drawable.button2));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.hotlist.length == 0) {
                    Intent intent = new Intent(getActivity(), DashBoadActivity.class);
                    startActivity(intent);
                } else {
                    dismiss();
                }

            }
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            setCancelable(false);
            // window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        ButterKnife.bind(this, v);
        Bundle bundle = getArguments();
        balance = bundle.getInt("current_balance", 0);
        previousBidAmount = bundle.getInt("previous_bid_amount", 0);

        if (balance != 0) {
            tvCredits.setText(String.valueOf((balance) + " " + " Credits"));
        }
        // Do all the stuff to initialize your custom view
        return v;
    }


   /* @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
*/

    @OnClick(R.id.submit_button)
    public void onBid() {
        //int value = editTextBidAmount.getText().toString().trim().length();
        bidAmount = Integer.parseInt(editTextBidAmount.getText().toString());

        if (bidAmount > 0) {
            if (bidAmount != 0) {
                if (bidAmount > balance) {
                    dismiss();
                    addFundHotlistDialog();
                } else if (bidAmount < 20 && bidAmount > 0) {
                    minimumBidHotlistDialog();
                    dismiss();

                } else if (previousBidAmount >= bidAmount) {
                    currentBidHotlistDialog();
                    dismiss();


                } else {
                    StringRequest request = new StringRequest(Request.Method.POST, Constants.BASE_URL + "add_hotlist.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            listener.onSuccessListener(balance - bidAmount);
                            Toast.makeText(getActivity(), "Congratulations, you have successfully joined the hotlist.", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("secret_key", String.valueOf(Constants.SECRET_KEY));
                            map.put("user_id", Data.userId);
                            map.put("bid", String.valueOf(bidAmount));
                            return map;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getActivity().getBaseContext());
                    queue.add(request);

                }
            } else {
                final PrettyDialog dialog = new PrettyDialog(getActivity());
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

                    dismiss();

                    addFundHotlistDialog();



                    /*dialog.setTitle("Insufficient balance !!!")
                            .setTitleColor(R.color.pdlg_color_blue)
                            .setMessage("Dear " + Data.userName + ", you don not have sufficient palup credits, please recharge!\n" +
                                    "\n" +
                                    "Through the credits you can view who liked, favorite and visited you. You can also use your credits for joining the Hotlist.\n" +
                                    "\n" + "Palup is all about making friends or love")
                            .setMessageColor(R.color.pdlg_color_black)
                            .setIcon(R.drawable.logo1).setIconTint(R.color.colorPrimary)
                            .addButton("ADD FUNDS", R.color.white, R.color.amber_900, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    goToBuyCredit();
                                    dialog.dismiss();
                                    dialog.dismiss();
                                }
                            })
                            .addButton("ACTIVE PALUP PLUS", R.color.red_900, R.color.white, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    goToPalupPlus();
                                    dialog.dismiss();
                                    dialog.dismiss();
                                }
                            }).show();
                    dialog.setCancelable(false);*/


                } else {

                    dismiss();
                    addFundHotlistDialog();



                   /* dialog.setTitle("Insufficient balance !!!")
                            .setTitleColor(R.color.pdlg_color_blue)
                            .setMessage("Dear " + Data.userName + ", you don not have sufficient palup credits, please recharge!\n" +
                                    "\n" +
                                    "Through the credits you can view who liked, favorite and visited you. You can also use your credits for joining the Hotlist.\n" +
                                    "\n" + "Palup is all about making friends or love")
                            .setMessageColor(R.color.pdlg_color_black)
                            .addButton("ADD FUNDS", R.color.white, R.color.amber_900, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                    goToBuyCredit();
                                    dialog.dismiss();
                                }
                            })
                            .addButton("ACTIVE PALUP PLUS", R.color.red_900, R.color.white, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                    goToPalupPlus();
                                    dialog.dismiss();
                                }
                            }).show();
                    dialog.setCancelable(false);*/


                }

            }
        }
        else {
            Toast.makeText(getContext(), "You have to bid a valid amount!", Toast.LENGTH_SHORT).show();
        }
    }


    public void goToPalupPlus() {
        Intent intent = new Intent(getActivity(), BuyCreditActivity.class);
        intent.putExtra(Constants.PALUP_PLUS, Constants.PALUP_PLUS);
        startActivity(intent);
    }

    /*@OnClick(R.id.cancel_button)
    public void onCancelButtonClicked() {
        dismiss();
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void addFundHotlistDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setTitle("info");
        dialog.setContentView(R.layout.add_fund_hotlist_dialog);
        final Button btnDialogAddFund = dialog.findViewById(R.id.btnDialogAddFund);
        btnDialogAddFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //goToBuyCredit();

                Intent intent = new Intent(v.getContext(), BuyCreditActivity.class);
                intent.putExtra("BuyCredits", true);
                // activity.overridePendingTransition(0, 0);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                // activity.finish();
                v.getContext().startActivity(intent);
            }
        });
        final Button btnDialogCancel = dialog.findViewById(R.id.btnDialogCancel);
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void minimumBidHotlistDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setTitle("info");
        dialog.setContentView(R.layout.minimum_bid_hotlist_dialog);
        final Button btnDialogMinimumBid = dialog.findViewById(R.id.btnDialogMinimumBid);
        btnDialogMinimumBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.addMeHotlist();
            }
        });
        final Button btnDialogMinimumBidCancel = dialog.findViewById(R.id.btnDialogMinimumBidCancel);
        btnDialogMinimumBidCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void currentBidHotlistDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setTitle("info");
        dialog.setContentView(R.layout.current_bid_hotlist_dialog);
        final Button btnDialogMinimumBid = dialog.findViewById(R.id.btnDialogMinimumBid);
        btnDialogMinimumBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.addMeHotlist();
            }
        });
        final Button btnDialogMinimumBidCancel = dialog.findViewById(R.id.btnDialogMinimumBidCancel);
        btnDialogMinimumBidCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void goToBuyCredit() {
        //Toast.makeText(activity, "BuyCredits", Toast.LENGTH_SHORT).show();
       /* Intent intent = new Intent(getActivity(), BuyCreditActivity.class);
        intent.putExtra("BuyCredits", true);
        // activity.overridePendingTransition(0, 0);
        // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        // activity.finish();
        getActivity().startActivity(intent);*/


    }


}
