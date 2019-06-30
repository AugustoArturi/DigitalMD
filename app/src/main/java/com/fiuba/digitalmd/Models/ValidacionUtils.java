package com.fiuba.digitalmd.Models;

import android.widget.EditText;

public class ValidacionUtils {
    public static boolean validarNoVacio(EditText editText, String msgError) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(msgError);
            editText.requestFocus();
            return true;
        }
        return false;
    }
}
