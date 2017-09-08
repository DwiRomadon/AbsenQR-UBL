package com.project.absenubl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import session.SessionManager;

public class AbsensiUBL extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManager session;
    private ProgressDialog pDialog;

    Toolbar toolbar;
    SharedPreferences prefs;

    String nidn;
    String nama;
    String id;
    String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        setContentView(R.layout.activity_absensi_ubl);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        //Session Login
        if(session.isLoggedIn()){
            aksesUserLogin();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.absensi_ubl, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mahasiswa) {
            // Handle the camera action
            Intent a = new Intent(getApplicationContext(), Absen.class);
            a.putExtra("nidn",nidn);
            startActivity(a);
            finish();
        } /*else if (id == R.id.nav_dosen) {
            //showDialog();
        } */else if(id == R.id.nav_tentang){
            Intent a = new Intent(getApplicationContext(), TentangActivity.class);
            startActivity(a);
            finish();
        } else if (id == R.id.nav_logout) {
            logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        session.setLogin(false);
        session.setSkip(false);
        session.setSessid(0);

        // Launching the login activity
        Intent intent = new Intent(AbsensiUBL.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void aksesUserLogin(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_absensi_ubl, null);
        navigationView.addHeaderView(header);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        TextView nameText           = (TextView)header.findViewById(R.id.nameText);
        TextView nidnText           = (TextView)header.findViewById(R.id.nidn);
        TextView idText             = (TextView)header.findViewById(R.id.id);
        TextView levelText          = (TextView)header.findViewById(R.id.level);
        CircularImageView gambarnya = (CircularImageView)header.findViewById(R.id.imageProfile);
        TextView namaText           = (TextView)findViewById(R.id.namaText);

        gambarnya.setBorderColor(getResources().getColor(R.color.bg_screen3));
        gambarnya.setBorderWidth(5);
        gambarnya.addShadow();
        gambarnya.setShadowRadius(5);
        gambarnya.setShadowColor(Color.parseColor("#34495E"));

        prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        nidn = prefs.getString("nidn","");
        nama = prefs.getString("nama", "");
        id   = prefs.getString("id", "");
        level= prefs.getString("level", "");

        nameText.setText(nama);
        nidnText.setText(nidn);
        idText.setText(id);
        levelText.setText(level);
        namaText.setText(nama);
        idText.setVisibility(View.INVISIBLE);
        levelText.setVisibility(View.INVISIBLE);

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showDialog(){
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(AbsensiUBL.this);
        LayoutInflater factory = LayoutInflater.from(AbsensiUBL.this);
        final View view = factory.inflate(R.layout.imgaleret, null);
        alertDialog.setView(view);
        alertDialog.setTitle(Html.fromHtml("<font color='#20d2bb'><b>Ups Sorry...</b></font>"));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(Html.fromHtml("<font color='#20d2bb'><b>Maaf fitur ini sedang dalam pengembangan :)</b></font>"));
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
