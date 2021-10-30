package com.track.trackingapp.Adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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

import com.track.trackingapp.Activities.UserMapActivity;
import com.track.trackingapp.GlobalClass.Constants;
import com.track.trackingapp.GlobalClass.PreferenceHelper;
import com.track.trackingapp.R;
import com.track.trackingapp.models.LoginModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private Context mContext;
    private List<LoginModel> participantsListModels;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }


    public UserListAdapter(Context mContext, List<LoginModel> albumList) {
        this.mContext = mContext;
        this.participantsListModels = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.username.setText("User Name : " + participantsListModels.get(position).getFirst_name() +
                " " + participantsListModels.get(position).getLast_name());
//        int index = position + 1;
//        holder.categorytype.setText("Category Type : " + participantsListModels.get(position).getCategory());


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_name = participantsListModels.get(position).getFirst_name() +
                        " " + participantsListModels.get(position).getLast_name();
                Intent intent = new Intent(mContext, UserMapActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                PreferenceHelper.putString(Constants.clicked_id, String.valueOf(participantsListModels.get(position).getUser_id()));
                PreferenceHelper.putString(Constants.clicked_name, str_name);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return participantsListModels.size();
    }

}
