package edu.zjut.zzy.ticklist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

import edu.zjut.zzy.ticklist.CInterface.HandToDo;
import edu.zjut.zzy.ticklist.CInterface.SwitchFragment;
import edu.zjut.zzy.ticklist.MainActivity;
import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.SP.UserManager;
import edu.zjut.zzy.ticklist.bean.ToDo;
import edu.zjut.zzy.ticklist.custom.ItemHelper;
import edu.zjut.zzy.ticklist.fragment.ClockFragment;
import edu.zjut.zzy.ticklist.popupwindows.EditPopUpWindow;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> implements ItemHelper, HandToDo {
    private static final String TAG = "ToDoAdapter";
    private Context context;
    private ArrayList<ToDo> data;
    private ToDo editToDo;
    private SwitchFragment switchFragment;

    public void setSwitchFragment(SwitchFragment switchFragment) {
        this.switchFragment = switchFragment;
    }

    public ToDoAdapter(Context context, ArrayList<ToDo> data) {
        this.context = context;
        this.data = data;
    }


    public void setData(ArrayList<ToDo> newData){
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //加载布局文件
        View view = View.inflate(context, R.layout.recyclerviewitem, null);
        return new ToDoViewHolder(view);
    }

    //绑定数据
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo temp = data.get(position);
        holder.checkButton.setText(temp.getContent());
        holder.checkButton.setChecked(temp.isFinish());
//        holder.donutProgress.setProgress(temp.getFinishTime());
//        holder.donutProgress.setMax(temp.getTargetTime());
        holder.editPopUpWindow = new EditPopUpWindow(context, data.get(position), position, ToDoAdapter.this);
        holder.todoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManager userManager = new UserManager(context);
                userManager.setToDoId(data.get(holder.getAdapterPosition()).getKiLinId());
                //跳转到计时Fragment
                switchFragment.switchFragment(3);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
//        ToDo todo = data.remove(fromPosition);
//        data.add(toPosition > fromPosition ? toPosition - 1 : toPosition, todo);
        Collections.swap(data, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void setToDo(ToDo toDo, int i) {
        data.set(i, toDo);
        notifyItemChanged(i);
    }

    public void insertToDo(int position){
        notifyItemInserted(position);
    }


    public class ToDoViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkButton;
//        private DonutProgress donutProgress;
        private ImageView todoButton;
        private RelativeLayout relativeLayout;
        private EditPopUpWindow editPopUpWindow;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            //绑定ui
            checkButton = itemView.findViewById(R.id.check_botton);
//            donutProgress = itemView.findViewById(R.id.donut_progress);
            todoButton = itemView.findViewById(R.id.todo_button);
            relativeLayout = itemView.findViewById(R.id.relativeview);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressLint("RtlHardcoded")
                @Override
                public void onClick(View view) {
                    editPopUpWindow.setBlurBackgroundEnable(true);
                    editPopUpWindow.setKeyboardAdaptive(true);
                    //设置在底部弹出
                    editPopUpWindow.setPopupGravity(Gravity.CENTER|Gravity.BOTTOM);
                    editPopUpWindow.showPopupWindow();
                }
            });

        }
    }
}
