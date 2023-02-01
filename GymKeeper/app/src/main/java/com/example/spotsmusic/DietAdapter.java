package com.example.spotsmusic;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.spotsmusic.fragment.DietAnalyseFragment;
import com.example.spotsmusic.fragment.DietFragment;
import com.example.spotsmusic.fragment.FoodFragment;
import com.example.spotsmusic.fragment.StartFragment;
import com.example.spotsmusic.fragment.TrainFragment;

public class DietAdapter extends FragmentStateAdapter {
    public DietAdapter(Activity fragment) {
        super((FragmentActivity) fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position==0) {
            Fragment dietFragment = new DietFragment();
            Bundle args = new Bundle();
            dietFragment.setArguments(args);
            return dietFragment;
        }
        else{
            Fragment dietAnalyseFragment=new DietAnalyseFragment();
            Bundle args = new Bundle();
            dietAnalyseFragment.setArguments(args);
            return dietAnalyseFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
