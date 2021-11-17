package com.track.trackingapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.track.trackingapp.Activities.ProductListActivity;
import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.models.CategoryModel;
import com.track.trackingapp.models.TaskModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {

    private Context mContext;
    private List<TaskModel> participantsListModels;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtDesc)
        TextView txtDesc;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtassignuser)
        TextView txtassignuser;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


    public TaskListAdapter(Context mContext, List<TaskModel> albumList) {
        this.mContext = mContext;
        this.participantsListModels = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {

        holder.txtTitle.setText("Title : " + participantsListModels.get(position).getTitle());
        holder.txtDesc.setText("Description : " + participantsListModels.get(position).getDescription());
        holder.txtassignuser.setText("Assigned User : " + participantsListModels.get(position).getAssigned_user());

        //        holder.categorytype.setText("Category Type : " + participantsListModels.get(position).getType());
//        Glide.with(mContext)
//                .load(participantsListModels.get(position).getImage())
//                .into(holder.img);


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PreferenceHelper.putString(Constants.category_id, participantsListModels.get(position).getId());
//
//                Intent intent = new Intent(mContext, ProductListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return participantsListModels.size();
    }

}
