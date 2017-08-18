package mx.teknei.searchtas.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mx.teknei.searchtas.R;
import mx.teknei.searchtas.asynctask.FindByCurp;
import mx.teknei.searchtas.dialogs.AlertDialog;
import mx.teknei.searchtas.utils.ApiConstants;
import mx.teknei.searchtas.utils.SharedPreferencesUtils;

public class FormActivity extends BaseActivity implements View.OnClickListener {
    EditText etName;
    EditText etCurp;
    Button buttonContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.form_activity_name));
            invalidateOptionsMenu();
        }
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals("")) { // for backspace
                    return source;
                }
                if (source.toString().matches("[a-zA-ZÑñáéíóúÁÉÍÓÚ ]+")) {
                    return source;
                }
                return "";
            }
        };
        InputFilter filterCURP = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals("")) { // for backspace
                    return source;
                }
                if (source.toString().matches("[A-Z0-9ÑÁÉÍÓÚ]+")) {
                    return source;
                }
                return "";
            }
        };
        etName = (EditText) findViewById(R.id.et_name_form);
        etCurp = (EditText) findViewById(R.id.et_curp_form);
        buttonContinue = (Button) findViewById(R.id.b_continue_form);

        buttonContinue.setOnClickListener(this);
        etName.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(30)});
        etCurp.setFilters(new InputFilter[]{filterCURP, new InputFilter.LengthFilter(18)});
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_continue_form:
                if (validateDataForm()) {
                    sendPetition();
                }
                break;
        }
    }

    private boolean validateDataForm() {
        /*if (etName.getText().toString().equals("")) {
            Toast.makeText(this, "El campo ( Nombre ) es obligatorio", Toast.LENGTH_SHORT).show();
            etName.clearFocus();
            if (etName.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(etName, InputMethodManager.SHOW_IMPLICIT);
            }
        } else*/
        if (etCurp.getText().toString().equals("")) {
            Toast.makeText(this, "El campo ( CURP ) es obligatorio", Toast.LENGTH_SHORT).show();
            etCurp.clearFocus();
            if (etCurp.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(etCurp, InputMethodManager.SHOW_IMPLICIT);
            }
        }else /* if (etCurp.getText().toString().length() < 18) {
            Toast.makeText(this, "El campo ( CURP ) debe tener 18 digitos", Toast.LENGTH_SHORT).show();
            etCurp.clearFocus();
            if (etCurp.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(etCurp, InputMethodManager.SHOW_IMPLICIT);
            }
        } else*/{
//            Toast.makeText(this, "Super OK !!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void sendPetition() {
        String token = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.TOKEN_APP, "");
        new FindByCurp(FormActivity.this, etCurp.getText().toString(), token).execute();
    }

    @Override
    public void goNext() {
        Intent i = new Intent(FormActivity.this, FingerScanActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialogoAlert;
        dialogoAlert = new AlertDialog(FormActivity.this, getString(R.string.message_title_logout), getString(R.string.message_message_logout), ApiConstants.ACTION_LOG_OUT);
        dialogoAlert.setCancelable(false);
        dialogoAlert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogoAlert.show();
    }
}
