package com.example.djangotest;

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

import com.example.djangotest.Adapter.CategoryListAdapter;
import com.example.djangotest.model.CategoryModel;
import com.example.djangotest.util.InternetUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryList extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<String> nameCategory = new ArrayList<>();
    private ArrayList<Integer> pkCategory = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.post_list,container,false);

        recyclerView = root.findViewById(R.id.recycler_category_list);

        getActivity().setTitle("Category");



        return root;
    }

    private void getAllCategories() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.POST_URL).addConverterFactory(GsonConverterFactory.create()).build();

        PostApi postApi = retrofit.create(PostApi.class);
        Call<List<CategoryModel>> call = postApi.getAllCategory();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        List<CategoryModel> catList = response.body();
                        for (CategoryModel h : catList){
                            Integer cat_id = h.getId();
                            pkCategory.add(cat_id);

                            String cat_name = h.getName();
                            nameCategory.add(cat_name);

                        }
                        initRecyclerView();
                    }
                }else{
                    Log.d("fail", "onResponse: Failed");
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {

            }
        });


    }

    private void initRecyclerView() {

        CategoryListAdapter adapter = new CategoryListAdapter(pkCategory,nameCategory,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();

        if(InternetUtil.isInternetOnline(getActivity())){
            clearList();
            getAllCategories();
        }
    }

    private void clearList() {
        pkCategory.clear();
        nameCategory.clear();

        CategoryListAdapter adapter = new CategoryListAdapter(pkCategory,nameCategory,getActivity());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


    }
}
