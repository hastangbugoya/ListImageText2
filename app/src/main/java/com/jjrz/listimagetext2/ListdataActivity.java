package com.jjrz.listimagetext2;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ListdataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_listdata);
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(getIntent().getStringExtra("product"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        ImageView img = findViewById(R.id.imgProduct);
        TextView txt = findViewById(R.id.txtName);
        TextView txtDesc = findViewById(R.id.txtDesc);
        Log.i("Fruity","Intents >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + jsonObj.toString());

        String receivedName = null;
        String receivedImage = null;
        String receivedDesc = null;
        try {
            receivedName = MiscStuff.stripHtml(jsonObj.getString("CommonName"));
            receivedImage = jsonObj.getString("ImgUrl");
            receivedDesc = MiscStuff.stripHtml(jsonObj.getString("Description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Fruity","Intents > " + receivedName + " > " + receivedImage);

        Picasso.with(getBaseContext()).load(receivedImage).into(img);;
        txt.setText(receivedName);
        txtDesc.setText(receivedDesc);
    };

}
