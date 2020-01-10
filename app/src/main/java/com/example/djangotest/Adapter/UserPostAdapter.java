package com.example.djangotest.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.djangotest.Edit;
import com.example.djangotest.R;

import java.util.ArrayList;

public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.mViewHolder> {

    private ArrayList<Integer> pkCategory = new ArrayList<>();
    private ArrayList<String> nameCategory = new ArrayList<>();

    private Context context;

    public UserPostAdapter(ArrayList<Integer> pkCategory, ArrayList<String> nameCategory, Context context) {
        this.pkCategory = pkCategory;
        this.nameCategory = nameCategory;
        this.context = context;

    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_item, parent, false);
        UserPostAdapter.mViewHolder holder = new UserPostAdapter.mViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {
        holder.userPost_id.setText(String.valueOf(pkCategory.get(position)));
        holder.userPost_name.setText(nameCategory.get(position));
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("post_id",pkCategory.get(position));
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                Fragment myFragment = new Edit();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, myFragment)
                        .addToBackStack(null)
                        .commit();


            }
        });

    }

    @Override
    public int getItemCount() {
        return pkCategory.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        TextView userPost_id;
        TextView userPost_name;
        RelativeLayout parent_layout;

        public mViewHolder(View itemView) {
            super(itemView);
            userPost_name = itemView.findViewById(R.id.post_listitem_name);
            userPost_id = itemView.findViewById(R.id.post_listitem_id);
            parent_layout =itemView.findViewById(R.id.parent_layout);
        }
    }
}
