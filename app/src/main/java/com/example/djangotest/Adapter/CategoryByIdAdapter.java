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

import com.example.djangotest.R;
import com.example.djangotest.ShowPost;

import java.util.ArrayList;

public class CategoryByIdAdapter extends RecyclerView.Adapter<CategoryByIdAdapter.mViewHolder> {

    private ArrayList<String> nameCategory;
    private ArrayList<Integer> pkCategory;
    private Context context;

    public CategoryByIdAdapter(ArrayList<String> nameCategory, ArrayList<Integer> pkCategory, Context context) {
        this.nameCategory = nameCategory;
        this.pkCategory = pkCategory;
        this.context = context;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_item, parent, false);

        CategoryByIdAdapter.mViewHolder holder = new CategoryByIdAdapter.mViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {
        holder.mCatid_name.setText(nameCategory.get(position));
        holder.mCatid_id.setText(String.valueOf(pkCategory.get(position)));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("post_id",pkCategory.get(position));
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new ShowPost();
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,myFragment)
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

        TextView mCatid_name;
        TextView mCatid_id;
        RelativeLayout parentLayout;

        public mViewHolder(View itemView) {
            super(itemView);
            mCatid_id = (TextView) itemView.findViewById(R.id.post_listitem_id);
            mCatid_name = (TextView) itemView.findViewById(R.id.post_listitem_name);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
