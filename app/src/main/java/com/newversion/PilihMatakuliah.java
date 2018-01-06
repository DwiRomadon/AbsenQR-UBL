package com.newversion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.absenubl.AbsensiUBL;
import com.project.absenubl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pojo.Adapter;
import pojo.AdapterMatakuliah;
import pojo.DataMatakuliah;
import pojo.DataMhsAbsen;
import server.AppController;
import server.Config_URL;

public class PilihMatakuliah extends AppCompatActivity {

    ProgressDialog pDialog;

    AdapterMatakuliah adapter;
    ListView list;

    ArrayList<DataMatakuliah> newsList = new ArrayList<DataMatakuliah>();

    String kodeHari = null;

    String kdmk;
    Calendar calendar;
    SimpleDateFormat dayFormat;
    SimpleDateFormat df,df1,df2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_matakuliah);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Pilih Matakuliah");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        list = (ListView) findViewById(R.id.list_news);
        newsList.clear();


        adapter = new AdapterMatakuliah(PilihMatakuliah.this, newsList);
        list.setAdapter(adapter);

        Intent intent = getIntent();
        final String nidn   = intent.getStringExtra("nidn");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(PilihMatakuliah.this, NewAbsen.class);
                intent.putExtra("nidn", nidn);
                intent.putExtra("kodemk", newsList.get(position).getNomk());
                //intent.putExtra("publisher", dataNya.get(position).getPublisher());
                startActivity(intent);
            }
        });

        //txtJamAwal.setText(df.format(calendar.getTime()));
        calendar = Calendar.getInstance();
        dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        df = new SimpleDateFormat("HH:mm:ss");
        df1 = new SimpleDateFormat("yyyy-MM-dd");
        df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(dayFormat.format(calendar.getTime()).equals("Sunday")){
            kodeHari = "7";
        }else if(dayFormat.format(calendar.getTime()).equals("Monday"))  {
            kodeHari = "1";
        }else if(dayFormat.format(calendar.getTime()).equals("Tuesday"))  {
            kodeHari = "2";
        }else if(dayFormat.format(calendar.getTime()).equals("Wednesday"))  {
            kodeHari = "3";
        }else if(dayFormat.format(calendar.getTime()).equals("Thursday"))  {
            kodeHari = "4";
        }else if(dayFormat.format(calendar.getTime()).equals("Friday"))  {
            kodeHari = "5";
        }else if(dayFormat.format(calendar.getTime()).equals("Saturday"))  {
            kodeHari = "6";
        }else{
            System.out.println("Sorry your day is wrong");
        }

        getMatakuliah(nidn, kodeHari,String.valueOf(df.format(calendar.getTime())),
                String.valueOf(df.format(calendar.getTime())));

    }

    //Fungsi Kembali
    @Override
    public void onBackPressed() {
        Intent a = new Intent(PilihMatakuliah.this, AbsensiUBL.class);
        startActivity(a);
        finish();
    }


    // fungsi kembali
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Fungsi get JSON Mahasiswa
    private void getMatakuliah(final String nidn, final String kodeHari, final String jamAwal, final String jamAkhir) {

        String tag_string_req = "req";

        pDialog.setMessage("Loading.....");
        showDialog();

        String tag_json_obj = "json_obj_req";
        StringRequest strReq = new StringRequest(Request.Method.POST, Config_URL.base_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){

                        String getObject = jObj.getString("result");
                        JSONArray jsonArray = new JSONArray(getObject);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            DataMatakuliah news = new DataMatakuliah();

                            news.setMatakuliah(jsonObject.getString("matakuliah"));
                            news.setNidn(jsonObject.getString("nidn"));
                            news.setNomk(jsonObject.getString("kodeMK"));

                            newsList.add(news);
                        }
                    }else {
                        String error_msg = jObj.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(PilihMatakuliah.this);
                        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
                        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>"+error_msg+"</b></font>"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent a = new Intent(PilihMatakuliah.this, AbsensiUBL.class);
                                        startActivity(a);
                                        finish();
                                    }
                                }).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(String.valueOf(getApplication()), "Error : " + error.getMessage());
                error.printStackTrace();
                ImageView image = new ImageView(PilihMatakuliah.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(PilihMatakuliah.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(PilihMatakuliah.this, AbsensiUBL.class);
                                startActivity(a);
                                finish();
                            }
                        }).setView(image)
                        .show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag","checkMatakuliah");
                params.put("nidn", nidn);
                params.put("kodehari", kodeHari);
                params.put("jamawal", jamAwal);
                params.put("jamakhir", jamAkhir);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
