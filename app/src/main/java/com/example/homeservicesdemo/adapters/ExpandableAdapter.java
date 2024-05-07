package com.example.homeservicesdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.homeservicesdemo.R;
import com.example.homeservicesdemo.models.ServiceDetailsBean;
import com.example.homeservicesdemo.models.VarientBean;

import java.util.ArrayList;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ServiceDetailsBean>  serviceDetailsBeansList;

    private TextView txtName,txtQuantity,txtPrice;
    public ExpandableAdapter(Context context, ArrayList<ServiceDetailsBean> serviceDetailsBeansList) {
        this.context = context;
        this.serviceDetailsBeansList = serviceDetailsBeansList;
    }

    @Override
    public int getGroupCount() {
        return serviceDetailsBeansList.size();
    }

    @Override
    public int getChildrenCount(int parentPosition) {
        ArrayList<VarientBean> varientBeansList = serviceDetailsBeansList.get(parentPosition).getVariant();
        return varientBeansList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return serviceDetailsBeansList.get(groupPosition);
    }

    @Override
    public Object getChild(int parentPosition, int childPosition) {
        ArrayList<VarientBean> varientBeansList =serviceDetailsBeansList.get(parentPosition).getVariant();
        return varientBeansList.get(childPosition);
    }

    @Override
    public long getGroupId(int parentPosition) {
        return parentPosition;
    }

    @Override
    public long getChildId(int parentPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int parentPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ServiceDetailsBean detailsBean = (ServiceDetailsBean) getGroup(parentPosition);
        if (convertView ==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView =layoutInflater.inflate(R.layout.parent_items,null);

        }
        txtName = convertView.findViewById(R.id.parent_item_servicesdetails_txtView_name);
        txtQuantity = convertView.findViewById(R.id.parent_item_servicesdetails_txtView_quantity);
        txtPrice = convertView.findViewById(R.id.parent_item_servicesdetails_txtView_price);

        txtName.setText(detailsBean.getService_name());
        txtQuantity.setText(detailsBean.getService_qty());
        txtPrice.setText(detailsBean.getService_price());

        return convertView;
    }

    @Override
    public View getChildView(int parentPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        VarientBean varientBean = (VarientBean) getChild(parentPosition,childPosition);
        if (convertView ==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView =layoutInflater.inflate(R.layout.child_item,null);

        }
        txtName = convertView.findViewById(R.id.child_item_servicesdetails_txtView_name);
        txtQuantity = convertView.findViewById(R.id.child_item_servicesdetails_txtView_quantity);
        txtPrice = convertView.findViewById(R.id.child_item_servicesdetails_txtView_price);

        txtName.setText(varientBean.getVariant_name());
        txtQuantity.setText(varientBean.getVariant_qty());
        txtPrice.setText(varientBean.getVariant_price());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
