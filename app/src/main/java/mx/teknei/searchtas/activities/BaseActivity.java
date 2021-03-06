package mx.teknei.searchtas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mx.teknei.searchtas.BaseAction;
import mx.teknei.searchtas.utils.SharedPreferencesUtils;


public class BaseActivity extends AppCompatActivity implements BaseAction {

    @Override
    protected void onResume() {
        super.onResume();
        String token = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.TOKEN_APP, "");
        if (token.equals("")) {
            if (this instanceof LogInActivity) {
                /// ....
            } else {
                Intent end = new Intent(getApplicationContext(), LogInActivity.class);
                end.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(end);
                finish();
            }
        }
    }

    @Override
    public void cancelOperation() {
//        String operationID = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.OPERATION_ID, "");
        String token = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.TOKEN_APP, "");
//        if (!operationID.equals("")) {
//            //new AsynckTask para cancelar la operacion
////            new CancelOp(this, operationID, token, ApiConstants.ACTION_CANCEL_OPERATION).execute();
//            return;
//        }
//        if (operationID.equals("")) {
//            Intent end = new Intent(this, FormActivity.class);
//            end.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(end);
//            finish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void logOut() {
//        String operationID = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.OPERATION_ID, "");
        String token = SharedPreferencesUtils.readFromPreferencesString(this, SharedPreferencesUtils.TOKEN_APP, "");
//        if (!operationID.equals("")) {
            //new AsynckTask para cancelar la operacion
//            new CancelOp(this, operationID, token, ApiConstants.ACTION_BLOCK_CANCEL_OPERATION).execute();
//            return;
//        }
        if (!token.equals("")) {
            //new AsynckTask para logOut de la aplicación
//            new LogOut(this, token).execute();
            return;
        }
//        if (token.equals("") && operationID.equals("")) {
//            finish();
//        }
    }

    @Override
    public void goNext() {
    }

    @Override
    public void sendPetition() {
    }
}
