package com.project.absenubl;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.newversion.NewAbsen;
import com.newversion.PilihMatakuliah;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import server.AppController;
import server.Config_URL;

public class SmartClass extends AppCompatActivity {

    String strNidn;

    String kodeHari = null;
    String Ruang;

    Calendar calendar;
    SimpleDateFormat dayFormat;
    SimpleDateFormat df,df1,df2;

    //qr code scanner object
    private IntentIntegrator intentIntegrator;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    ProgressDialog pDialog;

    private MediaPlayer mp;

    private Button btnTutupPintu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Smart Class");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        mp = new MediaPlayer();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnTutupPintu = (Button) findViewById(R.id.btnTutupPintu);

        Intent intent = getIntent();
        strNidn = intent.getStringExtra("nidn");

        //txtJamAwal.setText(df.format(calendar.getTime()));
        calendar = Calendar.getInstance();
        dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        df = new SimpleDateFormat("HH:mm:ss");
        df1 = new SimpleDateFormat("yyyy-MM-dd");
        df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        intentIntegrator = new IntentIntegrator(SmartClass.this);
        intentIntegrator.initiateScan();

        btnTutupPintu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutupPintu();
            }
        });
    }

    public void smartClass(final String nidn, final String kdHari, final String jamAwal, final String jamAkhir, final String ruang){
        //Tag used to cancel the request
        String tag_string_req = "req";

        pDialog.setMessage("Loading.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(String.valueOf(getApplication()), "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){

                        String sukses = jObj.getString("success");
                        ImageView image = new ImageView(SmartClass.this);
                        image.setImageResource(R.drawable.ic_open_door_sukses);
                        AlertDialog.Builder builder = new AlertDialog.Builder(SmartClass.this);
                        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Berhasil Membuka Pintu</b></font>"));
                        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>"+sukses+"</b></font>"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).setView(image).show();

                        play();

                    }else {
                        String error_msg = jObj.getString("error_msg");
                        //Toast.makeText(getApplicationContext(),
                        //        error_msg + " Atau tutup aplikasi dan masuk kembali", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SmartClass.this);
                        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
                        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>"+error_msg+"</b></font>"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent a = new Intent(SmartClass.this, AbsensiUBL.class);
                                        startActivity(a);
                                        finish();
                                    }
                                }).show();
                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(String.valueOf(getApplication()), "Error : " + error.getMessage());
                error.printStackTrace();
                ImageView image = new ImageView(SmartClass.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(SmartClass.this);
                builder.setMessage(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(SmartClass.this, AbsensiUBL.class);
                                startActivity(a);
                                finish();
                            }
                        }).setView(image).show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag","getSmartClass");
                params.put("nidn", nidn);
                params.put("kdhari", kdHari);
                params.put("awal", jamAwal);
                params.put("akhir", jamAkhir);
                params.put("ruang", ruang);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }


    public void tutupPintu(){
        //Tag used to cancel the request
        String tag_string_req = "req";

        pDialog.setMessage("Loading.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(String.valueOf(getApplication()), "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        tutup();
                        String sukses = jObj.getString("success");
                        ImageView image = new ImageView(SmartClass.this);
                        image.setImageResource(R.drawable.ic_close_door);
                        AlertDialog.Builder builder = new AlertDialog.Builder(SmartClass.this);
                        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Berhasil Membuka Pintu</b></font>"));
                        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>"+sukses+"</b></font>"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent a = new Intent(SmartClass.this, AbsensiUBL.class);
                                        startActivity(a);
                                        finish();
                                    }
                                }).setView(image).show();
                    }else {
                        String error_msg = jObj.getString("error_msg");
                        //Toast.makeText(getApplicationContext(),
                        //        error_msg + " Atau tutup aplikasi dan masuk kembali", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SmartClass.this);
                        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
                        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>"+error_msg+"</b></font>"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent a = new Intent(SmartClass.this, AbsensiUBL.class);
                                        startActivity(a);
                                        finish();
                                    }
                                }).show();
                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(String.valueOf(getApplication()), "Error : " + error.getMessage());
                error.printStackTrace();
                ImageView image = new ImageView(SmartClass.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(SmartClass.this);
                builder.setMessage(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(SmartClass.this, AbsensiUBL.class);
                                startActivity(a);
                                finish();
                            }
                        }).setView(image).show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag","getTutupPintu");
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(SmartClass.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
                builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>Hasil tidak di temukan</b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(SmartClass.this, AbsensiUBL.class);
                                startActivity(a);
                                finish();
                            }
                        }).show();
            }else{
                //String textt = result.getContents().replace("http://ublapps.ubl.ac.id/profileubl/?npm=","");
                //String textSerach = textt.toString().toLowerCase(Locale.getDefault());

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

                String awal     = String.valueOf(df.format(calendar.getTime()));
                String akhir    = String.valueOf(df.format(calendar.getTime()));
                String ruang    = result.getContents();

                //ruang           = txtRuang.getText().toString();
                //strNidn         = txtNidn.getText().toString();

                if (!ruang.isEmpty()) {
                    smartClass(strNidn, kodeHari,awal,
                            akhir,ruang);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(SmartClass.this, AbsensiUBL.class);
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

    private void play() {

        mp = MediaPlayer.create(this, R.raw.berhasilbukapintu);

        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** Menjalankan Audio */
        mp.start();

        /** Penanganan Ketika Suara Berakhir */
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
    }

    private void tutup() {

        mp = MediaPlayer.create(this, R.raw.berhasilmenutup);

        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** Menjalankan Audio */
        mp.start();

        /** Penanganan Ketika Suara Berakhir */
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            }
        });
    }
}