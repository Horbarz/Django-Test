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
import android.widget.LinearLayout;

import com.example.djangotest.Adapter.AddCategoryAdapter;
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

public class CategoryForAdd extends Fragment {

    private static final String TAG = "CategoryForAdd";

    private ArrayList<Integer> pkCategory = new ArrayList<>();
    private ArrayList<Integer> idCategory = new ArrayList<>();
    private ArrayList<String> nameCategory = new ArrayList<>();

    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_list, container, false);

        recyclerView = v.findViewById(R.id.recycler_category_list);

        if(InternetUtil.isInternetOnline(getActivity())){
            showAllCategory();
        }

        return v;

    }

    private void showAllCategory() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.POST_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        PostApi postApi = retrofit.create(PostApi.class);
        Call<List<CategoryModel>> call = postApi.getAllCategory();
        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        List<CategoryModel> categoryList = response.body();
                        for (CategoryModel h : categoryList){
                            Integer cat_id = h.getId();
                            pkCategory.add(cat_id);

                            String cat_name = h.getName();
                            nameCategory.add(cat_name);

                            Integer cat_id_category = h.getId_category();
                            idCategory.add(cat_id_category);

                        }
                        initRecyclerView();
                    }
                }else{
                    Log.d("Fail", "onResponse: failed");
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.d("fail", "onFailure: "+t.getMessage()==null?"":t.getMessage());

            }
        });
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycler view");
        AddCategoryAdapter adapter = new AddCategoryAdapter(pkCategory,idCategory,nameCategory,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
