package edu.zjut.zzy.ticklist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.bean.RankInfo;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {
    private static final String TAG = RankAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<RankInfo> data;

    public RankAdapter(Context context, ArrayList<RankInfo> data){
        this.context = context;
        this.data = data;
    }

    //加载布局文件
    @NonNull
    @Override
    public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.rank_item, null);
        return new RankViewHolder(view);
    }

    //绑定数据
    @Override
    public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
        RankInfo rankInfo = data.get(position);
        holder.rankNumber.setText(String.valueOf(rankInfo.getRankNumber()));
        holder.userName.setText(rankInfo.getUserName());
        holder.userFocusTime.setText(String.valueOf(rankInfo.getTotalFocusTime()));
        //加载图片
        Glide.with(context).load(rankInfo.getImageUrl()).into(holder.rankImage);
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUpdateData(ArrayList<RankInfo> rankInfos) {
        this.data = rankInfos;
        notifyDataSetChanged();
    }


    public class RankViewHolder extends RecyclerView.ViewHolder{
        private TextView rankNumber;
        private ShapeableImageView rankImage;
        private TextView userName;
        private TextView userFocusTime;

        public RankViewHolder(@NonNull View itemView) {
            super(itemView);
            //绑定ui
            rankNumber = itemView.findViewById(R.id.rank_number);
            rankImage = itemView.findViewById(R.id.rank_image);
            userName = itemView.findViewById(R.id.user_name);
            userFocusTime = itemView.findViewById(R.id.user_focus_time);
        }
    }
}
