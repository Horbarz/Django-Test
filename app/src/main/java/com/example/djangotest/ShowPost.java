package com.example.djangotest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.djangotest.model.PostModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowPost extends Fragment {

    TextView showPostTitle;
    TextView showPostText;
    TextView showPostId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.show_post, container, false);

        showPostTitle = root.findViewById(R.id.vshow_title);
        showPostText = root.findViewById(R.id.vshow_text);
        showPostId = root.findViewById(R.id.vshow_id);

        Bundle bundle = this.getArguments();
        if(bundle != null ){
            Integer bundle_id = bundle.getInt("post_id");
            GetServerData(bundle_id);
        }

        getActivity().setTitle("Show Post");
        return root;
    }

    private void GetServerData(Integer getted_id) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(PostApi.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        String data = String.valueOf(getted_id);
        final PostApi postApi = retrofit.create(PostApi.class);

        Call<PostModel> call = postApi.getPost(data);

        call.enqueue(new Callback<PostModel>() {
            @Override
            public void onResponse(Call<PostModel> call, Response<PostModel> response) {
                if(response.isSuccessful()){
                    if(response.body()!=null){
                        PostModel postValues = response.body();

                        String mShowPostTitle = postValues.getTitle();
                        String mShowText = postValues.getText();
                        String mShowId = String.valueOf(postValues.getId());

                        showPostTitle.setText(mShowPostTitle);
                        showPostText.setText(mShowText);
                        showPostId.setText(mShowId);

                    }
                }else{
                    Log.d("Fail", "onResponse: Fail");
                }
            }

            @Override
            public void onFailure(Call<PostModel> call, Throwable t) {
                Log.d("fail", "onFailure: "+t.getMessage()==null?"":t.getMessage());
            }
        });

    }
}
