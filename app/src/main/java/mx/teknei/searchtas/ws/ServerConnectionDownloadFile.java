package mx.teknei.searchtas.ws;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ServerConnectionDownloadFile {
    public static final String APPLICATION_JSON = "application/json";
    public static final String HEADER_TOKEN_CODE = "Authorization";
    public static final String HEADER_TOKEN_AUX_VALUE = "Token ";
    public static final String HEADER_BASIC_AUX_VALUE = "Basic ";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";

    private String tokenID = "";
    private String typeFile = "";
    public static final int TIME_OUT = 10000;
    Integer statusResponse = null;

    Bitmap bmp;

    public Object[] connection(Context context, String stringJSON, String serverMethod, String token, String method, String fileType) {
        // Creamos la peticion http
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, TIME_OUT);
        HttpClient clienteHTTP = new DefaultHttpClient(httpParameters);

        HttpResponse httpResponse = null;

        HttpPost httpPOST = null;
        HttpGet httpGet = null;
        HttpDelete httpDelete = null;
        this.tokenID = token;
        this.typeFile = fileType;
        //Selecciona que tipo de metodo crear
        switch (method) {
            case ServerConnectionDownloadFile.METHOD_POST:
                httpPOST = new HttpPost(serverMethod);
                break;
            case ServerConnectionDownloadFile.METHOD_GET:
                httpGet = new HttpGet(serverMethod);
                break;
            case ServerConnectionDownloadFile.METHOD_DELETE:
                httpDelete = new HttpDelete(serverMethod);
                break;
        }
        //Creamos la entidad de datos que enviaremos si existe el JSONOBJECT
        String sendJSON = stringJSON;
        if (sendJSON != null) {
            sendJSON = stringJSON.replaceAll("\\\\", "");
        }
        StringEntity entityData = null;
        try {
            if (sendJSON != null) {
                entityData = new StringEntity(sendJSON);
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        //Si hay entidad de datos se agrega al Post en caso de que el metodo POST tambien exista
        if (entityData != null ) {
            entityData.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
            if (httpPOST != null) {
                httpPOST.setEntity(entityData);
            }
        }

        // Ejecuta la peticion HTTP POST / GET / DELETE al servidor

        try {
            if (httpPOST != null) {
                //Normal headers add
                httpPOST.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                /****///Token add       //Authorization      //Token" "token_del_login *****************************
                httpPOST.addHeader(HEADER_TOKEN_CODE, HEADER_TOKEN_AUX_VALUE + tokenID);
                httpResponse = clienteHTTP.execute(httpPOST);
            } else if (httpGet != null) {
                //Normal headers add
                httpGet.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                //Token add
                httpGet.addHeader(HEADER_TOKEN_CODE, HEADER_TOKEN_AUX_VALUE + tokenID);
                httpResponse = clienteHTTP.execute(httpGet);
            } else if (httpDelete != null) {
                //Normal headers add
                httpDelete.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
                httpDelete.addHeader(HEADER_TOKEN_CODE, HEADER_TOKEN_AUX_VALUE + tokenID);
                httpResponse = clienteHTTP.execute(httpDelete);
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            Log.d("error Response", "Response: " + ee.getMessage());
        }

        if (httpResponse != null) {
            statusResponse = httpResponse.getStatusLine().getStatusCode();
            Log.i("Status response -> ", "estatus : " + statusResponse);
            HttpEntity entity = httpResponse.getEntity();
            try {
                InputStream is = entity.getContent();
                bmp = BitmapFactory.decodeStream(is);
                // if no directory exists, create new directory
//                if (!directory.exists()) {
//                    directory.mkdir();
//                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Object[]{bmp, statusResponse};
    }
}
