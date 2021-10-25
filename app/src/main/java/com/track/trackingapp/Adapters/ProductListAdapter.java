package com.track.trackingapp.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.track.trackingapp.R;
import com.track.trackingapp.models.ProductModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductModel> participantsListModels;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.categoryname)
        TextView categoryname;
        @BindView(R.id.categorytype)
        TextView categorytype;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


    public ProductListAdapter(Context mContext, List<ProductModel> albumList) {
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
        holder.categorytype.setText("Category Type : " + participantsListModels.get(position).getCategory());
    }


    @Override
    public int getItemCount() {
        return participantsListModels.size();
    }

}
