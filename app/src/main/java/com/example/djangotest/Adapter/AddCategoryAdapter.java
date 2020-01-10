package com.example.djangotest.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.djangotest.R;

import java.util.ArrayList;

public class AddCategoryAdapter extends RecyclerView.Adapter<AddCategoryAdapter.mViewHolder> {

    private static final String TAG = "AddCategoryAdapter";
    private ArrayList<Integer> pkCategory = new ArrayList<>();
    private ArrayList<Integer> idCategory = new ArrayList<>();
    private ArrayList<String> nameCategory = new ArrayList<>();
    private Context context;

    public AddCategoryAdapter(ArrayList<Integer> pkCategory,
                              ArrayList<Integer> idCategory,
                              ArrayList<String> nameCategory,
                              Context context) {
        this.pkCategory = pkCategory;
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.context = context;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
        AddCategoryAdapter.mViewHolder holder = new AddCategoryAdapter.mViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, final int position) {
        holder.TCatid.setText(String.valueOf(pkCategory.get(position)));
        holder.TCatName.setText(nameCategory.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences categoryPrefs = context.getSharedPreferences("SavedCategories", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefCategoryEditor = categoryPrefs.edit();
                prefCategoryEditor.putInt("CategoryId",pkCategory.get(position));
                prefCategoryEditor.commit();

                ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();
                Toast.makeText(context, "category_id: "+pkCategory.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pkCategory.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder{
        TextView TCatid;
        TextView TCatName;
        RelativeLayout parentLayout;

        public mViewHolder(View itemView) {
            super(itemView);

            TCatid = itemView.findViewById(R.id.post_listitem_id);
            TCatName = itemView.findViewById(R.id.post_listitem_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);


        }
    }
}
