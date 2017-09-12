package com.project.absenubl;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import server.AppController;
import server.Config_URL;
import server.KodeMK;

public class Absen extends AppCompatActivity implements View.OnClickListener{

    private EditText txtKodeHari;
    private EditText txtJamAwal;
    private EditText txtJamAkhir;
    private EditText txtRuang;
    private EditText txtKelas;
    private EditText txtNidn;
    private EditText txtKodeMatkul;
    private EditText txtSks;
    private EditText txtJumlahHadir;
    private EditText txtBlnTahunAbsen;
    private EditText txtKodeProdi;
    private EditText txtMingguKe;
    private EditText txtOperator;
    //private EditText txtHitung;
    private EditText txtThnSem;
    private EditText txtIdJadwal;
    private EditText txtTglAbsen;
    private EditText txtTglInput;
    private EditText txtBlnThnAbse;
    private EditText txtBeritaAcara;
    private Button btnKirim;

    //private Spinner spnBlnTahunAbsen;

    private TextView idAbsen;
    private TextView pertemuanKe;
    private TextView kelas;
    private TextView npm;
    private Button btnScan;

    String idabsenn;
    String pertemuann;
    String npmm;
    String kodeMK;
    String kelass;


    private Button btnSubmit;

    ProgressDialog pDialog;

    private static final String TAG = Absen.class.getSimpleName();


    //qr code scanner object
    private IntentIntegrator intentIntegrator;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    String kodeHari = null;
    String nidn;
    Intent intent;
    Calendar calendar;
    SimpleDateFormat df;
    SimpleDateFormat df1;
    SimpleDateFormat df2;
    SimpleDateFormat dayFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Absen UBL");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        txtKodeHari         = (EditText) findViewById(R.id.edKdHari);
        txtJamAwal          = (EditText) findViewById(R.id.edJamAwal);
        txtJamAkhir         = (EditText) findViewById(R.id.edJamAkhir);
        txtRuang            = (EditText) findViewById(R.id.edRuang);
        txtKelas            = (EditText) findViewById(R.id.edKelas);
        txtNidn             = (EditText) findViewById(R.id.edNidn);
        txtKodeMatkul       = (EditText) findViewById(R.id.edKdMK);
        txtSks              = (EditText) findViewById(R.id.edSks);
        txtJumlahHadir      = (EditText) findViewById(R.id.edJumlahHadir);
        //txtBlnTahunAbsen    = (EditText) findViewById(R.id.edblnTahunAbsen);
        txtKodeProdi        = (EditText) findViewById(R.id.edKdProdi);
        txtMingguKe         = (EditText) findViewById(R.id.edMingguKe);
        txtOperator         = (EditText) findViewById(R.id.edOperator);
        //txtHitung           = (EditText) findViewById(R.id.edHitung);
        txtThnSem           = (EditText) findViewById(R.id.edThnSem);
        txtIdJadwal         = (EditText) findViewById(R.id.edIdJadwal);
        txtTglAbsen         = (EditText) findViewById(R.id.edTglAbsen);
        txtTglInput         = (EditText) findViewById(R.id.edTglInput);
        txtBlnTahunAbsen    = (EditText) findViewById(R.id.blnTahunAbsen);

        txtBeritaAcara      = (EditText) findViewById(R.id.beritaAcara);
        btnKirim            = (Button) findViewById(R.id.btnSubmitBerita);
        btnKirim.setEnabled(false);

        //spnBlnTahunAbsen    = (Spinner) findViewById(R.id.spnBlnThnAbsen);

        idAbsen     = (TextView)findViewById(R.id.idAbsen);
        pertemuanKe = (TextView)findViewById(R.id.txtPertemuan);
        kelas       = (TextView)findViewById(R.id.txtKelas);
        npm         = (TextView)findViewById(R.id.txtNPM);

        idAbsen.setEnabled(false);
        kelas.setEnabled(false);
        npm.setEnabled(false);
        pertemuanKe.setEnabled(false);


        btnSubmit           = (Button) findViewById(R.id.btnSubmit);

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        btnScan.setEnabled(false);

        String[] blnThn = new String[]{
                "Sep2017",
                "Okt2017",
                "Nov2017",
                "Des2017",
                "Jan2018"
        };

        // Initializing an ArrayAdapter
        //ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
        //        this,R.layout.spinner_item,blnThn
        //);
        //spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        //spnBlnTahunAbsen.setAdapter(spinnerArrayAdapter);

        intent = getIntent();
        nidn = intent.getStringExtra("nidn");
        txtNidn.setText(nidn);
        txtOperator.setText(nidn);

        calendar = Calendar.getInstance();
        df = new SimpleDateFormat("HH:mm:ss");
        df1 = new SimpleDateFormat("yyyy-MM-dd");
        df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dfff = new SimpleDateFormat("MM");
        txtTglAbsen.setText(df1.format(calendar.getTime()));
        txtTglInput.setText(df2.format(calendar.getTime()));

        //txtJamAwal.setText(df.format(calendar.getTime()));
        dayFormat = new SimpleDateFormat("EEEE", Locale.US);

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


