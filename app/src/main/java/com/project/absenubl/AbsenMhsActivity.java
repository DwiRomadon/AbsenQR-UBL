package com.project.absenubl;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.vividcode.android.zxing.CaptureActivity;
import info.vividcode.android.zxing.CaptureActivityIntents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import server.AppController;
import server.Config_URL;
import server.KodeMK;

public class AbsenMhsActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener,  View.OnClickListener{

    Spinner spinKodeMK;
    private TextView idAbsen;
    private TextView pertemuanKe;
    private TextView kelas;
    private TextView npm;
    private Button btnScan;
    private Button btnGetCode;

    ProgressDialog pDialog;

    private static final String TAG = AbsenMhsActivity.class.getSimpleName();

    String idabsenn;
    String pertemuann;
    String npmm;
    String kodemkk;
    String kelass;


    //JSON Array
    private JSONArray result;

    private ArrayList<String> students;

    //qr code scanner object
    private IntentIntegrator intentIntegrator;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_absen_mhs);
        setTitle("Absen Mahasiswa");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        spinKodeMK = (Spinner)findViewById(R.id.spnKodeMK);
        idAbsen     = (TextView)findViewById(R.id.idAbsen);
        pertemuanKe = (TextView)findViewById(R.id.txtPertemuan);
        kelas       = (TextView)findViewById(R.id.txtKelas);
        npm         = (TextView)findViewById(R.id.txtNPM);

        idAbsen.setEnabled(false);
        kelas.setEnabled(false);
        npm.setEnabled(false);
        pertemuanKe.setEnabled(false);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        students = new ArrayList<String>();

        spinKodeMK.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        String nidn = intent.getStringExtra("nidn");

        TextView txtNidn = (TextView)findViewById(R.id.nidn);
        txtNidn.setText(nidn);

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);

        getData(nidn);

        btnGetCode = (Button) findViewById(R.id.btnGET);
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kodemkk = spinKodeMK.getSelectedItem().toString();
                if(!kodemkk.isEmpty()){
                    getKode(kodemkk);
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#20d2bb'><b>Peringatan !</b></font>"));
        builder.setMessage(Html.fromHtml("<font color='#20d2bb'><b>Apakah anda sudah yakin telah mengabsen seluruh mahasiswa yang hadir ?...</b></font>"))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent a = new Intent(AbsenMhsActivity.this, AbsensiUBL.class);
                        startActivity(a);
                        finish();
                    }
                })
                //jika tidak
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getData(final String nidnn){

        String tag_string_req = "req_";
        pDialog.setMessage("Loading.....");
        showDialog();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Config_URL.base_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("result");

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(String.valueOf(getApplicationContext()), "Login Error : " + error.getMessage());
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Please Check Your Network Connection", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "getKodeMK");
                params.put("nidn", nidnn);
                return params;
            }
        };

        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

    }

    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                students.add(json.getString("KodeMK"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinKodeMK.setAdapter(new ArrayAdapter<String>(AbsenMhsActivity.this, android.R.layout.simple_spinner_dropdown_item, students));
        ArrayAdapter<String> mAdapter;
        mAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, students);
        spinKodeMK.setAdapter(mAdapter);
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
    public void onClick(View view) {
        kodemkk = spinKodeMK.getSelectedItem().toString();
        if (!kodemkk.isEmpty()) {
            intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.initiateScan();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Maaf Anda Harus Memasukan Kode Matakuliah Terlebih Dahulu!!", Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{
                String textt = result.getContents().replace("http://elearning.ubl.ac.id/profileubl/?npm=","");
                npm.setText(textt);
                idabsenn = idAbsen.getText().toString();
                pertemuann = pertemuanKe.getText().toString();
                npmm = npm.getText().toString();
                kodemkk = spinKodeMK.getSelectedItem().toString();
                kelass = kelas.getText().toString();

                if (!kodemkk.isEmpty() && !pertemuann.isEmpty()) {
                    storeDataToServer(idabsenn,pertemuann,npmm, kodemkk, kelass);
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

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String value = data.getStringExtra("SCAN_RESULT");
                //result.setText(value);
                npm.setText(value);
                idabsenn = idAbsen.getText().toString();
                pertemuann = pertemuanKe.getText().toString();
                npmm = npm.getText().toString();
                kodemkk = spinKodeMK.getSelectedItem().toString();
                kelass = kelas.getText().toString();

                if (!kodemkk.isEmpty() && !pertemuann.isEmpty()) {
                    storeDataToServer(idabsenn,pertemuann,npmm, kodemkk, kelass);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "Scanning Gagal, mohon coba lagi.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Scanning Gagal, mohon coba lagi.", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/


    /*
    Function Get Code
     */
    public void getKode(final String kodemk){
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
                        String id       = jObj.getString("IdAbsenNgajar");
                        String kelasnya = jObj.getString("Kelas");
                        idAbsen.setText(id);
                        kelas.setText(kelasnya);
                        getPertemuanKe(kodemkk);
                    }else {
                        String error_msg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                error_msg, Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(String.valueOf(getApplication()), "Login Error : " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Please Check Your Network Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag","selectKodeMK");
                params.put("kode", kodemk);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }

    /*
    Function get pertemuan
     */
    public void getPertemuanKe(final String kodemk){
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
                        String pertemuan       = jObj.getString("pertemuan");
                        pertemuanKe.setText(pertemuan);
                    }else {
                        String error_msg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                error_msg, Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(String.valueOf(getApplication()), "Login Error : " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Please Check Your Network Connection", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag","getPertemuanke");
                params.put("kode", kodemk);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }

    /*
    Function for Store data to server
     */

    public void storeDataToServer(final String idAbsen, final String pertemuan, final String npm,
                                  final String kodemk, final String kls){

        // Tag used to cancel the request
        String tag_string_req = "req";

        pDialog.setMessage("Wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.base_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String errorrr = jObj.getString("success");
                        Toast.makeText(getApplicationContext(),
                                errorrr, Toast.LENGTH_LONG).show();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "inputabsenmhs");
                params.put("idabsenngajar", idAbsen);
                params.put("pertemuan", pertemuan);
                params.put("npm", npm);
                params.put("kodemk", kodemk);
                params.put("kelas", kls);
                return params;
            }

        };

        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}