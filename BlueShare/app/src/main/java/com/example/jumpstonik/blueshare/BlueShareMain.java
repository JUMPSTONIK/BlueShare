package com.example.jumpstonik.blueshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BlueShareMain extends AppCompatActivity {
    Button btnGoToShare;
    Button btnGoToConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_share_main);
        btnGoToConnection = (Button) findViewById(R.id.btnGoToConnection);
        btnGoToShare = (Button) findViewById(R.id.btnGoToShare);
        btnGoToConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blueshareconnection = new Intent(BlueShareMain.this, BlueShareConnection.class);
                startActivity(blueshareconnection);
            }
        });
        btnGoToShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bluesharecollectandsend = new Intent(BlueShareMain.this, BlueShareCollectAndSend.class);
                startActivity(bluesharecollectandsend);
            }
        });
    }

}
