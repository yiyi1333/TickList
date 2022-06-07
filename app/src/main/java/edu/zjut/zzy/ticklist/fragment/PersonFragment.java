package edu.zjut.zzy.ticklist.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.zjut.zzy.ticklist.R;
import edu.zjut.zzy.ticklist.activity.LoginActivity;

public class PersonFragment extends Fragment {
    private static final String TAG = PersonFragment.class.getSimpleName();
    private View root;
    private Button logoutButton;

    public PersonFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(root == null){
            Log.d(TAG, "---------------oncreatedView");
            root = inflater.inflate(R.layout.fragment_person, container, false);
        }
        bindView();
        bindEvents();

        return root;
    }

    private void bindView(){
        logoutButton = root.findViewById(R.id.logout_button);
    }

    private void bindEvents(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到登录界面
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}