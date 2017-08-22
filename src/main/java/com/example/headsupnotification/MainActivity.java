package com.example.headsupnotification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showHeadsUpWindow = (Button) findViewById(R.id.show_heads_up_window);
        showHeadsUpWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HeadsUpWindowService.class);
                startService(intent);
            }
        });

        Button hideHeadsUpWindow = (Button) findViewById(R.id.hide_heads_up_window);
        hideHeadsUpWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HeadsUpWindowService.class);
                stopService(intent);
            }
        });
    }
}
