package com.jjrz.listimagetext2;

import android.app.DownloadManager;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    private JSONArray myFlowers = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Fruity","1");
        listView = findViewById(R.id.lstMain);
        GetAllProducts();
        Log.i ("Fruity", "myFlowers > " + myFlowers.length());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(),ListdataActivity.class);
                try {
                    intent.putExtra("product",myFlowers.getJSONObject(position).toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class CustomListAdapter extends BaseAdapter {
        JSONArray js;
        CustomListAdapter(JSONArray ja)
        {
            js = ja;
        }
        @Override
        public int getCount() {
            return myFlowers.length();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1 = getLayoutInflater().inflate(R.layout.row_data,null);
            TextView name = view1.findViewById(R.id.fruits);
            ImageView image = view1.findViewById(R.id.images);
            try {
                JSONObject js = (JSONObject) myFlowers.get(position);
                //Log.i("Fruity", "JSON Object > " + position + " > " + js.toString());
                String nameString = MiscStuff.stripHtml(myFlowers.getJSONObject(position).getString("CommonName"));
                String urlString = myFlowers.getJSONObject(position).getString("ImgUrl");
                name.setText(nameString);
                Picasso.with(getBaseContext()).load(urlString).into(image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view1;
        }
    }

    void GetAllProducts()
    {
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        Log.i("Fruity","GetAllProducts Running");
        final JsonArrayRequest jsonArrayRequest  = new JsonArrayRequest
                (Request.Method.POST, "https://cryogenic-islands.000webhostapp.com/flowers/sharedphp/getallproductsjson.php", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        Log.i("Fruity","Count:" + response.length());
                        for(int i = 0; i < response.length(); i++)
                        {
                            try {
                                myFlowers.put(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        CustomListAdapter customAdapter = new CustomListAdapter(myFlowers);
                        listView.setAdapter(customAdapter);

//                        Log.i("Fruity","myFlowers>>" + myFlowers.length());
//                        for(int i = 0; i < myFlowers.length(); i++) {
//                            try {
//                                Log.i("Fruity", "myFlowers:" + myFlowers.getJSONObject(i).toString());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Fruity","Error : " + error.toString());
                    }

                });
        queue.add(jsonArrayRequest);
    }

}
