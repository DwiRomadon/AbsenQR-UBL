package com.newversion;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.absenubl.Absen;
import com.project.absenubl.AbsensiUBL;
import com.project.absenubl.R;

public class AksenAja extends AppCompatActivity implements View.OnClickListener{

    EditText npm;
    private Button btnScan;

    //qr code scanner object
    private IntentIntegrator intentIntegrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aksen_aja);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Absen UBL");
        overridePendingTransition(R.anim.slidein, R.anim.slideout);

        npm         = (EditText) findViewById(R.id.txtNpm);
        npm.setEnabled(false);

        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        //btnScan.setEnabled(false);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kodeMK = txtKodeMatkul.getText().toString();
                //btnSubmit.setEnabled(false);
                //if (!kodeMK.isEmpty()) {
                    intentIntegrator = new IntentIntegrator(AksenAja.this);
                    intentIntegrator.initiateScan();
                //} else {
                //    Toast.makeText(getApplicationContext(),
                //            "Opps.... Maaf Server Tidak Meresponse. Silahkan Hubungi Administrator.", Toast.LENGTH_LONG)
                //            .show();
                //}
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this, "Hasil tidak ditemukan", Toast.LENGTH_SHORT).show();
            }else{
                String textt = result.getContents().replace("http://ublapps.ubl.ac.id/profileubl/?npm=","");
                npm.setText(textt);

                Toast.makeText(getApplicationContext(),"Sukses Input Absen", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int color = R.color.colorPrimary;
        builder.setTitle(Html.fromHtml("<font color='#2980B9'><b>Peringatan !</b></font>"));
        builder.setMessage(Html.fromHtml("<font color='#2980B9'><b>Apakah anda sudah yakin telah mengabsen seluruh mahasiswa yang hadir ?...</b></font>"))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent a = new Intent(AksenAja.this, AbsensiUBL.class);
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

}
