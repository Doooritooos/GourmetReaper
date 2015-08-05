package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class OrderActivity extends Activity {
    private TextView orderTitle, totalPriceTitle, noOrderTitle;
    private ArrayList<String> chosenList;
    private ListAdapter myAdapter;
    private ListView myListView;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        chosenList = new ArrayList<>();
        mSettings = this.getSharedPreferences("Settings", 0);
        editor = mSettings.edit();
        showDate();
        showChosenList();

    }

    public void showChosenList() {
        if ((mSettings.getStringSet("orderSet", null) == null) && getIntent().getStringArrayListExtra("chosenList") == null) {
            noOrderTitle = (TextView) findViewById(R.id.noOrderTitle);
            noOrderTitle.setText("You have no order yet today!");
            return;
        } else if ((mSettings.getStringSet("orderSet", null) != null) && getIntent().getStringArrayListExtra("chosenList") == null) {
            chosenList = new ArrayList<>(mSettings.getStringSet("orderSet", null));
        } else if (getIntent().getStringArrayListExtra("chosenList") != null) {
            chosenList = getIntent().getStringArrayListExtra("chosenList");
            editor.clear();
            editor.commit();
            editor.putStringSet("orderSet", new HashSet<>(chosenList));
            editor.commit();
        }
        myAdapter = new CustomAdapter(this, chosenList);
        myListView = (ListView) findViewById(R.id.myListView);
        myListView.setAdapter(myAdapter);
    }

    public void showDate() {
        orderTitle = (TextView) findViewById(R.id.orderTitle);
        Date curDate = new Date( );
        SimpleDateFormat ft =
                new SimpleDateFormat ("MM/dd/yyyy");
        orderTitle.setText("Your order on " + ft.format(curDate));

    }

    public void sumUp(Map<String, Double> map) {
        totalPriceTitle = (TextView) findViewById(R.id.totalPriceTitle);
        double total = 0;
        for (Double price : map.values()) {
            total += price;
        }
        totalPriceTitle.setText("Total Price: $ " + total);
    }
}
