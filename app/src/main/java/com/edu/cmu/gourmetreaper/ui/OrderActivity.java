package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.edu.cmu.gourmetreaper.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OrderActivity extends Activity {
    private TextView orderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        showDate();
        showChosenList();

    }

    public void showChosenList() {
        ArrayList<String> chosenList = getIntent().getStringArrayListExtra("chosenList");
        if (chosenList == null) return;
        ListAdapter myAdapter = new CustomAdapter(this, chosenList);
        ListView myListView = (ListView) findViewById(R.id.myListView);
        myListView.setAdapter(myAdapter);
    }

    public void showDate() {
        orderTitle = (TextView) findViewById(R.id.orderTitle);
        Date curDate = new Date( );
        SimpleDateFormat ft =
                new SimpleDateFormat ("MM/dd/yyyy");
        orderTitle.setText("Your order on " + ft.format(curDate));

    }
}
