package com.zinc.jrecycleview.anim.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.anim.AnimActivity;
import com.zinc.jrecycleview.anim.AnimFactory;
import com.zinc.jrecycleview.diy.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2018-11-02 21:27
 * email        : 56002982@qq.com
 * desc         : 动画选择器
 * version      : 1.0.0
 */

public class AnimDialog extends AppCompatDialogFragment implements AnimAdapter.AnimListener {

    private RecyclerView mRecycleView;
    private static final List<AnimData> mData = new ArrayList<>();

    static {
        mData.add(new AnimData(AnimFactory.SLIDE_TOP, "SLIDE_TOP"));
        mData.add(new AnimData(AnimFactory.SLIDE_RIGHT, "SLIDE_RIGHT"));
        mData.add(new AnimData(AnimFactory.SLIDE_BOTTOM, "SLIDE_BOTTOM"));
        mData.add(new AnimData(AnimFactory.SLIDE_LEFT, "SLIDE_LEFT"));
        mData.add(new AnimData(AnimFactory.SCALE, "SCALE"));
        mData.add(new AnimData(AnimFactory.ALPHA, "ALPHA"));
    }

    public static AnimDialog newInstance() {
        Bundle bundle = new Bundle();

        AnimDialog fragment = new AnimDialog();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BitFrameDialog);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_anim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWindows(getDialog().getWindow());

        mRecycleView = view.findViewById(R.id.recycle_view);

        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.setAdapter(new AnimAdapter(getContext(), mData, this));

    }

    private void initWindows(Window window) {
        View decorView = window.getDecorView();
        decorView.setPadding(0, decorView.getPaddingTop(), 0, 0);
        window.setLayout(UIUtils.dip2px(getContext(), 300),
                UIUtils.dip2px(getContext(), 300));
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onItemClick(int type) {
        AnimActivity.startActivity(getContext(), type);
        dismiss();
    }
}
