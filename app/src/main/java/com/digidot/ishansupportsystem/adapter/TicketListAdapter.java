package com.digidot.ishansupportsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.model.Ticket;

import java.util.List;

/**
 * Created by riddhi.parkhiya on 11/05/2018.
 */

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.ViewHolder> {
    private List<Ticket> ticketList;
    private Context context;

    public TicketListAdapter(Context context, List<Ticket> notificationList) {
        this.ticketList = notificationList;
        this.context = context;
    }

    @Override
    public TicketListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_ticket_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TicketListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.mTextViewTicketId.setText(ticketList.get(i).getTicketId());
        viewHolder.mTextViewFault.setText(ticketList.get(i).getFaultId());
        viewHolder.mTextViewDate.setText(ticketList.get(i).getDate());
        viewHolder.mImageViewCancelTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.mImageViewUpdateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextViewTicketId;
        private TextView mTextViewFault;
        private TextView mTextViewDate;
        private ImageView mImageViewUpdateTicket;
        private ImageView mImageViewCancelTicket;

        public ViewHolder(View view) {
            super(view);
            mTextViewTicketId = view.findViewById(R.id.textViewTicketId);
            mTextViewFault = view.findViewById(R.id.textViewFault);
            mTextViewDate = view.findViewById(R.id.textViewDate);
            mImageViewUpdateTicket = view.findViewById(R.id.imageUpdateTicket);
            mImageViewCancelTicket = view.findViewById(R.id.imageCancelTicket);
        }
    }

    public void refreshListData(List<Ticket> notificationListNew){
        ticketList.clear();
        ticketList = notificationListNew;
        this.notifyDataSetChanged();
    }

}