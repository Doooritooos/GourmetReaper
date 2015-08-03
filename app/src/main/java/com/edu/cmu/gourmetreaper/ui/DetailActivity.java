package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.app.TabActivity;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dbLayout.CuisineDAO;
import com.edu.cmu.gourmetreaper.dbLayout.CuisineReviewDAO;
import com.edu.cmu.gourmetreaper.dbLayout.daoImplementation.CuisineDAOImpl;
import com.edu.cmu.gourmetreaper.dbLayout.daoImplementation.CuisineReviewDAOImpl;
import com.edu.cmu.gourmetreaper.entities.Cuisine;
import com.edu.cmu.gourmetreaper.entities.RestaurantReview;


public class DetailActivity extends Activity {
    private CuisineDAO cd;
    private CuisineReviewDAO crd;
    TextView dishTitle, disText;
    ImageView dishImg1, dishImg2;
    EditText dishComInput;
    TextView dishComText1;
    TextView dishComText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        showDish();
        initReview();
        showReview();
    }


    public void showDish() {
        dishTitle = (TextView) findViewById(R.id.dishTitle);
        disText = (TextView) findViewById(R.id.disText);
        dishImg1 = (ImageView) findViewById(R.id.dishImg1);
        cd = new CuisineDAOImpl(this);
        Bundle dishMsg = getIntent().getExtras();
        if (dishMsg == null) {
            return;
        }
        String dishName = dishMsg.getString("dishName");
        Cuisine c = cd.getCuisineByName(dishName);
        dishTitle.setText(c.getCuisineName());
        disText.setText(c.getCuisineDescription());
        dishImg1.setImageBitmap(c.getImage());

    }


    public void initReview() {
        crd = new CuisineReviewDAOImpl(this);
        dishComInput = (EditText) findViewById(R.id.dishComInput);
        dishComText1 = (TextView) findViewById(R.id.dishComText1);
        dishComText2 = (TextView) findViewById(R.id.dishComText2);

    }

    public void showReview() {


    }


    public void submitReview(View view) {

    }


}
