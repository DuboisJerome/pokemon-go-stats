package com.pokemongostats.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.DrawerItem;

import java.util.List;

/**
 * Created by Zapagon on 04/04/2017.
 */
public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> {

    private List<DrawerItem> drawerItemList;
    private int layoutResID;

    public CustomDrawerAdapter(Context context, int layoutResourceID,
                               List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = (TextView) view
                    .findViewById(R.id.drawer_itemName);
            drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }

        DrawerItem dItem = this.drawerItemList.get(position);

        int icId = dItem.getImgResID();
        if (icId >= 0) {
            drawerHolder.icon.setImageDrawable(ContextCompat.getDrawable(getContext(),
                    icId));
        } else {
            drawerHolder.icon.setVisibility(View.INVISIBLE);
        }
        drawerHolder.ItemName.setText(dItem.getItemName());

        return view;
    }

    private static class DrawerItemHolder {
        TextView ItemName;
        ImageView icon;
    }
}