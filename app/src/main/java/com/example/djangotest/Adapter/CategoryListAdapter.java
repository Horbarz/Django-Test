package com.example.djangotest.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.djangotest.CategoryById;
import com.example.djangotest.R;
import com.example.djangotest.model.CategoryModel;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.mViewHolder> {

    private static final String TAG = "CategoryListAdapter";

    private ArrayList<Integer> pkCategory = new ArrayList<>();
    private ArrayList<String> nameCategory = new ArrayList<>();
    private Context context;

    public CategoryListAdapter(ArrayList<Integer> pkCategory, ArrayList<String> nameCategory, Context context) {
        this.pkCategory = pkCategory;
        this.nameCategory = nameCategory;
        this.context = context;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View v = LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.post_list_item, parent, false);
         CategoryListAdapter.mViewHolder holder = new CategoryListAdapter.mViewHolder(v);
         return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {
        holder.mCatName.setText(nameCategory.get(position));
        holder.mCatid.setText(String.valueOf(pkCategory.get(position)));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();
                bundle.putInt("post_id",pkCategory.get(position)); // Put anything what you want
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                android.support.v4.app.Fragment myFragment = new CategoryById();
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
        TextView mCatid;
        TextView mCatName;
        RelativeLayout parentLayout;
        public mViewHolder(View itemView) {
            super(itemView);
            mCatid = itemView.findViewById(R.id.post_listitem_id);
            mCatName = itemView.findViewById(R.id.post_listitem_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
