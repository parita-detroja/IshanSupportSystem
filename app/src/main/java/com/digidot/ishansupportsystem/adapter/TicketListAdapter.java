package com.digidot.ishansupportsystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.activity.ViewTicketActivity;
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
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextViewTicketNo;
        private TextView mTextViewFault;
        private TextView mTextViewDate;

        public ViewHolder(View view) {
            super(view);
            mTextViewTicketNo = view.findViewById(R.id.textViewTicketNo);
            mTextViewFault = view.findViewById(R.id.textViewFault);
            mTextViewDate = view.findViewById(R.id.textViewDate);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(), ViewTicketActivity.class);
                    int itemPosition=getAdapterPosition();
                    intent.putExtra(Constant.INTENT_PARAM_TICKET_ID,ticketList.get(itemPosition).getIntTicketId());
                    intent.putExtra(Constant.INTENT_PARAM_TICKET_NO,ticketList.get(itemPosition).getStrTicketNo());
                    intent.putExtra(Constant.INTENT_PARAM_TICKET_DATE,ticketList.get(itemPosition).getStrTicketDate());
                    intent.putExtra(Constant.INTENT_PARAM_TICKET_FAULT,ticketList.get(itemPosition).getStrFault());
                    intent.putExtra(Constant.INTENT_PARAM_TICKET_DESCRIPTION,ticketList.get(itemPosition).getStrDescription());
                    intent.putExtra(Constant.INTENT_PARAM_TICKET_STATUS,ticketList.get(itemPosition).getStrTicketStatus());
                    v.getContext().startActivity(intent);
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