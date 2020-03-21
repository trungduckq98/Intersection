package com.deathmarch.intersection.view.homepage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    public TabsAccessorAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewsFragment newsFragment = new NewsFragment();
                return newsFragment;
            case 1:
                MessengerFragment messengerFragment = new MessengerFragment();
                return messengerFragment;
            case 2:
                NotifyFragment notifyFragment = new NotifyFragment();
                return notifyFragment;
            case 3:
                OptionFragment optionFragment = new OptionFragment();
                return optionFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
