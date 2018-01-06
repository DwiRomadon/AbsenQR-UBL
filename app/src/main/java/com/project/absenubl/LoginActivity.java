package com.project.absenubl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.newversion.NewAbsen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import server.AppController;
import server.Config_URL;
import session.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private Button loginBtn;
    private  EditText txtNidn;
    private  EditText txtPassword;

    private String level = "";
    private String id="";
    private String nama = "";
    private String nidn = "";

    private ProgressDialog pDialog;
    private SessionManager session;
    SharedPreferences prefs;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    LinearLayout linearLayout;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        txtNidn     = (EditText) findViewById(R.id.nidn);
        txtPassword = (EditText) findViewById(R.id.password);

        loginBtn    = (Button) findViewById(R.id.buttonLogin);

        linearLayout = (LinearLayout) findViewById(R.id.activity_login);

        prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        id             = prefs.getString("id","");
        level          = prefs.getString("level", "");
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            if(level.contains("2")){
                Intent intent = new Intent(LoginActivity.this, AbsensiUBL.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
            }
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txtNidn.getText().toString();
                String password = txtPassword.getText().toString();

                //Check for empty data in the form
                if(username.trim().length() > 0 && password.trim().length() > 0){
                    checkLogin(username, password);
                }else{
                    snackbar = Snackbar
                            .make(linearLayout, "Nidn dan Password tidak boleh kosong. " +
                                    "Silahkan Masukan Nidn atau Password Anda !!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private void checkLogin(final String username, final String password){

        //Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Login, Please Wait.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.base_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if(!error){
                        nama = jObj.getString("nama");
                        level = jObj.getString("level");
                        id = jObj.getString("id");
                        nidn = jObj.getString("nidn");
                        //user successsfully
                        //Create login Session
                        session.setLogin(true);

                        storeRegIdinSharedPref(getApplicationContext(),id, nama, level, nidn);
                        String theRole = level;
                        if(theRole.equals("2")){
                            //Lauch to main activity
                            Intent i = new Intent(getApplicationContext(),
                                    AbsensiUBL.class);
                            i.putExtra("id", id);
                            i.putExtra("nama", username);
                            i.putExtra("level",level);
                            i.putExtra("nidn", nidn);
                            startActivity(i);
                            finish();
                        }else{
                            snackbar = Snackbar
                                    .make(linearLayout, "Maaf Aplikasi Ini Hanya Untuk Dosen !!!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }else {
                        String error_msg = jObj.getString("error_msg");
                        snackbar = Snackbar
                                .make(linearLayout, error_msg, Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG, "Login Error : " + error.getMessage());
                error.printStackTrace();
                ImageView image = new ImageView(LoginActivity.this);
                image.setImageResource(R.drawable.ic_check_connection);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle(Html.fromHtml("<font color='#2980B9'><b></b></font>"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).setView(image)
                        .show();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config_URL.TAG, Config_URL.TAG_LOGIN);
                params.put(Config_URL.username, username);
                params.put(Config_URL.password, password);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void storeRegIdinSharedPref(Context context,String iduser,String usernme, String level, String nidn) {

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", iduser);
        editor.putString("nama", usernme);
        editor.putString("level", level);
        editor.putString("nidn", nidn);
        editor.commit();
    }
}