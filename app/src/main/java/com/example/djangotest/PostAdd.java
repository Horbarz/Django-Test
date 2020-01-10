package com.example.djangotest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djangotest.model.AddPostModel;
import com.example.djangotest.model.PostModel;
import com.example.djangotest.util.InternetUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAdd extends Fragment implements View.OnClickListener {
   private Button add_saveBtn;
   private Button add_categoryBtn;
   private EditText addTitle;
   private EditText addText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_add, container, false);

        initViews(v);

        getActivity().setTitle("Add Post");

        return v;
    }

    private void initViews(View view) {
        add_saveBtn = view.findViewById(R.id.add_save_button);
        add_categoryBtn= view.findViewById(R.id.add_category_button);
        addText = view.findViewById(R.id.add_text);
        addTitle = view.findViewById(R.id.add_title);

        add_saveBtn.setOnClickListener(this);
        add_categoryBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_save_button:
                saveButtonClick();
                break;
            case R.id.add_category_button:
                categoryButtonClick();
                break;
        }

    }

    private void categoryButtonClick() {
        if(InternetUtil.isInternetOnline(getActivity())){
            Fragment fragment = null;
            fragment = new CategoryForAdd();
            replaceFragment(fragment);
        }

    }

    private void saveButtonClick() {
        if(!isEmptyEditText()){
            if(InternetUtil.isInternetOnline(getActivity())){
                getSavedId();
            }
        }

    }

    private void getSavedId() {
        SharedPreferences preferences = getActivity().getSharedPreferences("SavedCategories", Context.MODE_PRIVATE);
        Integer shared_categoryId = preferences.getInt("CategoryId",0);
        Integer add_id = 1;
        int add_category = shared_categoryId;
        String add_title = addTitle.getText().toString();
        String add_text = addText.getText().toString();

        AddPostModel addPostModel = new AddPostModel(add_category,add_title,add_text);
        if(InternetUtil.isInternetOnline(getActivity())){
            AddPostServer(addPostModel);
        }

    }

    private void AddPostServer(AddPostModel addPostModel) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.POST_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        String queryToken = SharedDataGetSet.getMySavedToken(getActivity());
        PostApi postApi = retrofit.create(PostApi.class);
        Call<AddPostModel> call = postApi.addPost(queryToken,addPostModel);

        call.enqueue(new Callback<AddPostModel>() {
            @Override
            public void onResponse(Call<AddPostModel> call, Response<AddPostModel> response) {
                Log.d("good", "onResponse: Good");
            }

            @Override
            public void onFailure(Call<AddPostModel> call, Throwable t) {
                Log.d("fail", "onFailure: "+t.getMessage()==null?"":t.getMessage());
            }
        });

    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    private boolean isEmptyEditText(){
        if(addTitle.getText().toString().isEmpty()||addText.getText().toString().isEmpty()){
            Toast toast = Toast.makeText(getActivity(), "Pls fill necessary fields", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return true;
        }else{
            return false;
        }

    }
}
