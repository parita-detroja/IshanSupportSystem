package com.digidot.ishansupportsystem.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.model.Ticket;
import com.digidot.ishansupportsystem.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class TicketHistoryAdapter extends BaseExpandableListAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<String> headerList;
    private HashMap<String, ArrayList<Ticket>> childList;
    private Context mContext;
    private SharedPreferences pref;

    public TicketHistoryAdapter(Context mContext, ArrayList<String> headerList, HashMap<String, ArrayList<Ticket>> childList) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.headerList = headerList;
        this.childList = childList;
        this.mContext=mContext;
        pref=mContext.getSharedPreferences("IffcoPref",0);
    }

    @Override
    public int getGroupCount() {
        return this.headerList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childList.get(this.headerList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.childList.get(this.headerList.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {

        final ViewholderParentMenu mViewHolder;

        if (view == null) {

            view = mLayoutInflater.inflate(R.layout.row_group_layout, parent, false);

            mViewHolder = new ViewholderParentMenu(view);

            view.setTag(mViewHolder);

        } else {

            mViewHolder = (ViewholderParentMenu) view.getTag();
        }

        mViewHolder.mTextViewHeader.setText((String) getGroup(groupPosition));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup child) {

        ViewHolderChild viewHolderChild;

        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.row_child_layout, child, false);

            viewHolderChild = new ViewHolderChild(view);

            view.setTag(viewHolderChild);

        } else {

            viewHolderChild = (ViewHolderChild) view.getTag();
        }

        Ticket ticket = (Ticket) getChild(groupPosition, childPosition);

        viewHolderChild.mTextviewStatusDateValue.setText(ticket.getDtStatusDate());
        viewHolderChild.mTextviewStatusRemarksValue.setText(ticket.getStrRemarks());
        if (ticket.getStrDependency().equals("")) {
            viewHolderChild.mLinearLayoutBroadCategory.setVisibility(View.VISIBLE);
            viewHolderChild.mLinearLayoutResolutionCode.setVisibility(View.VISIBLE);
            viewHolderChild.mLinearLayoutCaptureImage.setVisibility(View.VISIBLE);
            viewHolderChild.mTextviewResolutionCodeValue.setText(ticket.getStrResolutionCode());
            viewHolderChild.mTextviewBroadCategoryValue.setText(ticket.getStrBroadCategory());
            if(ticket.getStrImage()!=null && ticket.getStrImage().equals("")){
                viewHolderChild.mImageViewCaptureImage.setVisibility(View.VISIBLE);
                viewHolderChild.mTextviewNoimage.setVisibility(View.GONE);
                String temp=pref.getString(Constant.PREF_KEY_WEBSITE_LINK,"");
                Picasso.with(mContext).load(temp+ticket.getStrImage()).into(viewHolderChild.mImageViewCaptureImage);
            }else{
                viewHolderChild.mImageViewCaptureImage.setVisibility(View.GONE);
                viewHolderChild.mTextviewNoimage.setVisibility(View.VISIBLE);
            }

        } else {
            viewHolderChild.mLinearLayoutDependencyCode.setVisibility(View.VISIBLE);
            viewHolderChild.mTextViewDependencyCodeValue.setText(ticket.getStrDependency());
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private class ViewholderParentMenu {
        private TextView mTextViewHeader;

        public ViewholderParentMenu(View view) {
            mTextViewHeader = view.findViewById(R.id.textview_header);
        }
    }

    private class ViewHolderChild {
        private TextView mTextviewStatusDateValue;
        private TextView mTextviewStatusRemarksValue;
        private TextView mTextViewDependencyCodeValue;
        private TextView mTextviewResolutionCodeValue;
        private TextView mTextviewBroadCategoryValue;
        private ImageView mImageViewCaptureImage;
        private TextView mTextviewNoimage;

        private LinearLayout mLinearLayoutDependencyCode;
        private LinearLayout mLinearLayoutResolutionCode;
        private LinearLayout mLinearLayoutBroadCategory;
        private LinearLayout mLinearLayoutCaptureImage;


        public ViewHolderChild(View view) {
            mTextviewStatusDateValue = view.findViewById(R.id.textviewStatusDateValue);
            mTextviewStatusRemarksValue = view.findViewById(R.id.textviewTicketRemarksValue);
            mTextViewDependencyCodeValue = view.findViewById(R.id.textViewDependencyCodeValue);
            mTextviewResolutionCodeValue = view.findViewById(R.id.textviewResolutionCodeValue);
            mTextviewBroadCategoryValue = view.findViewById(R.id.textviewBroadCategoryValue);
            mImageViewCaptureImage=view.findViewById(R.id.imageViewCaptureImage);
            mTextviewNoimage=view.findViewById(R.id.textNoImage);

            mLinearLayoutDependencyCode = view.findViewById(R.id.linearLayoutDependencyCode);
            mLinearLayoutResolutionCode = view.findViewById(R.id.linearLayoutResolutionCode);
            mLinearLayoutBroadCategory = view.findViewById(R.id.linearLayoutBroadCategory);
            mLinearLayoutCaptureImage = view.findViewById(R.id.linearLayoutCaptureImage);
        }
    }
}