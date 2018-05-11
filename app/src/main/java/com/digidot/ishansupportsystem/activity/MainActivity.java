package com.digidot.ishansupportsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.digidot.ishansupportsystem.R;
import com.digidot.ishansupportsystem.adapter.TicketListAdapter;
import com.digidot.ishansupportsystem.model.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {
    public static Logger logger=Logger.getLogger("MainActivity");

    private RecyclerView mRecyclerViewTicketList;
    private List<Ticket> mListTicket;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){

        mFloatingActionButton = findViewById(R.id.fab_chart);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this,CreateTicketActivity.class);
                startActivity(mIntent);
            }
        });

        mRecyclerViewTicketList = findViewById(R.id.rvTicketList);
        mRecyclerViewTicketList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        mRecyclerViewTicketList.setLayoutManager(layoutManager);

        mListTicket = new ArrayList<>();
        Ticket ticket = new Ticket();
        ticket.setTicketId("101");
        ticket.setFaultId("Fauld");
        ticket.setDate("11-05-2018");
        mListTicket.add(ticket);
        TicketListAdapter adapter = new TicketListAdapter(getApplicationContext(), mListTicket,
                new TicketListAdapter.OnItemClickListener() {
                    @Override public void onItemClick() {
                        Intent mIntent = new Intent(MainActivity.this,ViewTicketActivity.class);
                    }
                });
        mRecyclerViewTicketList.setAdapter(adapter);
    }


}
