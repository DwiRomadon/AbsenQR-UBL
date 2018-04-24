package com.newversion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.project.absenubl.Absen;
import com.project.absenubl.AbsensiUBL;
import com.project.absenubl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pojo.Adapter;
import pojo.DataMhsAbsen;
import server.AppController;
import server.Config_URL;

public class NewAbsen extends AppCompatActivity {

    private static final String TAG = Absen.class.getSimpleName();

    private String strNidn;

    private EditText edNidn;
    private EditText edKodeHari;
    private EditText edJamAwal;
    private EditText edJamAkhir;
    private EditText edRuang;
    private EditText edKelas;
    private EditText edKodeMatkul;
    private EditText edSks;
    private EditText edJumlahHadir;
    private EditText edTahunAbsen;
    private EditText edKodeProdi;
    private EditText edMingguKe;
    private EditText edOperator;
    private EditText edProgram;
    //private EditText txtHitung;
    private EditText edThnSem;
    private EditText edIdJadwal;
    private EditText edTglAbsen;
    private EditText edTglInput;
    private EditText edBlnThnAbsen;
    private EditText edMatakuliah;
    private EditText idAbsenNgajar;
    private EditText edPertemuan;
    private TextView edLisAbsenMhs;
    private EditText edNpm;
    private EditText edBeritaAcara;
    private TextView prodi;
    private TextView hari;
    private TextView mingguKenya;
    private TextView matkuliahnya;
    private TextView namaDosen;

    private Button btnMulaiAbsen;
    private Button btnScan;
    private Button lihatMhsAbsen;

    //private LinearLayout linearListView;

    private  Button btnKirim ;

    String tglAbsen     ;
    String jamAwal      ;
    String jamAkhir     ;
    String ruang        ;
    String kelas        ;
    String nidn         ;
    String kodeMK       ;
    String sks          ;
    String jumlahHadir  ;
    String blnTahunSem  ;
    String kdProdi      ;
    String mingguKe     ;
    String operator     ;
    String thnsem       ;
    String idJadwal     ;
    String tglInput     ;
    String Program      ;
    String namaMK;

    String kodeHari = null;

    Calendar calendar;
    SimpleDateFormat dayFormat;
    SimpleDateFormat df,df1,df2;

    ProgressDialog pDialog;

    //Adapter adapter;
    //ListView list;
    //SwipeRefreshLayout swipe;
    //ArrayList<DataMhsAbsen> newsList = new ArrayList<DataMhsAbsen>();

    String kodemk;
    //qr code scanner object
    private IntentIntegrator intentIntegrator;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_absen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Absen UBL");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //list = (ListView) findViewById(R.id.list_news);
        //newsList.clear();

        //adapter = new Adapter(NewAbsen.this, newsList);
        //list.setAdapter(adapter);

        //linearListView = (LinearLayout) findViewById(R.id.linearListView);

        edNidn          = (EditText) findViewById(R.id.edNidn);
        edKodeHari      = (EditText) findViewById(R.id.edKdHari);
        edJamAwal       = (EditText) findViewById(R.id.edJamAwal);
        edJamAkhir      = (EditText) findViewById(R.id.edJamAkhir);
        edRuang         = (EditText) findViewById(R.id.edRuang);
        edKelas         = (EditText) findViewById(R.id.txtKelas);
        edKodeMatkul    = (EditText) findViewById(R.id.edKdMK);
        edKodeProdi     = (EditText) findViewById(R.id.edKdProdi);
        edThnSem        = (EditText) findViewById(R.id.edThnSem);
        edIdJadwal      = (EditText) findViewById(R.id.edIdJadwal);
        edProgram       = (EditText) findViewById(R.id.edProgram);
        edSks           = (EditText) findViewById(R.id.edSks);
        edJumlahHadir   = (EditText) findViewById(R.id.edJumlahHadir);
        edBlnThnAbsen   = (EditText) findViewById(R.id.blnTahunAbsen);
        edMingguKe      = (EditText) findViewById(R.id.edMingguKe);
        edOperator      = (EditText) findViewById(R.id.edOperator);
        edTglAbsen      = (EditText) findViewById(R.id.edTglAbsen);
        edTglInput      = (EditText) findViewById(R.id.edTglInput);
        edMatakuliah    = (EditText) findViewById(R.id.edMatakuliah);
        idAbsenNgajar   = (EditText) findViewById(R.id.idAbsen);
        edPertemuan     = (EditText) findViewById(R.id.edPertemuan);
        edLisAbsenMhs   = (TextView) findViewById(R.id.txtMhsAbsen);
        edNpm           = (EditText) findViewById(R.id.txtNPM);
        edBeritaAcara   = (EditText) findViewById(R.id.beritaAcara);

