package com.deathmarch.intersection.view.friend;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FriendTabsAccessorAdapter extends FragmentPagerAdapter {

    public FriendTabsAccessorAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListFriendFragment listFriendFragment = new ListFriendFragment();
                return listFriendFragment;
            case 1:
                ReceiveRequestFragment receiveRequestFragment = new ReceiveRequestFragment();
                return receiveRequestFragment;
            case 2:
                SendRequestFragment sendRequestFragment = new SendRequestFragment();
                return sendRequestFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
