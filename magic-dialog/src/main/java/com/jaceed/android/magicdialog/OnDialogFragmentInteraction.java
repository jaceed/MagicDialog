package com.jaceed.android.magicdialog;

import android.view.View;

/**
 * Created by Jacee.
 * Date: 2022.04.07
 */
public interface OnDialogFragmentInteraction {

    void onDialogNegativeClick(View v);

    void onDialogPositiveClick(View v);

    default void onDialogNeutralClick(View v) {
    }

}
