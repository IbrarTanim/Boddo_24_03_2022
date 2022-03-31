package net.boddo.btm.Activity.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.boddo.btm.Activity.AllLikesActivity;
import net.boddo.btm.Adepter.AllLikesAdapter;
import net.boddo.btm.Adepter.LanguageSelectionAdapter;
import net.boddo.btm.Fragment.ProfileFragment;
import net.boddo.btm.Fragment.ProfileInfoFragment;
import net.boddo.btm.Model.LanguageSelection;
import net.boddo.btm.R;
import net.boddo.btm.Utills.AboutUpdate;
import net.boddo.btm.Utills.Data;
import net.boddo.btm.Utills.ItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageActivity extends AppCompatActivity {

    LanguageActivity activity;
    public static String[] listItem;

    LanguageSelectionAdapter languageSelectionAdapter;

    ArrayList<LanguageSelection> languageSelectionList;

    Button tvSave;
    List<String> languageList;


    TextView  tvBack;
    RecyclerView rvLanguage;

    String value = "";
    String key = "habits";
    boolean isChanged = false;
    TextView btnCancelLanguageActivity,btnSaveLanguageActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        activity = this;

        if (Data.STATUS_BAR_HEIGHT != 0) {
            View blankView = findViewById(R.id.blankView);
            ViewGroup.LayoutParams params = blankView.getLayoutParams();
            params.height = Data.STATUS_BAR_HEIGHT;
            blankView.setLayoutParams(params);
        }

        languageList = new ArrayList<>();

        ButterKnife.bind(this);

        languageSelectionList = new ArrayList<>();


        if (!Data.userHabits.equals("")) {
            String doUserHabits = Data.userHabits;
        }

        insertLanguages();

        tvSave = findViewById(R.id.tvSave);
        tvBack = findViewById(R.id.tvBack);

        rvLanguage = findViewById(R.id.rvLanguage);
        btnCancelLanguageActivity = findViewById(R.id.btnCancelLanguageActivity);
        btnSaveLanguageActivity = findViewById(R.id.btnSaveLanguageActivity);


        languageSelectionAdapter = new LanguageSelectionAdapter(languageSelectionList, activity, new ItemOnClickListener() {
            @Override
            public void OnClick(View view, int position, boolean isLongClicked) {

                String itemSelected = languageSelectionList.get(position).getLanguageName();


                if (languageList.contains(itemSelected)) {

                    languageList.remove(itemSelected);
                    isChanged = true;
                    Log.e("Remove", "Removed");

                } else {

                    if (languageList.size() >= 5) {


                        CheckBox checkBox = view.findViewById(R.id.languageCheckBox);
                        checkBox.setChecked(false);
                        Toast.makeText(LanguageActivity.this, "You can select maximum 5 languages..", Toast.LENGTH_SHORT).show();

                    } else {
                        languageList.add(itemSelected);
                        isChanged = true;
                        Log.e("Added", "added");
                    }

                }


            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity, 1);
        rvLanguage.setLayoutManager(mLayoutManager);
        //rvAllLikes.addItemDecoration(new GridSpacingItemDecoration(1, GridSpacingItemDecoration.dpToPx(10, activity), true));
        rvLanguage.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager.setAutoMeasureEnabled(false);

        rvLanguage.setHasFixedSize(true);
        rvLanguage.setLayoutManager(mLayoutManager);
        rvLanguage.setAdapter(languageSelectionAdapter);


        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dataSave();
                saveLanguage();
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancelLanguageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void insertLanguages() {
        LanguageSelection languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Bangla");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("English");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Afghanistan");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Albania");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Algeria");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Andorra");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Angola");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Austria");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Urdu");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Belgium");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Brazil");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Bulgaria");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Croatia");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("France");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("German");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Spanish");
        languageSelectionList.add(languageSelection);

        languageSelection = new LanguageSelection();
        languageSelection.setLanguageName("Italian");
        languageSelectionList.add(languageSelection);


    }

    private void dataSave() {
        if (isChanged) {
            if (AboutUpdate.result) {
                Data.userHabits = value;
                Toast.makeText(this, "Update Successfull", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void saveToServer() {
        AboutUpdate update = new AboutUpdate(this);
        update.updateAbout(key, value);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LanguageActivity.class);
        return intent;
    }


    private void saveLanguage() {

        if (isChanged) {

            if (!languageList.isEmpty()) {

                String item = "";

                for (int i = 0; i < languageList.size(); i++) {

                    if (i == languageList.size() - 1) {
                        item = item + languageList.get(i);
                    } else {
                        item = item + languageList.get(i) + ",";
                    }


                }

                AboutUpdate obj = new AboutUpdate(this);
                obj.updateAbout("language", item);
                Data.userLanguage = item;
                Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();

            }


        }

    }
}
