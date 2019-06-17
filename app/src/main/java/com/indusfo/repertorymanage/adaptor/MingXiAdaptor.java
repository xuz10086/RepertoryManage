package com.indusfo.repertorymanage.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.bean.Gather;
import com.indusfo.repertorymanage.bean.ProStoreD;
import com.indusfo.repertorymanage.utils.StringUtils;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MingXiAdaptor extends ArrayAdapter<ProStoreD> {
    private Context mContext;
    private List<ProStoreD> mData;
    private int resourceId;
    private List<Gather> gatherList;

    public MingXiAdaptor(Context context, int resource, List<ProStoreD> data, List<Gather> list2) {
        super(context, resource, data);
        mContext = context;
        resourceId = resource;
        mData = data;
        gatherList = list2;
    }

    public void setData(List<ProStoreD> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ProStoreD getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ProStoreD item = getItem(position);
        // 内部类
        ViewHolder viewHolder;

        if (convertView == null) {
            /* 如果converView参数值为null，则使用LayoutInflater去加载布局 */
            convertView = LayoutInflater.from(getContext()).
                    inflate(resourceId, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            /* 否则，直接对converView进行重用 */
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText(item.getVcProStoreCode());
        viewHolder.tv2.setText(item.getVcModel());
        viewHolder.tv3.setText(item.getVcLocaName());
        viewHolder.tv4.setText(item.getVcNum());

        // 点击删除后移除
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(position);
            }
        });

        return convertView;
    }

    /**
     * 删除list的子项
     *
     * @author xuz
     * @date 2019/5/16 1:37 PM
     * @param [position]
     * @return void
     */
    public void remove(int position) {
        ProStoreD item = getItem(position);
        String vcNum = item.getVcNum();
        Integer num = 0;
        mData.remove(position);
        // 更新汇总的集合
        for (Iterator<Gather> iterator=gatherList.iterator(); iterator.hasNext();) {
            Gather gather = iterator.next();
            if (gather.getVcModel().equals(item.getVcModel())) {
                if (StringUtils.isInteger(vcNum)) {
                    num = Integer.valueOf(vcNum);
                }
                int count = gather.getSum()-num;
                // 为0时则删除
                if (count == 0) {
                    iterator.remove();
                } else {
                    gather.setSum(count);
                    gather.setCartons(gather.getCartons()-1);
                }
            }
        }

//        // 更新全局变量
//        myApplication.setProStoreDList(mData);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.text1) TextView tv1;
        @BindView(R.id.text2) TextView tv2;
        @BindView(R.id.text3) TextView tv3;
        @BindView(R.id.text4) TextView tv4;
        @BindView(R.id.delete) TextView delete;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
