package com.example.spotsmusic.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.spotsmusic.DietAdapter;
import com.example.spotsmusic.MainAdapter;
import com.example.spotsmusic.R;
import com.example.spotsmusic.dao.DBManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Date;

// Instances of this class are fragments representing a single
// object in our collection.
public class DietAndAnalyseFragment extends Fragment {
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.diet_and_analyse, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        context = getContext();

        ViewPager2 viewPager = view.findViewById(R.id.dietPager);
        DietAdapter dietAdapter= new DietAdapter(requireActivity());
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(dietAdapter);

        TabLayout tabLayout = view.findViewById(R.id.dietTab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if(position==0)
                            tab.setText("今日饮食");
                        if(position==1)
                            tab.setText("饮食分析");
                    }
                }
        ).attach();

    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
