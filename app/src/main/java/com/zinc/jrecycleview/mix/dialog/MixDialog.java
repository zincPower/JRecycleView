package com.zinc.jrecycleview.mix.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.diy.UIUtils;
import com.zinc.jrecycleview.mix.Constant;
import com.zinc.jrecycleview.mix.MixActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2018-11-02 21:27
 * email        : 56002982@qq.com
 * desc         : 混合选择列表
 * version      : 1.0.0
 */

public class MixDialog extends AppCompatDialogFragment {

    private RecyclerView mRecycleView;
    private TextView mTvGo;
    private static final List<MixShowInfoData> mData = new ArrayList<>();

    static {
        mData.add(new MixShowInfoData(Constant.REFRESH, "下拉刷新(REFRESH)"));
        mData.add(new MixShowInfoData(Constant.LOAD_MORE, "上拉加载(LOAD_MORE)"));
        mData.add(new MixShowInfoData(Constant.SWIPE, "侧滑(SWIPE)"));
        mData.add(new MixShowInfoData(Constant.ANIM, "动画(ANIM)"));
        mData.add(new MixShowInfoData(Constant.STICK, "粘性(STICK)"));
    }

    public static MixDialog newInstance() {

        Bundle args = new Bundle();

        MixDialog fragment = new MixDialog();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.dialog_mix, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWindows(getDialog().getWindow());

        mRecycleView = view.findViewById(R.id.recycle_view);
        mTvGo = view.findViewById(R.id.tv_go);

        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.setAdapter(new MixAdapter(getContext(), mData));

        mTvGo.setOnClickListener(v -> {
            MixActivity.startActivity(getContext(),
                    mData.get(0).isSelect(),
                    mData.get(1).isSelect(),
                    mData.get(2).isSelect(),
                    mData.get(3).isSelect(),
                    mData.get(4).isSelect()
            );
        });

    }

    private void initWindows(Window window) {
        View decorView = window.getDecorView();
        decorView.setPadding(0, decorView.getPaddingTop(), 0, 0);
        window.setLayout(UIUtils.dip2px(getContext(), 300),
                UIUtils.dip2px(getContext(), 300));
        window.setGravity(Gravity.CENTER);
    }

}
