package com.indusfo.repertorymanage.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.bean.Gather;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HuiZongAdaptor extends ArrayAdapter<Gather> {

    private Context mContext;
    private List<Gather> mData;
    private int resourceId;

    public HuiZongAdaptor(Context context, int resource, List<Gather> data) {
        super(context, resource, data);
        mContext = context;
        resourceId = resource;
        mData = data;
    }

    public void setData(List<Gather> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Gather getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gather item = getItem(position);
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
        viewHolder.tv1.setText(item.getVcModel());
        viewHolder.tv2.setText(item.getSum()+"");
        viewHolder.tv3.setText(item.getCartons()+"");

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
        mData.remove(position);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @BindView(R.id.text1)
        TextView tv1;
        @BindView(R.id.text2)
        TextView tv2;
        @BindView(R.id.text3)
        TextView tv3;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
