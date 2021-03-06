package mx.teknei.searchtas.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Desarrollo on 29/06/2017.
 */

public class PermissionsUtils {
    public static final int CAMERA_REQUEST_PERMISSION = 41;
    public static final int PHONE_STATE_PERMISSION = 42;
    public static final int WRITE_READ_EXTERNAL_STORAGE_PERMISSION = 43;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 44;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 45;
    //Debe usarse en el OnCreate o dentro de la Activity que lo requiera, al momento de pintar la interfaz grafica
    //no antes de llegar a la actividad , por el metodo context.onBackPressed() de la seccion else
    //
    public static void checkPermissionCamera(Activity context){
        //If authorisation not granted for camera
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //ask for authorisation
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
        }
        else {
//            pass;
        }
    }
    public static void checkPermissionPhoneState(Activity context){
        //If authorisation not granted for camera
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //ask for authorisation
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_PHONE_STATE}, PHONE_STATE_PERMISSION);
        }
        else {
            //pass
        }
    }
    public static void checkPermissionReadWriteExternalStorage(Activity context){
        //If authorisation not granted for camera
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //ask for authorisation
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_READ_EXTERNAL_STORAGE_PERMISSION);
        }
        else {
            //pass
        }
    }
    public static void checkPermissionWriteExternalStorage(Activity context){
        //If authorisation not granted for camera
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            //ask for authorisation
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
        else {
            //pass
        }
    }
    public static void checkPermissionReadExternalStorage(Activity context){
        //If authorisation not granted for camera
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            //ask for authorisation
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);
        }
        else {
            //pass
        }
    }
}
