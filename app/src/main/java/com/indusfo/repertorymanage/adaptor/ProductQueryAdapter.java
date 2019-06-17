package com.indusfo.repertorymanage.adaptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.bean.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductQueryAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private List<Product> mData;
    private int resourceId;

    public ProductQueryAdapter(@NonNull Context context, int resource, @NonNull List<Product> data) {
        super(context, resource, data);
        mContext = context;
        resourceId = resource;
        mData = data;
    }

    public void setData(List<Product> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData!=null?mData.size():0;
    }

    @Override
    public Product getItem(int position) {
        return mData!=null?mData.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product item = getItem(position);
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
        viewHolder.tv1.setText("产品型号：" + item.getVcModel());

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
        @BindView(R.id.listview_item)
        TextView tv1;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
