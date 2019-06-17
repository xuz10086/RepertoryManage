package com.indusfo.repertorymanage.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.adaptor.MingXiAdaptor;
import com.indusfo.repertorymanage.bean.Gather;
import com.indusfo.repertorymanage.bean.ProStoreD;
import com.roamer.slidelistview.SlideListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MingXiFragment extends Fragment {

    @BindView(R.id.listview_1)
    SlideListView listview1;

    Unbinder unbinder;
    List<ProStoreD> list = new ArrayList<ProStoreD>();
    MingXiAdaptor mingXiAdaptor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ming_xi, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化数据展示
        Bundle bundle = getArguments();
        list = bundle.getParcelableArrayList("list");
        List<Gather> gatherList = bundle.getParcelableArrayList("list2");
        mingXiAdaptor = new MingXiAdaptor(getActivity(), R.layout.listview_item_1, list, gatherList);

        // 设置listView总是展现最后一行
        listview1.setTranscriptMode(listview1.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listview1.setStackFromBottom(true);

        listview1.setAdapter(mingXiAdaptor);
        mingXiAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * listView UI更新
     *
     * @author xuz
     * @date 2019/6/1 2:49 PM
     * @param []
     * @return void
     */
    public void UiChange() {
        Bundle bundle = getArguments();
        list = bundle.getParcelableArrayList("list");
        List<Gather> list2 = bundle.getParcelableArrayList("list2");
        mingXiAdaptor = new MingXiAdaptor(getActivity(), R.layout.listview_item_1, list, list2);
        listview1.setAdapter(mingXiAdaptor);
        mingXiAdaptor.notifyDataSetChanged();
    }
}
