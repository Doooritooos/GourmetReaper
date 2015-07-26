package com.edu.cmu.gourmetreaper.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dbLayout.RestaurantReviewDAO;
import com.edu.cmu.gourmetreaper.dbLayout.daoImplementation.RestaurantReviewDAOImpl;
import com.edu.cmu.gourmetreaper.entities.RestaurantReview;

/**
 * Author: Qianwen Li
 * Team: Gourmet Reapers
 * */
public class HomeActivity extends Activity {

    EditText comInput;
    TextView comText1;
    RestaurantReviewDAO rrd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        comInput = (EditText) findViewById(R.id.comInput);
        comText1 = (TextView) findViewById(R.id.comText1);
        rrd = new RestaurantReviewDAOImpl(this);
        showReview();
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
