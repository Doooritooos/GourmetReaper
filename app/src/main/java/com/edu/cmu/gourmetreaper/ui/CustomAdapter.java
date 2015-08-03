package com.edu.cmu.gourmetreaper.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.edu.cmu.gourmetreaper.dbLayout.CuisineDAO;

import com.edu.cmu.gourmetreaper.R;
import com.edu.cmu.gourmetreaper.dbLayout.daoImplementation.CuisineDAOImpl;
import com.edu.cmu.gourmetreaper.entities.Cuisine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Doris on 8/3/15.
 */
class CustomAdapter extends ArrayAdapter<String> implements AdapterView.OnItemSelectedListener {
    private CuisineDAO cd;

    public CustomAdapter(Context context, List<String> chosenList) {
        super(context, R.layout.custom_row, chosenList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.custom_row, parent, false);
        cd = new CuisineDAOImpl(parent.getContext());
        String dishName = getItem(position);
        Cuisine c = cd.getCuisineByName(dishName);

        ImageView dishImg = (ImageView) customView.findViewById(R.id.dishImg);
        TextView nameText = (TextView) customView.findViewById(R.id.nameText);
        TextView descripText = (TextView) customView.findViewById(R.id.descripText);
        TextView priceText = (TextView) customView.findViewById(R.id.priceText);
        Spinner quanSpinner = (Spinner) customView.findViewById(R.id.quanSpinner
        );

        dishImg.setImageBitmap(c.getImage());
        nameText.setText(c.getCuisineName());
        descripText.setText(c.getCuisineDescription());
        priceText.setText("$ " + c.getPrice());

        // Month Spinner elements

        String[] quanArr = {"1", "2", "3", "4", "5", "6", "7", "8","9", "10"};
        ArrayList<String> quanList = new ArrayList<>(Arrays.asList(quanArr));
        // Creating adapter for spinner
        ArrayAdapter<String> quanAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, quanList);
        // Specify the layout to use when the list of choices appears
        quanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        quanSpinner.setAdapter(quanAdapter);
        quanSpinner.setSelection(0);
        quanSpinner.setOnItemSelectedListener(this);

        return customView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
