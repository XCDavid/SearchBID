package mx.teknei.searchtas.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mx.teknei.searchtas.R;
import mx.teknei.searchtas.utils.SharedPreferencesUtils;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etServerTeknei;
    Button updateButton;
    String urlTeknei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.settings_activity_name));
            invalidateOptionsMenu();
        }

        urlTeknei = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.URL_TEKNEI, getString(R.string.default_url_teknei));

        etServerTeknei = (EditText) findViewById(R.id.et_settings_url_teknei);
        updateButton = (Button) findViewById(R.id.b_update_project_settings);

        updateButton.setOnClickListener(this);
        etServerTeknei.setText(urlTeknei);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.b_update_project_settings:
                urlTeknei = etServerTeknei.getText().toString();
                SharedPreferencesUtils.saveToPreferencesString(SettingsActivity.this,SharedPreferencesUtils.URL_TEKNEI,urlTeknei);
                Toast.makeText(SettingsActivity.this, "Ajustes actualizados !", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
