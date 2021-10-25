package com.track.trackingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.track.trackingapp.Activities.ProductListActivity;
import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.models.CategoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoryModel> participantsListModels;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.categoryname)
        TextView categoryname;
        @BindView(R.id.categorytype)
        TextView categorytype;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


    public CategoryListAdapter(Context mContext, List<CategoryModel> albumList) {
        this.mContext = mContext;
        this.participantsListModels = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.categoryname.setText("Category Name : " + participantsListModels.get(position).getName());
        int index = position + 1;
        holder.categorytype.setText("Category Type : " + participantsListModels.get(position).getType());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceHelper.putString(Constants.category_id, participantsListModels.get(position).getId());

                Intent intent = new Intent(mContext, ProductListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return participantsListModels.size();
    }

}
