package mx.teknei.searchtas.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import mx.teknei.searchtas.R;
import mx.teknei.searchtas.asynctask.GetFace;
import mx.teknei.searchtas.asynctask.GetINE;
import mx.teknei.searchtas.utils.SharedPreferencesUtils;

public class ResultSearchActivity extends AppCompatActivity {
    ImageView faceView;
    ImageView ineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        faceView = (ImageView) findViewById(R.id.iv_front_face);
        ineView = (ImageView) findViewById(R.id.iv_id_front);

        String operationID = SharedPreferencesUtils.readFromPreferencesString(ResultSearchActivity.this, SharedPreferencesUtils.ID_SEARCH_OPERATION, "");
        String curp = SharedPreferencesUtils.readFromPreferencesString(ResultSearchActivity.this, SharedPreferencesUtils.CURP_SEARCH_OPERATION, "");
        String token = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.TOKEN_APP, "");
        new GetFace(ResultSearchActivity.this,curp,operationID,token).execute();
        new GetINE(ResultSearchActivity.this,curp,operationID,token).execute();
    }

    public void printFace(Bitmap bmp){
        faceView.setImageBitmap(bmp);
    }
    public void printINE(Bitmap bmp){
        ineView.setImageBitmap(bmp);
    }
}
