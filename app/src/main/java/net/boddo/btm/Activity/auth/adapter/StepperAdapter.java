package net.boddo.btm.Activity.auth.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import net.boddo.btm.Activity.auth.stepperfragments.BirthdateFragment;
import net.boddo.btm.Activity.auth.stepperfragments.EmailFragment;
import net.boddo.btm.Activity.auth.stepperfragments.FullNameFragment;
import net.boddo.btm.Activity.auth.stepperfragments.GenderFragment;
import net.boddo.btm.Activity.auth.stepperfragments.PasswordFragment;
import net.boddo.btm.Activity.auth.stepperfragments.UserNameFragment;

public class StepperAdapter extends FragmentPagerAdapter {

    private static int NUM_FRAGMENT = 6;

    public StepperAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return UserNameFragment.newInstance();
            case 1:
                return FullNameFragment.newInstance();
            case 2:
                return PasswordFragment.newInstance();
            case 3:
                return EmailFragment.newInstance();
            case 4:
                return GenderFragment.newInstance();
            case 5:
                return BirthdateFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_FRAGMENT;
    }
}
