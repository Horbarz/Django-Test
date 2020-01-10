package com.example.djangotest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Footer extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.footer, container, false);

        initViews(v);
        return v;
    }

    private void initViews(View v) {
        LinearLayout lbuttonHome = (LinearLayout) v.findViewById(R.id.lbuttonHome);
        LinearLayout lbuttonCategory = (LinearLayout) v.findViewById(R.id.lbuttonCategory);
        LinearLayout lbuttonPlus = (LinearLayout) v.findViewById(R.id.lbuttonPlus);
        LinearLayout lbuttonProfile = (LinearLayout) v.findViewById(R.id.lbuttonProfile);


        lbuttonHome.setOnClickListener(this);
        lbuttonCategory.setOnClickListener(this);
        lbuttonPlus.setOnClickListener(this);
        lbuttonProfile.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.lbuttonHome:
                fragment = new Home();
                replaceFragment(fragment);
                break;
            case R.id.lbuttonCategory:
                fragment = new CategoryList();
                replaceFragment(fragment);
                break;
            case R.id.lbuttonPlus:
                if (CheckLogin()) {
                    fragment = new PostAdd();
                }else {
                    fragment = new ProfileLogin();
                }
                replaceFragment(fragment);
                break;
            case R.id.lbuttonProfile:

                if (CheckLogin()) {
                    fragment = new UserPosts();
                }else {
                    fragment = new ProfileLogin();
                }
                replaceFragment(fragment);
                break;
        }


    }

    public void replaceFragment(Fragment someFragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public Boolean CheckLogin() {

        SharedPreferences preferences = getActivity().getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        Boolean shered_category_id = preferences.getBoolean("loggedin",  false);


        if (shered_category_id) {
            return true;
        }
        else {
            return false;
        }
    }

}
