package com.edu.cmu.gourmetreaper.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.edu.cmu.gourmetreaper.R;

public class MainActivity extends TabActivity {
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        // Home tab
        TabHost.TabSpec tabHome = tabHost
                .newTabSpec("Home")
                .setIndicator("Home", resources.getDrawable(R.drawable.icon_home_config))
                .setContent(new Intent().setClass(this, HomeActivity.class));

        // Menu tab
        TabHost.TabSpec tabMenu = tabHost
                .newTabSpec("Menu")
                .setIndicator("Menu", resources.getDrawable(R.drawable.icon_menu_config))
                .setContent(new Intent().setClass(this, MenuActivity.class));

        // Order tab
        TabHost.TabSpec tabOrder = tabHost
                .newTabSpec("Order")
                .setIndicator("Order", resources.getDrawable(R.drawable.icon_order_config))
                .setContent(new Intent().setClass(this, OrderActivity.class));

        // User tab
        TabHost.TabSpec tabUser = tabHost
                .newTabSpec("User")
                .setIndicator("User", resources.getDrawable(R.drawable.icon_user_config))
                .setContent(new Intent().setClass(this, ConnectFBActivity.class));

        // add all tabs
        tabHost.addTab(tabHome);
        tabHost.addTab(tabMenu);
        tabHost.addTab(tabOrder);
        tabHost.addTab(tabUser);

        //set Windows tab as default (zero based)
        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
