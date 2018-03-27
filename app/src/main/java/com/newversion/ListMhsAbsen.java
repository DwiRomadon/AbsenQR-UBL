package com.newversion;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.absenubl.AbsensiUBL;
import com.project.absenubl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pojo.Adapter;
import pojo.DataMhsAbsen;
import server.AppController;
import server.Config_URL;

public class ListMhsAbsen extends AppCompatActivity {

    Adapter adapter;
    ListView list;
    //SwipeRefreshLayout swipe;
    ArrayList<DataMhsAbsen> newsList = new ArrayList<DataMhsAbsen>();

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    ProgressDialog pDialog;

    TextView nomk, tglabsen, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mhs_absen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("List Mhs Absen");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        nomk = (TextView) findViewById(R.id.kodeMK);
        tglabsen = (TextView) findViewById(R.id.tgl);
        nama = (TextView) findViewById(R.id.namaMK);

        Intent intent = getIntent();
        String kodeMatkul = intent.getStringExtra("kode");
        String tglAbsenn  = intent.getStringExtra("tglabsen");
        String mk  = intent.getStringExtra("mk");

        nama.setText("\u25CF Nama MK\t\t\t\t\t: " + mk);
        nomk.setText("\u25CF Kode Matakuliah\t: "+kodeMatkul);
        tglabsen.setText("\u25CF Tanggal Absen\t\t: " + tglAbsenn);

        list = (ListView) findViewById(R.id.list_news);
        newsList.clear();

        adapter = new Adapter(ListMhsAbsen.this, newsList);
        list.setAdapter(adapter);

        getNpmAndNamaMahasiswa(kodeMatkul, tglAbsenn);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(ListMhsAbsen.this, NewAbsen.class);
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
    private void getNpmAndNamaMahasiswa(final String kodeMk, final String tglAbsen) {

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

                            DataMhsAbsen news = new DataMhsAbsen();

                            news.setNpm(jsonObject.getString("Npm"));
                            news.setNama(jsonObject.getString("Nama"));

                            newsList.add(news);
                        }
                    }else {
                        String error_msg = jObj.getString("result");
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ListMhsAbsen.this);
                        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
                        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>"+error_msg+"</b></font>"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent a = new Intent(ListMhsAbsen.this, NewAbsen.class);
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
                ImageView image = new ImageView(ListMhsAbsen.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(ListMhsAbsen.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(ListMhsAbsen.this, AbsensiUBL.class);
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
                params.put("tag","getMhsAbsen");
                params.put("kode", kodeMk);
                params.put("tglabsen", tglAbsen);
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
