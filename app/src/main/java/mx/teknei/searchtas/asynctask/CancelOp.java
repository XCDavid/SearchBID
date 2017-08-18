package mx.teknei.searchtas.asynctask;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import mx.teknei.searchtas.R;
import mx.teknei.searchtas.activities.BaseActivity;
import mx.teknei.searchtas.dialogs.AlertDialog;
import mx.teknei.searchtas.dialogs.ProgressDialog;
import mx.teknei.searchtas.utils.ApiConstants;
import mx.teknei.searchtas.utils.SharedPreferencesUtils;
import mx.teknei.searchtas.ws.ServerConnection;

public class CancelOp extends AsyncTask<String, Void, Void> {
    //    private String newToken;
    private String token;
    private String operationID;
    private int ACTION;

    private Activity activityOrigin;
    private JSONObject responseJSONObject;
    private String errorMessage;
    private boolean responseOk = false;
    private ProgressDialog progressDialog;

    private boolean hasConecction = false;
    private Integer responseStatus = 0;

    private long endTime;

    public CancelOp(Activity context, String operationString, String tokenOld, int action) {
        this.activityOrigin = context;
        this.operationID = operationString;
        this.token = tokenOld;
        this.ACTION = action;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(
                activityOrigin,
                activityOrigin.getString(R.string.message_cancel_operation));
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        endTime = System.currentTimeMillis() + 2000;
        Log.i("Wait", "Timer Start: " + System.currentTimeMillis());
        Log.i("Wait", "Timer END: " + endTime);
        ConnectivityManager check = (ConnectivityManager) activityOrigin.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for (int i = 0; i < info.length; i++) {
            if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                hasConecction = true;
            }
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        if (hasConecction) {
            try {
                ServerConnection serverConnection = new ServerConnection();
                String endPoint = SharedPreferencesUtils.readFromPreferencesString(activityOrigin, SharedPreferencesUtils.URL_TEKNEI, activityOrigin.getString(R.string.default_url_teknei));
                Object arrayResponse[] = serverConnection.connection(activityOrigin, null, endPoint + ApiConstants.METHOD_CANCEL_OPERATION+operationID, token, ServerConnection.METHOD_DELETE,null,"");
                if (arrayResponse[1] != null) {
                    manageResponse(arrayResponse);
                } else {
                    errorMessage = activityOrigin.getString(R.string.message_ws_petition_fail);
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = activityOrigin.getString(R.string.message_ws_petition_fail);
            }
            Log.i("Wait", "timer after DO: " + System.currentTimeMillis());
            while (System.currentTimeMillis() < endTime) {
                //espera hasta que pasen los 2 segundos en caso de que halla terminado muy rapido el hilo
            }
            Log.i("Wait", "timer finish : " + System.currentTimeMillis());
        }
        return null;
    }


    private void manageResponse(Object arrayResponse[]) {
        responseJSONObject = (JSONObject) arrayResponse[0];
        responseStatus = (Integer) arrayResponse[1];
        if (responseStatus >= 200 && responseStatus < 300) {
            responseOk = true;
        } else if (responseStatus >= 300 && responseStatus < 400) {
            errorMessage = activityOrigin.getString(R.string.message_ws_response_300);
        } else if (responseStatus >= 400 && responseStatus < 500) {
            errorMessage = activityOrigin.getString(R.string.message_ws_response_400);
        } else if (responseStatus >= 500 && responseStatus < 600) {
            errorMessage = activityOrigin.getString(R.string.message_ws_response_500);
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        progressDialog.dismiss();
        //BORRAR -Revisar
        SharedPreferencesUtils.cleanSharedPreferencesOperation(activityOrigin);

        if (hasConecction) {
            if (responseOk) {
                SharedPreferencesUtils.cleanSharedPreferencesOperation(activityOrigin);

                if (ACTION == ApiConstants.ACTION_CANCEL_OPERATION) {
                    ((BaseActivity) activityOrigin).cancelOperation();
                }else if(ACTION == ApiConstants.ACTION_BLOCK_CANCEL_OPERATION){
                    ((BaseActivity) activityOrigin).logOut();
                }
            } else {
                Log.i("Message logout","logout: "+errorMessage);
                AlertDialog dialogoAlert;
                dialogoAlert = new AlertDialog(activityOrigin, activityOrigin.getString(R.string.message_ws_notice), errorMessage, ACTION);
                dialogoAlert.setCancelable(false);
                dialogoAlert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogoAlert.show();
            }
        } else {
            Log.i("Message logout","logout: "+errorMessage);
            AlertDialog dialogoAlert;
            dialogoAlert = new AlertDialog(activityOrigin, activityOrigin.getString(R.string.message_ws_notice), errorMessage, ACTION);
            dialogoAlert.setCancelable(false);
            dialogoAlert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogoAlert.show();
        }
    }

}