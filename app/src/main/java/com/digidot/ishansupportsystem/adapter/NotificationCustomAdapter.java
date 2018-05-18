package com.digidot.ishansupportsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.model.Notification;
import com.digidot.ishansupportsystem.util.Utils;

import java.util.List;

public class NotificationCustomAdapter extends RecyclerView.Adapter<NotificationCustomAdapter.ViewHolder> {
    private List<Notification> notificationList;
    private Context context;

    public NotificationCustomAdapter(Context context, List<Notification> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @Override
    public NotificationCustomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_item_event, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationCustomAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tvEventCat.setText(notificationList.get(i).getStrNotificationTitle());
        viewHolder.tvEventDes.setText(notificationList.get(i).getStrDetails());
        String datetime= notificationList.get(i).getDtCreatedOn();
        String str[]=datetime.split(" ");
        viewHolder.tvDate.setText(str[0]);
        viewHolder.tvTime.setText(str[1]);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvEventCat;
        private TextView tvDate;
        private TextView tvTime;
        private TextView tvEventDes;

        public ViewHolder(View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.textViewDate);
            tvTime = (TextView) view.findViewById(R.id.textViewTime);
            tvEventDes = (TextView) view.findViewById(R.id.textViewEventDes);
            tvEventCat = (TextView) view.findViewById(R.id.textViewEventCategory);
        }
    }

    public void refreshListData(List<Notification> notificationListNew){
        notificationList.clear();
        notificationList = notificationListNew;
        this.notifyDataSetChanged();
    }

}