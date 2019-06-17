package com.indusfo.repertorymanage.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.adaptor.HuiZongAdaptor;
import com.indusfo.repertorymanage.bean.Gather;
import com.roamer.slidelistview.SlideListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HuiZongFragment extends Fragment {

    List<Gather> gatherList = new ArrayList<>();
    HuiZongAdaptor huiZongAdaptor;

    Unbinder unbinder;
    @BindView(R.id.listview_2)
    SlideListView huiZongListView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hui_zong, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        gatherList = bundle.getParcelableArrayList("list2");
        huiZongAdaptor = new HuiZongAdaptor(getActivity(), R.layout.listview_item_2, gatherList);
        huiZongListView.setAdapter(huiZongAdaptor);
        huiZongAdaptor.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void UiChange() {
        Bundle bundle = getArguments();
        gatherList = bundle.getParcelableArrayList("list2");
        huiZongAdaptor = new HuiZongAdaptor(getActivity(), R.layout.listview_item_2, gatherList);
        huiZongListView.setAdapter(huiZongAdaptor);
        huiZongAdaptor.notifyDataSetChanged();
    }
}
