package mx.teknei.searchtas.asynctask;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

import mx.teknei.searchtas.R;
import mx.teknei.searchtas.activities.BaseActivity;
import mx.teknei.searchtas.dialogs.AlertDialog;
import mx.teknei.searchtas.dialogs.ProgressDialog;
import mx.teknei.searchtas.utils.ApiConstants;
import mx.teknei.searchtas.utils.SharedPreferencesUtils;
import mx.teknei.searchtas.ws.ServerConnection;

public class SearchFinger extends AsyncTask<String, Void, Void> {
    private String token;
    private String jsonS;

    private Activity activityOrigin;
    private JSONObject responseJSONObject;
    private String errorMessage;
    private boolean responseOk = false;
    private ProgressDialog progressDialog;

    private boolean hasConecction = false;
    private Integer responseStatus = 0;

    private long endTime;

    public SearchFinger(Activity context, String tokenOld, String jsonString/*, int action*/) {
        this.activityOrigin = context;
        this.token = tokenOld;
        this.jsonS = jsonString;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(
                activityOrigin,
                activityOrigin.getString(R.string.message_search_finger_operation));
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.show();
        endTime = System.currentTimeMillis() + 1000;
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
                Object arrayResponse[] = serverConnection.connection(activityOrigin, jsonS, endPoint + ApiConstants.METHOD_SEARCH_FINGER, token, ServerConnection.METHOD_POST,null,"");
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
        String dataExist = "";
        if (responseStatus >= 200 && responseStatus < 300) {
//            try {
//                dataExist = responseJSONObject.getString("hasFingers "); //obtiene los datos del json de respuesta
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            if (!dataExist.equals("")) {
                responseOk = true;
//            } else {
//                errorMessage = activityOrigin.getString(R.string.message_ws_response_fail);
//            }
        } else if (responseStatus >= 300 && responseStatus < 400) {
            errorMessage = activityOrigin.getString(R.string.message_ws_response_300);
        } else if (responseStatus >= 400 && responseStatus < 500) {
//            errorMessage = activityOrigin.getString(R.string.message_ws_response_400);
            String errorResponse = "";
            if (responseStatus == 404){
                errorResponse = "El usuario NO se encuentra registrado.";
            }
            errorMessage = responseStatus + " - " + errorResponse;
        } else if (responseStatus >= 500 && responseStatus < 600) {
//            errorMessage = activityOrigin.getString(R.string.message_ws_response_500);
            errorMessage = responseStatus + " - " + "Ocurrió un problema con el servidor";
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        progressDialog.dismiss();
        if (hasConecction) {
            if (responseOk) {
//                int operationINT=-1;
//                String messageResp = "";
//                try {
//                    operationINT = responseJSONObject.getInt("operationId");
//                    messageResp = responseJSONObject.getString("errorMessage");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                ((BaseActivity) activityOrigin).goNext();
//                SharedPreferencesUtils.saveToPreferencesString(activityOrigin, SharedPreferencesUtils.OPERATION_ID, operationINT+"");

//                AlertDialog dialogoAlert;
//                dialogoAlert = new AlertDialog(activityOrigin, activityOrigin.getString(R.string.message_ws_notice), messageResp, ApiConstants.ACTION_GO_NEXT);
//                dialogoAlert.setCancelable(false);
//                dialogoAlert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//                dialogoAlert.show();
            } else {
                Log.i("Message logout", "logout: " + errorMessage);
                AlertDialog dialogoAlert;
                dialogoAlert = new AlertDialog(activityOrigin, activityOrigin.getString(R.string.message_ws_notice), errorMessage, ApiConstants.ACTION_TRY_AGAIN_CANCEL);
                dialogoAlert.setCancelable(false);
                dialogoAlert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialogoAlert.show();
            }
        } else {
            Log.i("Message logout", "logout: " + errorMessage);
            AlertDialog dialogoAlert;
            dialogoAlert = new AlertDialog(activityOrigin, activityOrigin.getString(R.string.message_ws_notice), errorMessage, ApiConstants.ACTION_TRY_AGAIN_CANCEL);
            dialogoAlert.setCancelable(false);
            dialogoAlert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogoAlert.show();
        }
    }

}