        prodi           = (TextView) findViewById(R.id.prodi);
        hari            = (TextView) findViewById(R.id.harinya);
        mingguKenya     = (TextView) findViewById(R.id.mingguKe);
        matkuliahnya    = (TextView) findViewById(R.id.matakuliahnya);
        namaDosen       = (TextView) findViewById(R.id.nmdos);

        edBeritaAcara.setEnabled(false);
        //edBeritaAcara.setShowSoftInputOnFocus(false);
        edBeritaAcara.setFocusableInTouchMode(false);
        btnMulaiAbsen   = (Button) findViewById(R.id.btnSubmit);
        btnScan         = (Button) findViewById(R.id.btnScan);
        lihatMhsAbsen   = (Button) findViewById(R.id.lihatMhsAbsen);
        btnScan.setEnabled(false);

        btnKirim            = (Button) findViewById(R.id.btnSubmitBerita);
        btnKirim.setEnabled(false);

        Intent intent = getIntent();
        strNidn = intent.getStringExtra("nidn");
        kodemk = intent.getStringExtra("kodemk");
        edNidn.setText(strNidn);
        edOperator.setText(strNidn);

        //txtJamAwal.setText(df.format(calendar.getTime()));
        calendar = Calendar.getInstance();
        dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        df = new SimpleDateFormat("HH:mm:ss");
        df1 = new SimpleDateFormat("yyyy-MM-dd");
        df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        edTglAbsen.setText(String.valueOf(df1.format(calendar.getTime())));
        edTglInput.setText(String.valueOf(df2.format(calendar.getTime())));
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

        getDataFromTblSplitDosen(kodemk, kodeHari,String.valueOf(df.format(calendar.getTime())),
                String.valueOf(df.format(calendar.getTime())),strNidn);
        getBlnThnSemAndMingguKe(String.valueOf(df1.format(calendar.getTime())),
                String.valueOf(df1.format(calendar.getTime())));

