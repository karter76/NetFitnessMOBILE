package br.net.netfitness.netfitness;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity {


    JSONObject jsonReturned;
    AsynckTaskLogin loginTask;
    public static Activity instance;
    ProgressDialog progress;

    private boolean temConexao()
    {
        ConnectivityManager cm = (ConnectivityManager)
        this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        setContentView(R.layout.activity_login);



        final EditText inputLogin = (EditText)findViewById(R.id.inputLogin);
        final EditText inputSenha = (EditText)findViewById(R.id.inputSenha);


        Button buttonLogin = (Button)findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (temConexao())
                {
                    loginTask = new AsynckTaskLogin(v.getContext());
                    //loginTask.execute(inputLogin.getText().toString(), inputSenha.getText().toString());
                    //loginTask.execute("123", "123");
                    //loginTask.execute("aaa", "aaa");
                    loginTask.execute("pinolone", "pignolone");
                    //loginTask.execute("bbb", "bbb");
                    progress = new ProgressDialog(instance);
                    progress.setTitle(getResources().getString(R.string.loading));
                    progress.setMessage(getResources().getString(R.string.wait_loading));
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                }
                else
                {
                    Toast toast = Toast.makeText(v.getContext(), R.string.no_connection, Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class AsynckTaskLogin extends AsyncTask<String, String, JSONObject>
    {
        private Context mContext;

        public AsynckTaskLogin(Context context)
        {
            mContext = context;
        }

        @Override
        protected JSONObject doInBackground(String... params)
        {

            try
            {
                JSONParser jsonParser = new JSONParser();
                List jsonParams = new ArrayList();
                jsonParams.add(new BasicNameValuePair("login", params[0]));
                jsonParams.add(new BasicNameValuePair("senha", params[1]));
                JSONObject json = jsonParser.getJSONFromUrl(getResources().getString(R.string.web_service_login), jsonParams);

                return json;

            }
            catch (Exception e)
            {
                Toast toast = Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonResult)
        {
            super.onPostExecute(jsonResult);
            jsonReturned = jsonResult;

            progress.dismiss();

            try
            {
                if(jsonResult.getString("tipoUsuario").equals("Desconhecido")) {
                    Toast toast = Toast.makeText(getBaseContext(), jsonResult.getString("mensagem"), Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    if(jsonResult.getString("tipoUsuario").equals("Instrutor"))
                    {
                        Intent intent = new Intent(getBaseContext(), InstrutorActivity.class);
                        intent.putExtra("EXTRA_USUARIO_JSON", jsonResult.toString());
                        startActivity(intent);
                    }
                    else if(jsonResult.getString("tipoUsuario").equals("Aluno"))
                    {
                        Intent intent = new Intent(getBaseContext(), AlunoActivity.class);
                        intent.putExtra("EXTRA_USUARIO_JSON", jsonResult.toString());
                        startActivity(intent);
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getBaseContext(), R.string.user_not_allowed, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
            catch (JSONException e)
            {
                Toast toast = Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }
}
