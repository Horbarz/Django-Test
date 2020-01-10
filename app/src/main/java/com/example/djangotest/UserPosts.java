package com.example.djangotest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.djangotest.Adapter.UserPostAdapter;
import com.example.djangotest.model.PostModel;
import com.example.djangotest.util.InternetUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPosts extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Integer> pkCategory = new ArrayList<>();
    private ArrayList<String> nameCategory = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_list, container, false);
        recyclerView = v.findViewById(R.id.recycler_category_list);

        getActivity().setTitle("My Posts");
        return v;
    }



    private void clearList() {
        pkCategory.clear();
        nameCategory.clear();
        if(InternetUtil.isInternetOnline(getActivity())){
            UserPostAdapter adapter = new UserPostAdapter(pkCategory,nameCategory,getActivity());
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

    private void showUsersPost() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.base_local)
                .addConverterFactory(GsonConverterFactory.create()).build();

        String queryToken = SharedDataGetSet.getMySavedToken(getActivity());

        PostApi postApi = retrofit.create(PostApi.class);
        Call <List<PostModel>> call = postApi.getProfileList(queryToken);

        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        List<PostModel> userPostList = response.body();
                        for(PostModel h : userPostList){
                            Integer userPost_id = h.getId();
                            pkCategory.add(userPost_id);

                            String userPost_name = h.getTitle();
                            nameCategory.add(userPost_name);

//                            Log.d("Result", "returned: id: " +
//                                    String.valueOf(userPost_catid)+" name: "+ userPost_name+" cat_id: "+String.valueOf(userPost_catid));
                            Log.d("Result", "onResponse: "+userPostList);
                        }
                        initRecycleView();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d("fail", "onFailure: Failed");
            }
        });
    }

    private void initRecycleView() {
        UserPostAdapter adapter = new UserPostAdapter(pkCategory,nameCategory,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();

        if(InternetUtil.isInternetOnline(getActivity())){
            clearList();
            showUsersPost();
        }

    }
}