        btnHandler();
    }


    //Fungsi Kembali
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>Apakah anda sudah yakin telah mengabsen seluruh mahasiswa yang hadir ?...</b></font>"))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent a = new Intent(NewAbsen.this, AbsensiUBL.class);
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

    /*
    Function Get Code Dosen
     */
    public void getDataFromTblSplitDosen(final String kdmk, final String kdHari, final String jamAwal, final String jamAkhir, final String nidn){
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
                        String program          = jObj.getString("KodeProgram");
                        namaMK           = jObj.getString("Nama_MK");
                        String sks              = jObj.getString("Sks");
                        String prodinya         = jObj.getString("Prodi");
                        String harinya          = jObj.getString("Hari");
                        String nama             = jObj.getString("Dosen");
                        String gelar            = jObj.getString("Gelar");
                        double cekSks      = Double.parseDouble(sks);

                        if(cekSks == 0){
                            edJumlahHadir.setText("1");
                        }else {
                            double jumlahHadir = cekSks/2;
                            edJumlahHadir.setText(String.valueOf(jumlahHadir));
                        }

                        edSks.setText(sks);
                        edMatakuliah.setText(namaMK);
                        matkuliahnya.setText("\u25CF Matakul: "+namaMK);
                        edKodeHari.setText(kodeHari);
                        edJamAwal.setText(jamAwal);
                        edJamAkhir.setText(jamAkhir);
                        edKodeMatkul.setText(KodeMk);
                        edRuang.setText(Ruang);
                        edKelas.setText(kelasnya);
                        edThnSem.setText(thnSem);
                        edKodeProdi.setText(kodeProdi);
                        edIdJadwal.setText(idJadwal);
                        edProgram.setText(program);
                        prodi.setText("\u25CF Prodi\t\t: "+prodinya);
                        hari.setText("\u25CF Hari\t\t\t: "+harinya);
                        namaDosen.setText("\u25CF Dosen\t: "+nama+" "+gelar);
                        //getSKS(KodeMk);
                        //getMingguKe(KodeMk,Ruang,kelasnya);
                        Toast.makeText(getApplicationContext(),
                                "Sukses mengambil data silahkan tekan tombol mulai", Toast.LENGTH_LONG).show();
                    }else {
                        String error_msg = jObj.getString("error_msg");
                        //Toast.makeText(getApplicationContext(),
                        //        error_msg + " Atau tutup aplikasi dan masuk kembali", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewAbsen.this);
                        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
                        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>"+error_msg+"</b></font>"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent a = new Intent(NewAbsen.this, AbsensiUBL.class);
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
                ImageView image = new ImageView(NewAbsen.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewAbsen.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(NewAbsen.this, AbsensiUBL.class);
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
                params.put("tag","getAbsenDosen");
                params.put("kodemk", kdmk);
                params.put("kdhari", kdHari);
                params.put("awal", jamAwal);
                params.put("akhir", jamAkhir);
                params.put("nidn", nidn);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }

    /*
    Function Get Code SKS DOSEN
     */
    public void getBlnThnSemAndMingguKe(final String tglAwal, final String tglAkhir){
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
                        String blnThnAbsen  = jObj.getString("BlnThnAbsen");
                        String mingguKe     = jObj.getString("MingguKe");

                        edBlnThnAbsen.setText(blnThnAbsen);
                        edMingguKe.setText(mingguKe);
                        mingguKenya.setText("\u25CF Minggu\t: Ke "+mingguKe);
                        edPertemuan.setText(mingguKe);

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
                Log.e(String.valueOf(getApplication()), "Error : " + error.getMessage());
                error.printStackTrace();
                ImageView image = new ImageView(NewAbsen.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewAbsen.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(NewAbsen.this, AbsensiUBL.class);
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
                params.put("tag","getBlnThnAbsenAndMinnguKe");
                params.put("tglAwal", tglAwal);
                params.put("tglAkhir", tglAkhir);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

    }

    // Fungsi get JSON Mahasiswa
    /*private void getNpmAndNamaMahasiswa(final String kodeMk) {

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
                    String getObject = jObj.getString("result");
                    JSONArray jsonArray = new JSONArray(getObject);

                    for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            DataMhsAbsen news = new DataMhsAbsen();

                            news.setNpm(jsonObject.getString("Npm"));
                            news.setNama(jsonObject.getString("Nama"));

                            newsList.add(news);
                    }

                    linearListView.setVisibility(View.VISIBLE);

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
                ImageView image = new ImageView(NewAbsen.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewAbsen.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(NewAbsen.this, AbsensiUBL.class);
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
                params.put("tag","getNamaMhsDalamSatuKelas");
                params.put("kode", kodeMk);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{
                String textt = result.getContents().replace("http://ublapps.ubl.ac.id/profileubl/?npm=","");
                String textSerach = textt.toString().toLowerCase(Locale.getDefault());
                edNpm.setText(textSerach);
                String idabsenn = idAbsenNgajar.getText().toString();
                String pertemuann = edMingguKe.getText().toString();
                String npmm = edNpm.getText().toString();
                kodeMK = edKodeMatkul.getText().toString();
                String kelass = edKelas.getText().toString();
                String tglAbsen = edTglAbsen.getText().toString();
                String tglInput = edTglInput.getText().toString();

                if (!kodeMK.isEmpty() && !pertemuann.isEmpty()) {
                    inputAbsenMhs(idabsenn,tglAbsen,pertemuann,npmm, kodeMK, kelass,tglInput);
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
    Function for Store data to server
     */
    public void inputAbsenMhs(final String idAbsen,final String tglAbsen, final String pertemuan, final String npm,
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

                    if(error){
                        String errorMsg = jObj.getString("pesan_error");
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewAbsen.this);
                        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
                        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>"+errorMsg+"</b></font>"))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }else {
                        /*if(error){
                            String errorMsg = jObj.getString("pesan_error");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();*/
                        //}else {
                            if (!error) {
                                //viewMhsAbsen(npmm);
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
                        //}
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                ImageView image = new ImageView(NewAbsen.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewAbsen.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(NewAbsen.this, AbsensiUBL.class);
                                startActivity(a);
                                finish();
                            }
                        }).setView(image)
                        .show();
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
                ImageView image = new ImageView(NewAbsen.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewAbsen.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(NewAbsen.this, AbsensiUBL.class);
                                startActivity(a);
                                finish();
                            }
                        }).setView(image)
                        .show();
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


    private void btnHandler(){

        btnMulaiAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 kodeHari     = edKodeHari.getText().toString();
                 tglAbsen     = edTglAbsen.getText().toString();
                 jamAwal      = edJamAwal.getText().toString();
                 jamAkhir     = edJamAkhir.getText().toString();
                 ruang        = edRuang.getText().toString();
                 kelas        = edKelas.getText().toString();
                 nidn         = edNidn.getText().toString();
                 kodeMK       = edKodeMatkul.getText().toString();
                 sks          = edSks.getText().toString();
                 jumlahHadir  = edJumlahHadir.getText().toString();
                 blnTahunSem  = edBlnThnAbsen.getText().toString();
                 kdProdi      = edKodeProdi.getText().toString();
                 mingguKe     = edMingguKe.getText().toString();
                 operator     = edOperator.getText().toString();
                 thnsem       = edThnSem.getText().toString();
                 idJadwal     = edIdJadwal.getText().toString();
                 tglInput     = edTglInput.getText().toString();
                 Program      = edProgram.getText().toString();


                if(!kodeHari.isEmpty() && !tglAbsen.isEmpty() && !jamAwal.isEmpty() && !jamAkhir.isEmpty() && !ruang.isEmpty() &&
                        !kelas.isEmpty() && !nidn.isEmpty() && !kodeMK.isEmpty() && !sks.isEmpty() && !jumlahHadir.isEmpty() && !blnTahunSem.isEmpty()
                        && !kdProdi.isEmpty() && !mingguKe.isEmpty() && !operator.isEmpty() && !thnsem.isEmpty() && !idJadwal.isEmpty() && !tglInput.isEmpty()){
                    storeDataToServer(kodeHari,tglAbsen,jamAwal,jamAkhir,ruang,kelas,nidn,kodeMK,sks,
                            jumlahHadir,blnTahunSem,kdProdi,mingguKe,Program,operator,thnsem,idJadwal,tglInput);
                    ///getKode(kodeMK);
                    btnScan.setEnabled(true);
                    btnKirim.setEnabled(true);
                    getIdAbsenNgajar(kodeMK,tglAbsen);
                    //getNpmAndNamaMahasiswa(kodeMK);
                    btnMulaiAbsen.setEnabled(false);
                    //edBeritaAcara.setShowSoftInputOnFocus(true);
                    edBeritaAcara.setEnabled(true);
                    edBeritaAcara.setFocusableInTouchMode(true);
                    btnMulaiAbsen.setVisibility(View.GONE);

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
                kodeMK = edKodeMatkul.getText().toString();
                //btnSubmit.setEnabled(false);
                if (!kodeMK.isEmpty()) {
                    intentIntegrator = new IntentIntegrator(NewAbsen.this);
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
                String beritaAcaranya = edBeritaAcara.getText().toString();
                String kodeMK         = edKodeMatkul.getText().toString();
                String mingguKe       = edMingguKe.getText().toString();

                //if(!beritaAcaranya.isEmpty() && !kodeHari.isEmpty() && mingguKe.isEmpty()){
                updateBeritaAcara(beritaAcaranya, kodeMK, mingguKe);
                //}else {
                //Toast.makeText(getApplicationContext(),"Cek inputan atau Server Tidak Meresponse", Toast.LENGTH_LONG).show();
                //}
            }
        });


        lihatMhsAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(NewAbsen.this, ListMhsAbsen.class);
                a.putExtra("kode",kodemk);
                a.putExtra("tglabsen",String.valueOf(df1.format(calendar.getTime())) );
                a.putExtra("mk",namaMK);
                startActivity(a);
                finish();
            }
        });

    }

    public void storeDataToServer(final String kdHari, final String tglAbsen, final String jamAwal,final String jamAkhir,final String ruang,
                                  final String kelas, final String nidn,final String kodeMK,final String sks,final String jmlhadir,
                                  final String blnthnSem,final String kdProdi,final String mingguKe,final String program,final String operator,final String thnSem,
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
                        //btnSubmit.setEnabled(false);
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
                ImageView image = new ImageView(NewAbsen.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewAbsen.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(NewAbsen.this, AbsensiUBL.class);
                                startActivity(a);
                                finish();
                            }
                        }).setView(image)
                        .show();
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
                params.put("program", program);
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

    /*
    Function Get Code SKS DOSEN
     */
    public void getIdAbsenNgajar(final String KodeMk, final String tglAbsen){
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
                        String idAbsen       = jObj.getString("IdAbsenNgajar");

                        idAbsenNgajar.setText(idAbsen);
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
                Log.e(String.valueOf(getApplication()), "Error : " + error.getMessage());
                error.printStackTrace();
                ImageView image = new ImageView(NewAbsen.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewAbsen.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent a = new Intent(NewAbsen.this, AbsensiUBL.class);
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
                params.put("tag","getIdAbsenNgajar");
                params.put("kodemk", KodeMk);
                params.put("tglAbsen", tglAbsen);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq,tag_string_req);

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
