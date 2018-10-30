package com.example.user.work_4;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    Button btnAgain;
    TextView txtPecent;
    EditText edtNumber;
    ProgressBar pgbLoading;

    private int maxPgb = 100;


    Handler handlerParent = new Handler();

    Runnable foregroundTask = new Runnable() {
        @Override
        public void run() {
            pgbLoading.incrementProgressBy(1);
            double percent = pgbLoading.getProgress() / (double)maxPgb * 100.0;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            txtPecent.setText(decimalFormat.format(percent) + "%");

            if (pgbLoading.getProgress() == maxPgb)
                btnAgain.setEnabled(true);

        }
    };

    Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {
            for (int i =0 ;i < maxPgb; i++) {
                handlerParent.post(foregroundTask);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAgain = (Button)findViewById(R.id.btnDoItAgain);
        txtPecent = (TextView) findViewById(R.id.txtPercent);
        edtNumber = (EditText) findViewById(R.id.edtNum);
        pgbLoading = (ProgressBar) findViewById(R.id.pgbLoading);

        btnAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                maxPgb = Integer.parseInt(edtNumber.getText().toString());
                pgbLoading.setMax(maxPgb);
                txtPecent.setText("0%");
                Thread background = new Thread(backgroundTask, "abc");
                background.start();
                v.setEnabled(false);
            }
        });
    }
}
