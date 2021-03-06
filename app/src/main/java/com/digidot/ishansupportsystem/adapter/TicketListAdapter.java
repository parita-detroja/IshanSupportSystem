package com.digidot.ishansupportsystem.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.HomeActivity;
import com.digidot.ishansupportsystem.fragment.ViewTicketFragment;
import com.digidot.ishansupportsystem.model.Ticket;
import com.digidot.ishansupportsystem.util.Constant;

import java.util.List;

/**
 * Created by riddhi.parkhiya on 11/05/2018.
 */

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.ViewHolder> {
    private List<Ticket> ticketList;
    private Activity activity;

    public TicketListAdapter(Activity activity, List<Ticket> notificationList) {
        this.ticketList = notificationList;
        this.activity = activity;
    }

    @Override
    public TicketListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_ticket_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TicketListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.mTextViewTicketNo.setText(ticketList.get(i).getStrTicketNo());
        viewHolder.mTextViewFault.setText(ticketList.get(i).getStrFault());
        viewHolder.mTextViewDate.setText(ticketList.get(i).getStrTicketDate());
        viewHolder.mTextviewOfficeValue.setText(ticketList.get(i).getStrOfficeName());
        viewHolder.mTextviewClientValue.setText(ticketList.get(i).getStrFullName());
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextViewTicketNo;
        private TextView mTextViewFault;
        private TextView mTextViewDate;
        private TextView mTextviewOfficeValue;
        private TextView mTextviewClientValue;

        public ViewHolder(View view) {
            super(view);
            mTextViewTicketNo = view.findViewById(R.id.textViewTicketNo);
            mTextViewFault = view.findViewById(R.id.textViewFault);
            mTextViewDate = view.findViewById(R.id.textViewDate);
            mTextviewClientValue=view.findViewById(R.id.textviewClientValue);
            mTextviewOfficeValue=view.findViewById(R.id.textviewOfficeValue);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int itemPosition=getAdapterPosition();
                    FragmentManager fragmentManager = ((HomeActivity)activity).getSupportFragmentManager();
                    ViewTicketFragment mViewTicketFragment = new ViewTicketFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.INTENT_PARAM_TICKET_ID,ticketList.get(itemPosition).getIntTicketId());
                    bundle.putString(Constant.INTENT_PARAM_TICKET_NO,ticketList.get(itemPosition).getStrTicketNo());
                    bundle.putString(Constant.INTENT_PARAM_TICKET_DATE,ticketList.get(itemPosition).getStrTicketDate());
                    bundle.putString(Constant.INTENT_PARAM_TICKET_FAULT,ticketList.get(itemPosition).getStrFault());
                    bundle.putString(Constant.INTENT_PARAM_TICKET_DESCRIPTION,ticketList.get(itemPosition).getStrDescription());
                    bundle.putString(Constant.INTENT_PARAM_TICKET_STATUS,ticketList.get(itemPosition).getStrTicketStatus());
                    bundle.putString(Constant.INTENT_PARAM_TICKET_CLIENT_NAME,ticketList.get(itemPosition).getStrFullName());
                    bundle.putString(Constant.INTENT_PARAM_TICKET_OFFICE_NAME,ticketList.get(itemPosition).getStrOfficeName());
                    mViewTicketFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, mViewTicketFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    public void refreshListData(List<Ticket> notificationListNew){
        ticketList.clear();
        ticketList = notificationListNew;
        this.notifyDataSetChanged();
    }

}