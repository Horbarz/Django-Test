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

import com.example.djangotest.Home;
import com.example.djangotest.R;
import com.example.djangotest.ShowPost;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.mViewHolder> {

    private static final String TAG = "homeAdapter";
    private ArrayList<Integer> pkPost = new ArrayList<>();
    private ArrayList<String> namePost = new ArrayList<>();

    private Context context;

    public HomeAdapter(ArrayList<Integer> pkPost, ArrayList<String> namePost, Context context) {
        this.pkPost = pkPost;
        this.namePost = namePost;
        this.context = context;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        HomeAdapter.mViewHolder holder = new HomeAdapter.mViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {
        holder.Tid.setText(String.valueOf(pkPost.get(position)));
        holder.Tname.setText(namePost.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("post_id",pkPost.get(position)); // Put anything what you want
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = new ShowPost();
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
        return pkPost.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        TextView Tid;
        TextView Tname;
        RelativeLayout parentLayout;

        public mViewHolder(View itemView) {
            super(itemView);

            Tid = itemView.findViewById(R.id.post_listitem_id);
            Tname = itemView.findViewById(R.id.post_listitem_name);

            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
