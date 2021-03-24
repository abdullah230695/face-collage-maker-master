package com.codetho.photocollage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.codetho.photocollage.R;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        TextView frameView= (TextView) findViewById(R.id.frameButton);
        frameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TemplateActivity.class);
                intent.putExtra(TemplateActivity.EXTRA_IS_FRAME_IMAGE, true);
                startActivity(intent);
            }
        });
    }

}
