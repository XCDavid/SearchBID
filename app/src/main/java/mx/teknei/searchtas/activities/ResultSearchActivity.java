package mx.teknei.searchtas.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import mx.teknei.searchtas.R;
import mx.teknei.searchtas.asynctask.GetFace;
import mx.teknei.searchtas.asynctask.GetINE;
import mx.teknei.searchtas.utils.ApiConstants;
import mx.teknei.searchtas.utils.SharedPreferencesUtils;

public class ResultSearchActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView faceView;
    ImageView ineView;
    Button newSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.result_activity_name));
            invalidateOptionsMenu();
        }
        faceView = (ImageView) findViewById(R.id.iv_front_face);
        ineView = (ImageView) findViewById(R.id.iv_id_front);
        newSearch = (Button) findViewById(R.id.b_new_search_result_documents);
        newSearch.setOnClickListener(this);

        String operationID = SharedPreferencesUtils.readFromPreferencesString(ResultSearchActivity.this, SharedPreferencesUtils.ID_SEARCH_OPERATION, "");
        String curp = SharedPreferencesUtils.readFromPreferencesString(ResultSearchActivity.this, SharedPreferencesUtils.CURP_SEARCH_OPERATION, "");
        String token = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.TOKEN_APP, "");
        new GetFace(ResultSearchActivity.this, curp, operationID, token).execute();
        new GetINE(ResultSearchActivity.this, curp, operationID, token).execute();
    }

    public void printFace(Bitmap bmp) {
        faceView.setImageBitmap(bmp);
    }

    public void printINE(Bitmap bmp) {
        // find the width and height of the screen:
        Display d = getWindowManager().getDefaultDisplay();

        int x = bmp.getWidth();
        int y = bmp.getHeight();
        Bitmap rotatedBitmap = bmp;
        if (x < y) {
// create a matrix object
            Matrix matrix = new Matrix();
            matrix.postRotate(90); // anti-clockwise by 90 degrees
// create a new bitmap from the original using the matrix to transform the result
            rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }
// scale it to fit the screen, x and y swapped because my image is wider than it is tall
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, y, x, true);

        ineView.setImageBitmap(rotatedBitmap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_new_search_result_documents:
                SharedPreferencesUtils.cleanSharedPreferencesOperation(ResultSearchActivity.this);

                Intent end = new Intent(ResultSearchActivity.this, FormActivity.class);
                end.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(end);
                finish();
                break;
        }
    }
}
