package mx.teknei.searchtas.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.morpho.android.usb.USBManager;
import com.morpho.morphosmart.sdk.ErrorCodes;
import com.morpho.morphosmart.sdk.MorphoDevice;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import mx.teknei.searchtas.R;
import mx.teknei.searchtas.asynctask.SearchFinger;
import mx.teknei.searchtas.dialogs.FingerScanDialog;
import mx.teknei.searchtas.mso.MSOConnection;
import mx.teknei.searchtas.mso.MSOShower;
import mx.teknei.searchtas.tools.TKN_MSO_ERROR;
import mx.teknei.searchtas.utils.SharedPreferencesUtils;

public class FingerScanActivity extends BaseActivity implements View.OnClickListener, MSOShower {
    Button searchButton;
    ImageButton bFingerprint;
    private ImageButton imgFP;

    FingerScanDialog dialogScan;
    MorphoDevice morphoDevice;

    private byte[] imgFPBuff = null;
    String base64Finger;
    File imageFileFinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_scan);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.finger_scan_activity_name));
            invalidateOptionsMenu();
        }
        searchButton = (Button) findViewById(R.id.b_continue_finger_scan);
        bFingerprint = (ImageButton) findViewById(R.id.b_finger_search_finger_scan);

        searchButton.setOnClickListener(this);
        bFingerprint.setOnClickListener(this);

        morphoDevice = new MorphoDevice();
        // ---------- Aqui se inicia la conexion con el lector(ya no se hace en el metodo mso1300
        USBManager.getInstance().initialize(this, "com.morpho.morphosample.USB_ACTION");
        try {
            MSOConnection.getInstance().tkn_mso_connect();
            Log.i(this.getClass().getName(), "Conexion Realizada");
        } catch (TKN_MSO_ERROR e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getErrorMsg(), Toast.LENGTH_LONG).show();
        }
        MSOConnection.getInstance().setMsoShower(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean permission = USBManager.getInstance().isDevicesHasPermission();
//        if (!permission) {
//            try {
//                onBackPressed();
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_continue_finger_scan:
//                Intent i = new Intent(this, PayConfirmationActivity.class);

//                Intent i = new Intent(this, PayConfirmationActivity.class);
//                startActivity(i);
                sendPetition();
                break;
            case R.id.b_finger_search_finger_scan:
//                imgFP = (ImageButton)view;
                dialogScan = new FingerScanDialog(this, getString(R.string.tv_title_finger_dialog),getString(R.string.tv_message_finger_dialog));
                dialogScan.setCancelable(false);
                dialogScan.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogScan.show();
                mso1300(view);
                break;
        }
    }

    public void mso1300(View v) {
        imgFP = ((ImageButton) v);
        try {
            MSOConnection.getInstance().tkn_mso_capture(FingerScanActivity.this);
        } catch (TKN_MSO_ERROR e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getErrorMsg(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);
        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }
        return hasImage;
    }

    public void setImageToRightFinger() {
        if (imgFP != null) {
            Bitmap msoBitMap = MSOConnection.getInstance().getBitMap();
            Log.d("base64","lenght:"+imgFPBuff.length);
            imgFP.setImageBitmap(msoBitMap);
//            String operationID = SharedPreferencesUtils.readFromPreferencesString(FingerPrintsActivity.this, SharedPreferencesUtils.OPERATION_ID, "");
            String dir = Environment.getExternalStorageDirectory() + File.separator;
            switch (imgFP.getId()) {
                case R.id.b_finger_search_finger_scan:
                    base64Finger = mx.teknei.searchtas.tools.Base64.encode(imgFPBuff);
                    break;
            }
            //Guarda nueva imagen del dedo
//            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "finger_search" + ".jpg");
//            if (f.exists()) {
//                f.delete();
//                f = new File(Environment.getExternalStorageDirectory() + File.separator + "finger_search" + ".jpg");
//            }
//            try {
//                f.createNewFile();
//                //write the bytes in file
//                FileOutputStream fo = new FileOutputStream(f);
//                fo.write(imgFPBuff);
//                // remember close de FileOutput
//                fo.close();
//                imageFileFinger = f;
//            } catch (IOException e) {
//                e.printStackTrace();
//                f = null;
//            }
        }
    }

    @Override
    public void sendPetition() {
        String token = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.TOKEN_APP, "");
