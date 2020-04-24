package com.example.myweather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String maVille = "essaouira";

    TextView ville,temp_min,temp_max,pression,humidite,wind,temp2;
    ImageView image;
    String photo_url_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        temp_max = findViewById(R.id.tmax);
        temp_min = findViewById(R.id.tmin);
        pression = findViewById(R.id.pression);
        humidite = findViewById(R.id.humidite);
        image = findViewById(R.id.imageView);
        temp2 = findViewById(R.id.temp2);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String url="http://api.openweathermap.org/data/2.5/weather?q="+query+"&appid=5bd7e048cf1ef62c79254f75dfe27d19";
                System.out.println("######## Ville saisie est : "+query);
                ville = findViewById(R.id.ville);
                ville.setText(query.substring(0, 1).toUpperCase() + query.substring(1));
                StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("#### oui");
                        System.out.println("### resp = "+response);
                        try {
                            JSONObject res = new JSONObject(response);
                            System.out.println(res);
                            JSONObject main = res.getJSONObject("main");
                            JSONObject wind = res.getJSONObject("wind");
                            int Temp = (int) (main.getDouble("temp")-273.15);
                            int TempMin = (int) (main.getDouble("temp_min")-273.15);
                            int TempMax = (int) (main.getDouble("temp_max")-273.15);
                            int speed = (int) (wind.getDouble("speed"));
                            int Pression = (int) (main.getDouble("pressure"));
                            int Humidity = (int) (main.getDouble("humidity"));

                            System.out.println("speed = "+speed);
                            System.out.println("humidity = "+Humidity);
                                temp2.setText(String.valueOf(TempMax)+" °C ");
                                temp_max.setText(String.valueOf(speed)+" Km/h ");
                                temp_min.setText(String.valueOf(TempMin)+" °C ");
                                pression.setText(String.valueOf(Pression)+" Pa ");
                                humidite.setText(String.valueOf(Humidity)+" % ");

                            JSONObject weather = res.getJSONArray("weather").getJSONObject(0);
                            System.out.println("######## weather state = "+weather.getString("main"));
                            photo_url_str="http://openweathermap.org/img/wn/"+weather.getString("icon")+"@2x.png";
                            Picasso.get().load(photo_url_str).resize(300, 300).into(image);

                        } catch (JSONException e) {
                            e.printStackTrace();
                       }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("#### no");
                        ville.setText("Ville Incorrecte");
                        temp2.setText("");
                        temp_max.setText("");
                        temp_min.setText("");
                        pression.setText("");
                        humidite.setText("");
                    }
                });
                requestQueue.add(stringRequest1);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


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
