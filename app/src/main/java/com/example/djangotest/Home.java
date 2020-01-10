package com.example.djangotest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.djangotest.Adapter.HomeAdapter;
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

public class Home extends Fragment {
    private ArrayList<Integer> pkPost = new ArrayList<>();
    private ArrayList<String> namePost = new ArrayList<>();
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.post_list, container, false);

        recyclerView = root.findViewById(R.id.recycler_category_list);

        if(InternetUtil.isInternetOnline(getActivity())){
            clearList();
            showAllPost();
        }

        getActivity().setTitle("All Posts");

        return root;
    }

    private void clearList() {
        pkPost.clear();
        namePost.clear();

        HomeAdapter adapter = new HomeAdapter(pkPost,namePost,getActivity());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void showAllPost(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        PostApi postApi = retrofit.create(PostApi.class);
        Call<List<PostModel>> call = postApi.getListPost();

        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        List<PostModel> postList = response.body();
                        for(PostModel h: postList){
                            Integer cat_id = h.getId();
                            pkPost.add(cat_id);

                            String cat_name = h.getTitle();
                            namePost.add(cat_name);
                        }

                        initRecyclerView();

                    }
                }else{
                    Log.d("fail", "onResponse: Failed");
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d("fail", "onFailure: "+t.getMessage() == null?"Nothing to show":t.getMessage());
            }
        });
    }

    private void initRecyclerView() {
        Log.d("Home", "initRecyclerView: init recycler view ");
        HomeAdapter adapter = new HomeAdapter(pkPost,namePost,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction()==KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    getActivity().finish();
                    return true;
                } return false;

            }
        });
    }
}