//        String fingerOperation = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.FINGERS_OPERATION, "");
        boolean bitMapTake = false;

        if (bFingerprint.getDrawable() instanceof BitmapDrawable) {
            bitMapTake = true;
        }/* else if (bIndexLeft.getDrawable() instanceof VectorDrawableCompat || bIndexRight.getDrawable() instanceof VectorDrawableCompat){
            bitMapTake = false;
        }*/
        //DES_COMENTAR
//        if(bitMapTake){
            //BORRAR
            if (true) {
//            String localTime = PhoneSimUtils.getLocalDateAndTime();
//            SharedPreferencesUtils.saveToPreferencesString(FingerPrintsActivity.this, SharedPreferencesUtils.TIMESTAMP_FINGERPRINTS, localTime);
//
            String jsonString = buildJSON();
////                Log.d("FingerJSON", "JSON FINGERs:" + jsonString);
//            fileList.add(fileJson);
//            if (imageFileIndexLeft != null){
//                fileList.add(imageFileIndexLeft);
//            }
//            if (imageFileIndexRight != null){
//                fileList.add(imageFileIndexRight);
//            }
//            Log.d("ArrayList Files", "Files:" + fileList.size());
            new SearchFinger(FingerScanActivity.this, token, jsonString).execute();
//            goNext();
        } else {
            Toast.makeText(FingerScanActivity.this, "Escanea un dedo para continuar", Toast.LENGTH_SHORT).show();
//                goNext();
        }
    }
    public String buildJSON() {
        String operationID = SharedPreferencesUtils.readFromPreferencesString(FingerScanActivity.this, SharedPreferencesUtils.ID_SEARCH_OPERATION, "");
        //Construimos el JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",operationID);
            if (base64Finger != null && !base64Finger.equals("")) {
                try {
                    jsonObject.put("li", base64Finger);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.w("JSON SEND","JSON->"+jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void goNext() {
        Intent i = new Intent(FingerScanActivity.this, ResultSearchActivity.class);
        startActivity(i);
    }

    @Override
    public void updateSensorProgressBar(int level) {
        try {
            ProgressBar progressBar = (ProgressBar) dialogScan.findViewById(R.id.vertical_progressbar);

            final float[] roundedCorners = new float[]{1, 1, 1, 1, 1, 1, 1, 1};
            ShapeDrawable pgDrawable = new ShapeDrawable(new RoundRectShape(roundedCorners, null, null));

            int color = Color.GREEN;

            if (level <= 25) {
                color = Color.RED;
            } else if (level <= 50) {
                color = Color.YELLOW;
            }
            pgDrawable.getPaint().setColor(color);
            ClipDrawable progress = new ClipDrawable(pgDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
            progressBar.setProgressDrawable(progress);
            //progressBar.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.progress_horizontal));
            progressBar.setProgress(level);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void updateSensorMessage(String sensorMessage) {
        TextView txtMensaje2 = (TextView) dialogScan.findViewById(R.id.tv_message_move_finger);
        Log.i("updateMessage", "message update");
        try {
            txtMensaje2.setText(sensorMessage);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void updateImage(Bitmap bitmap) {
        ImageView imgFP2 = (ImageView) dialogScan.findViewById(R.id.fingerprint);
        Log.i("updateImage", "image update");
        try {
            imgFP2.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void showAlert(String message) {

    }

    @Override
    public void updateImageView(final byte[] imgeSrc, final int captureError) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (captureError == ErrorCodes.MORPHO_OK) {
                    imgFPBuff = imgeSrc;
                    Log.w("update OK Image", "image  OK update");
                    setImageToRightFinger();
                    if (dialogScan != null && dialogScan.isShowing()) {
                        dialogScan.dismiss();
                    }
                }
            }
        });
    }
}
