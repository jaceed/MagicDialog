package com.jacee.magicdialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.baicizhan.framework.common.magicdialog.OnDialogFragmentInteraction;
import com.baicizhan.framework.common.magicdialog.PromptDialog;
import com.baicizhan.framework.common.magicdialog.utils.Magics;

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