        if(dfff.format(calendar.getTime()).equals("09")){
            txtBlnTahunAbsen.setText("Sep2017");
        }else if(dfff.format(calendar.getTime()).equals("10")){
            txtBlnTahunAbsen.setText("Okt2017");
        }else if(dfff.format(calendar.getTime()).equals("11")){
            txtBlnTahunAbsen.setText("Nov2017");
        }else if(dfff.format(calendar.getTime()).equals("12")){
            txtBlnTahunAbsen.setText("Des2017");
        }else if(dfff.format(calendar.getTime()).equals("01")){
            txtBlnTahunAbsen.setText("Jan2018");
        }else if(dfff.format(calendar.getTime()).equals("02")){
            txtBlnTahunAbsen.setText("Feb2018");
        }else {
            System.out.println("Sorry is wrong");
        }

        getDataFromTblSplitDosen(nidn, kodeHari,String.valueOf(df.format(calendar.getTime())),
                String.valueOf(df.format(calendar.getTime())));
        btnHandler();

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /*
    Function Get Code Dosen
     */
    public void getDataFromTblSplitDosen(final String nidn, final String kdHari, final String jamAwal, final String jamAkhir){
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
                        String kodeHari         = jObj.getString("KodeHari");
                        String jamAwal          = jObj.getString("JamAwal");
                        String jamAkhir         = jObj.getString("JamAkhir");
                        String KodeMk           = jObj.getString("KodeMK");
                        String Ruang            = jObj.getString("Ruang");
                        String kelasnya         = jObj.getString("Kelas");
                        String thnSem           = jObj.getString("ThnSem");
                        String kodeProdi        = jObj.getString("KodeProdi");
                        String idJadwal         = jObj.getString("IdJadwal");

                        txtKodeHari.setText(kodeHari);
                        txtJamAwal.setText(jamAwal);
                        txtJamAkhir.setText(jamAkhir);
                        txtKodeMatkul.setText(KodeMk);
                        txtRuang.setText(Ruang);
                        txtKelas.setText(kelasnya);
                        txtThnSem.setText(thnSem);
                        txtKodeProdi.setText(kodeProdi);
                        txtIdJadwal.setText(idJadwal);
                        getSKS(KodeMk);
                        getMingguKe(KodeMk);
                        Toast.makeText(getApplicationContext(),
                                "Sukses mengambil data silahkan tekan tombol mulai", Toast.LENGTH_LONG).show();
                    }else {
                        String error_msg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                error_msg + " Atau tutup aplikasi dan masuk kembali", Toast.LENGTH_LONG).show();
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
                params.put("tag","getAbsenDosen");
                params.put("nidn", nidn);
                params.put("kdhari", kdHari);
                params.put("awal", jamAwal);
                params.put("akhir", jamAkhir);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }

    /*
    Function Get Code SKS DOSEN
     */
    public void getSKS(final String kdMk){
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
                        String sks            = jObj.getString("SKS");
                        double jmlhHadir      = Double.parseDouble(sks)/2;

                        txtSks.setText(sks);
                        txtJumlahHadir.setText(String.valueOf(jmlhHadir));
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
                params.put("tag","getSKS");
                params.put("kodemk", kdMk);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }

    /*
    Function get pertemuan
     */
    public void getMingguKe(final String kodemk){
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
                        String mingguKe       = jObj.getString("mingguke");
                        txtMingguKe.setText(mingguKe);
                        pertemuanKe.setText(mingguKe);
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
                params.put("tag","getMingguke");
                params.put("kode", kodemk);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }

    /*
    Function for Store data to server

    $kdhari,$tglAbsen,$jamAwal,$jamakhir,$ruang,$kelas,$nidn,$kdmk,$sks,
    $jmlhhadir,$blnthnabsen,$kdProdi,$mingguke,$operator,$thnSem,$idjadwal,$tglInput

     */

    public void updateBeritaAcara(final String beritaAcara, final String kodeMk, final String mingguKe){

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
                                errorrr + ", Berhasil mengirim berita acara", Toast.LENGTH_LONG).show();
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
                /*      $kdhari,$tglAbsen,$jamAwal,$jamakhir,$ruang,$kelas,$nidn,$kdmk,$sks,
                        $jmlhhadir,$blnthnabsen,$kdProdi,$mingguke,$operator,$thnSem,$idjadwal,$tglInput

                        */
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "UpdateBeritaAcara");
                params.put("beritaacara", beritaAcara);
                params.put("kodemk", kodeMk);
                params.put("mingguKe", mingguKe);
                return params;
            }

        };

        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    //button handler
    private void btnHandler(){

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String kodeHari     = txtKodeHari.getText().toString();
                String tglAbsen     = txtTglAbsen.getText().toString();
                String jamAwal      = txtJamAwal.getText().toString();
                String jamAkhir     = txtJamAkhir.getText().toString();
                String ruang        = txtRuang.getText().toString();
                String kelas        = txtKelas.getText().toString();
                String nidn         = txtNidn.getText().toString();
                kodeMK              = txtKodeMatkul.getText().toString();
                String sks          = txtSks.getText().toString();
                String jumlahHadir  = txtJumlahHadir.getText().toString();
                String blnTahunSem  = txtBlnTahunAbsen.getText().toString();
                String kdProdi      = txtKodeProdi.getText().toString();
                String mingguKe     = txtMingguKe.getText().toString();
                String operator     = txtOperator.getText().toString();
                String thnsem       = txtThnSem.getText().toString();
                String idJadwal     = txtIdJadwal.getText().toString();
                String tglInput     = txtTglInput.getText().toString();


                if(!kodeHari.isEmpty() && !tglAbsen.isEmpty() && !jamAwal.isEmpty() && !jamAkhir.isEmpty() && !ruang.isEmpty() &&
                        !kelas.isEmpty() && !nidn.isEmpty() && !kodeMK.isEmpty() && !sks.isEmpty() && !jumlahHadir.isEmpty() && !blnTahunSem.isEmpty()
                        && !kdProdi.isEmpty() && !mingguKe.isEmpty() && !operator.isEmpty() && !thnsem.isEmpty() && !idJadwal.isEmpty() && !tglInput.isEmpty()){
                    storeDataToServer(kodeHari,tglAbsen,jamAwal,jamAkhir,ruang,kelas,nidn,kodeMK,sks,
                            jumlahHadir,blnTahunSem,kdProdi,mingguKe,operator,thnsem,idJadwal,tglInput);
                    getKode(kodeMK);
                    btnScan.setEnabled(true);
                    btnKirim.setEnabled(true);
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Opps.... Maaf Server Tidak Meresponse. Silahkan Hubungi Administrator.", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kodeMK = txtKodeMatkul.getText().toString();
                btnSubmit.setEnabled(false);
                if (!kodeMK.isEmpty()) {
                    intentIntegrator = new IntentIntegrator(Absen.this);
                    intentIntegrator.initiateScan();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Opps.... Maaf Server Tidak Meresponse. Silahkan Hubungi Administrator.", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String beritaAcaranya = txtBeritaAcara.getText().toString();
                String kodeMK         = txtKodeMatkul.getText().toString();
                String mingguKe       = txtMingguKe.getText().toString();

                //if(!beritaAcaranya.isEmpty() && !kodeHari.isEmpty() && mingguKe.isEmpty()){
                    updateBeritaAcara(beritaAcaranya, kodeMK, mingguKe);
                //}else {
                    //Toast.makeText(getApplicationContext(),"Cek inputan atau Server Tidak Meresponse", Toast.LENGTH_LONG).show();
                //}
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
                        Intent a = new Intent(Absen.this, AbsensiUBL.class);
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
    public void onClick(View view) {

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
                kodeMK = txtKodeMatkul.getText().toString();
                kelass = kelas.getText().toString();
                String tglAbsen = txtTglAbsen.getText().toString();
                String tglInput = txtTglInput.getText().toString();
                if (!kodeMK.isEmpty() && !pertemuann.isEmpty()) {
                    storeDataToServer(idabsenn,tglAbsen,pertemuann,npmm, kodeMK, kelass,tglInput);
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
                        getPertemuanKe(kodeMK);
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
                        //pertemuanKe.setText(pertemuan);
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

    public void storeDataToServer(final String idAbsen,final String tglAbsen, final String pertemuan, final String npm,
                                  final String kodemk, final String kls, final String tglInput){

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
                params.put("tglabsen", tglAbsen);
                params.put("pertemuan", pertemuan);
                params.put("npm", npm);
                params.put("kodemk", kodemk);
                params.put("kelas", kls);
                params.put("tglInput", tglInput);
                return params;
            }

        };

        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void storeDataToServer(final String kdHari, final String tglAbsen, final String jamAwal,final String jamAkhir,final String ruang,
                                  final String kelas, final String nidn,final String kodeMK,final String sks,final String jmlhadir,
                                  final String blnthnSem,final String kdProdi,final String mingguKe,final String operator,final String thnSem,
                                  final String idJadwal,final String tglInput){

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
                /*      $kdhari,$tglAbsen,$jamAwal,$jamakhir,$ruang,$kelas,$nidn,$kdmk,$sks,
                        $jmlhhadir,$blnthnabsen,$kdProdi,$mingguke,$operator,$thnSem,$idjadwal,$tglInput

                        */
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "inputabsendosen");
                params.put("kdhari", kdHari);
                params.put("tglAbsen", tglAbsen);
                params.put("jamAwal", jamAwal);
                params.put("jamakhir", jamAkhir);
                params.put("ruang", ruang);
                params.put("kelas", kelas);
                params.put("nidn", nidn);
                params.put("kdmk", kodeMK);
                params.put("sks", sks);
                params.put("jmlhadir", jmlhadir);
                params.put("blnthnabsen", blnthnSem);
                params.put("kdProdi", kdProdi);
                params.put("mingguKe", mingguKe);
                params.put("operator", operator);
                params.put("thnSem", thnSem);
                params.put("idJadwal", idJadwal);
                params.put("tglInput", tglInput);
                return params;
            }

        };

        strReq.setRetryPolicy(policy);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}























