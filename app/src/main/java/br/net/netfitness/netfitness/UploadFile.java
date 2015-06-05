package br.net.netfitness.netfitness;


import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by Daniele on 04/06/2015.
 */
public class UploadFile {


    private String mNameFile;
    private String mNewFileName;
    private String mWebServiceName;
    private String mIdAluno;
    private String mSenhaAluno;
    private String mLoginAluno;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public UploadFile(String idALuno, String loginAluno, String senhaAluno, String fileName, String newFileName, String webServiceName) {

        mNameFile = fileName;
        mNewFileName = newFileName;
        mWebServiceName = webServiceName;
        mLoginAluno = loginAluno;
        mIdAluno = idALuno;
        mSenhaAluno = senhaAluno;
    }

    public JSONObject SendFile()  throws Exception
    {
        InputStream inputStream = null;
        byte[] data;

        try
        {
            inputStream = new FileInputStream(new File(mNameFile));
        } catch (FileNotFoundException e) {
            throw new Exception(e);
        }

        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, System.getProperty("http.agent"));

            HttpPost httpPost = new HttpPost(mWebServiceName);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(new File(mNameFile));
            builder.addPart("file", fileBody);
            builder.addTextBody("idAluno",mIdAluno, ContentType.TEXT_PLAIN);
            builder.addTextBody("login", mLoginAluno, ContentType.TEXT_PLAIN);
            builder.addTextBody("senha", mSenhaAluno, ContentType.TEXT_PLAIN);
            builder.addTextBody("novoNomeFile",mNewFileName, ContentType.TEXT_PLAIN);

            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            try {
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                Log.e("EXCEPTION", e.getMessage());
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                Log.e("EXCEPTION", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {

                Log.e("EXCEPTION", e.getMessage());
                e.printStackTrace();
            }


            try
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
                Log.e("JSON", json);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
                throw new Exception(e);
            }

            // try parse the string to a JSON object
            try
            {
                jObj = new JSONObject(json);
            }
            catch (JSONException e)
            {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                throw new Exception(e);
            }

            // return JSON  String
            return jObj;
        }
        catch(IOException e)
        {
            throw new Exception(e);
        }


    }

}
