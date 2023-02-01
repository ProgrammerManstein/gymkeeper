package com.example.spotsmusic;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.spotsmusic.fragment.DietAnalyseFragment;
import com.example.spotsmusic.fragment.DietAndAnalyseFragment;
import com.example.spotsmusic.fragment.FoodFragment;
import com.example.spotsmusic.fragment.StartFragment;
import com.example.spotsmusic.fragment.TrainFragment;

public class MainAdapter extends FragmentStateAdapter {
    public MainAdapter(Activity fragment) {
        super((FragmentActivity) fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position==0) {
            Fragment trainFragment = new TrainFragment();
            Bundle args = new Bundle();
            trainFragment.setArguments(args);
            return trainFragment;
        }
        else if(position==2){
            Fragment foodFragment = new FoodFragment();
            Bundle args = new Bundle();
            foodFragment.setArguments(args);
            return foodFragment;
        }
        else if(position==3){
            Fragment dietAndAnalyseFragment=new DietAndAnalyseFragment();
            Bundle args=new Bundle();
            dietAndAnalyseFragment.setArguments(args);
            return dietAndAnalyseFragment;
        }
        else{
            Fragment startFragment=new StartFragment();
            Bundle args=new Bundle();
            startFragment.setArguments(args);
            return startFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
