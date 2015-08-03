package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dbLayout.CuisineDAO;
import com.edu.cmu.gourmetreaper.dbLayout.RestaurantReviewDAO;
import com.edu.cmu.gourmetreaper.dbLayout.daoImplementation.CuisineDAOImpl;
import com.edu.cmu.gourmetreaper.dbLayout.daoImplementation.RestaurantReviewDAOImpl;
import com.edu.cmu.gourmetreaper.entities.Cuisine;
import com.edu.cmu.gourmetreaper.entities.RestaurantReview;

import java.util.List;

/**
 * Author: Qianwen Li
 * Team: Gourmet Reapers
 * */
public class HomeActivity extends Activity {

    private final static String TAG = "DorisDebug";
    private RestaurantReviewDAO rrd;
    private CuisineDAO cd;

    ImageView recImg1;
    ImageView recImg2;
    ImageView recImg3;
    TextView recText1;
    TextView recText2;
    TextView recText3;

    ImageView popImg1;
    ImageView popImg2;
    ImageView popImg3;
    TextView popText1;
    TextView popText2;
    TextView popText3;

    EditText comInput;
    TextView comText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rrd = new RestaurantReviewDAOImpl(this);
        cd = new CuisineDAOImpl(this);
        initRecAndPop();
        initReview();
        showReview();
    }

    private void initRecAndPop() {
        List<Cuisine> allList = cd.getAllCuisine();
        Log.v(TAG, "allList.size = " + allList.size());
        // recommendation
        recImg1 = (ImageView) findViewById(R.id.recImg1);
        recImg2 = (ImageView) findViewById(R.id.recImg2);
        recImg3 = (ImageView) findViewById(R.id.recImg3);
        recText1 = (TextView) findViewById(R.id.recText1);
        recText2 = (TextView) findViewById(R.id.recText2);
        recText3 = (TextView) findViewById(R.id.recText3);
        recImg1.setImageBitmap(null);
        recImg2.setImageBitmap(null);
        recImg3.setImageBitmap(null);

        recImg1.setImageBitmap(allList.get(0).getImage());
        recText1.setText(allList.get(0).getCuisineName());
        recImg2.setImageBitmap(allList.get(2).getImage());
        recText2.setText(allList.get(2).getCuisineName());
        recImg3.setImageBitmap(allList.get(6).getImage());
        recText3.setText(allList.get(6).getCuisineName());

        // Popular
        popImg1 = (ImageView) findViewById(R.id.popImg1);
        popImg2 = (ImageView) findViewById(R.id.popImg2);
        popImg3 = (ImageView) findViewById(R.id.popImg3);
        popText1 = (TextView) findViewById(R.id.popText1);
        popText2 = (TextView) findViewById(R.id.popText2);
        popText3 = (TextView) findViewById(R.id.popText3);
        popImg1.setImageBitmap(null);
        popImg2.setImageBitmap(null);
        popImg3.setImageBitmap(null);

        popImg1.setImageBitmap(allList.get(1).getImage());
        popText1.setText(allList.get(1).getCuisineName());
        popImg2.setImageBitmap(allList.get(4).getImage());
        popText2.setText(allList.get(4).getCuisineName());
        popImg3.setImageBitmap(allList.get(5).getImage());
        popText3.setText(allList.get(5).getCuisineName());

    }

    private void initReview() {
        comInput = (EditText) findViewById(R.id.comInput);
        comText1 = (TextView) findViewById(R.id.dishComText1);
    }

    public void submitRestReview(View view) {
        RestaurantReview rr = new RestaurantReview();
        rr.setRating(5);
        rr.setComment(comInput.getText().toString());
        rrd.insertRestaurantReview(rr);
        showReview();
    }

    public void shareFB(View view) {
        startActivity(new Intent(this, ConnectFBActivity.class));
    }

    public void showReview() {
        if (rrd.getAllRestaurantReview() != null && rrd.getAllRestaurantReview().size() > 0) {
            RestaurantReview  toprr = rrd.getRestaurantReviewByID(rrd.getAllRestaurantReview().size());
            if (toprr != null)
                comText1.setText(toprr.toString());
        } else {
            comText1.setText("There is no comments yet");
        }
    }
}
