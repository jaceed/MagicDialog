package com.jacee.magicdialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.jaceed.android.magicdialog.OnDialogFragmentInteraction;
import com.jaceed.android.magicdialog.PromptDialog;
import com.jaceed.android.magicdialog.utils.Magics;

public class TestJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_java);

        getWindow().getDecorView().postDelayed(() -> Magics.show(this,
                new PromptDialog.Builder(this)
                        .title("abc")
                        .interaction(new OnDialogFragmentInteraction() {
                            @Override
                            public void onDialogNegativeClick(View v) {

                            }

                            @Override
                            public void onDialogPositiveClick(View v) {

                            }
                        })
                        .build()), 500);
    }
}