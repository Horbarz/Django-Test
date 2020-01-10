package com.example.djangotest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.djangotest.model.PostModel;
import com.example.djangotest.util.InternetUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class Edit extends Fragment implements View.OnClickListener {

    EditText editTitle;
    EditText editText;
    Integer id_post;

    Button updateBtn;
    Button deleteBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.edit_post, container, false);

        editText = root.findViewById(R.id.eshow_text);
        editTitle = root.findViewById(R.id.eshow_title);

        updateBtn = root.findViewById(R.id.eshow_update);
        deleteBtn = root.findViewById(R.id.eshow_delete);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        if(InternetUtil.isInternetOnline(getActivity())){
            Bundle bundle = this.getArguments();
            if(bundle!=null){
                id_post = bundle.getInt("post_id");
                GetServerData(id_post);
            }
        }

        return root;
    }

    private void GetServerData(Integer getterid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        String id = String.valueOf(getterid);
        final PostApi postApi = retrofit.create(PostApi.class);
        Call<PostModel> call = postApi.getPost(id);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        PostModel postModel = response.body();
                        editText.setText(postModel.getText());
                        editTitle.setText(postModel.getTitle());
                    }
                }else{
                    Log.d("fail", "onResponse: failed");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Log.d("failed", "onFailure: "+t.getMessage()==null?"":t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.eshow_update:
                updateBtnClicked();
                break;
            case R.id.eshow_delete:
                deleteBtnClicked();       }

    }

    private void updateBtnClicked() {
        if(!isEditUserUpdateDetailsEmpty()){
            if(InternetUtil.isInternetOnline(getActivity())){
                updatePost();
            }
        }else{
            Toast.makeText(getActivity(), "Pls check empty fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBtnClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Post");
        builder.setMessage("Are you sure you want to delete post?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePost();
                dialog.dismiss();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void deletePost() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.base_local)
                .addConverterFactory(GsonConverterFactory.create()).build();

        String queryToken = SharedDataGetSet.getMySavedToken(getActivity());
        String id_Userpost = String.valueOf(id_post);
        PostApi postApi = retrofit.create(PostApi.class);
        Call <List<PostModel>> call = postApi.deletePost(queryToken,id_Userpost);

        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        Log.d("good", "onResponse: Good");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                Log.d("fail", "onFailure: "+t.getMessage()==null?"":t.getMessage());
            }
        });


    }

    private void updatePost() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.base_local)
                .addConverterFactory(GsonConverterFactory.create()).build();
        Integer user_id_post = 1;
        String user_text = editText.getText().toString();
        String user_title = editTitle.getText().toString();



        PostModel postModel = new PostModel(user_id_post,user_title,user_text,1);

        String queryToken = SharedDataGetSet.getMySavedToken(getActivity());
        String Userpost_id = String.valueOf(id_post);
        PostApi postApi =retrofit.create(PostApi.class);
        Call <PostModel> call = postApi.updatePost(queryToken,postModel,Userpost_id);
        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        Toast.makeText(getActivity(), "Successfully updated", Toast.LENGTH_SHORT).show();
                        Log.d("Good", "onResponse: Successfully updated");
                    }
                }else{
                    Log.d("Failed", "onResponse: Failed");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Log.d("Failed", "onFailure: failed");
            }
        });

    }

    private  Boolean isEditUserUpdateDetailsEmpty(){
        if(editTitle.getText().toString().isEmpty()||editText.getText().toString().isEmpty()){
            Toast toast = Toast.makeText(getActivity(), "Pls fill empty fields", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return true;
        }else{
            return false;
        }
    }


}
