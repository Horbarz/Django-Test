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

import com.example.djangotest.Adapter.CategoryByIdAdapter;
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

public class CategoryById extends Fragment {

    Integer CategoryUrl;
    private static final String TAG = "CategoryList";

    private ArrayList<Integer> pkCategory = new ArrayList<>();
    private ArrayList<Integer> idCategory = new ArrayList<>();
    private ArrayList<String> nameCategory = new ArrayList<>();

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.post_list, container, false);
        recyclerView = root.findViewById(R.id.recycler_category_list);

        if(InternetUtil.isInternetOnline(getActivity())){
            Bundle bundle = this.getArguments();
            if(bundle!=null){
                CategoryUrl = bundle.getInt("post_id");

            }
        }

        getActivity().setTitle("Category Posts");

        return root;
    }

    private void getCategoryById(Integer id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.POST_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        PostApi postApi = retrofit.create(PostApi.class);
        Call<List<CategoryModel>> call = postApi.getCategoryById(id);

        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        List<CategoryModel> catid_list = response.body();
                        for(CategoryModel h : catid_list){
                            Integer catid_id = h.getId();
                            pkCategory.add(catid_id);

                            String catid_name = h.getName();
                            nameCategory.add(catid_name);

                            Integer catid_idCat = h.getId_category();
                            idCategory.add(catid_idCat);

                        }
                        initRecyclerView();
                    }else{
                        Log.d("Failed", "failure ");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.d("Fail", "onFailure: Failed Finally");
            }
        });


    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycler view");
        CategoryByIdAdapter adapter = new CategoryByIdAdapter(nameCategory,pkCategory,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    public void clearList(){
        pkCategory.clear();
        nameCategory.clear();
        idCategory.clear();

        CategoryByIdAdapter adapter = new CategoryByIdAdapter(nameCategory,pkCategory,getActivity());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        if(InternetUtil.isInternetOnline(getActivity())){
            clearList();
            getCategoryById(CategoryUrl);
        }
    }
}
