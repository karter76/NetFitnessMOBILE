package br.net.netfitness.netfitness;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import interfaces.OnVisualizarTreinosCompleted;
import interfaces.clicouNoTreinoListener;
import utils.JSONConvert;


public class AlunoActivity extends ActionBarActivity implements OnVisualizarTreinosCompleted, clicouNoTreinoListener {

    JSONObject json;
    private String[] items;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int selectedPosition;
    private TextView mensagem;
    private HashMap<String,Object> result;
    protected JSONObject jsonReturned;
    protected AsynckTaskListarTreinos listarTreinosTask;

    private static final int VISUALIZAR_TREINOS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        if(LoginActivity.instance != null) {
            try {
                LoginActivity.instance.finish();
            } catch (Exception e) {}
        }

        mensagem = (TextView)findViewById(R.id.outputMessage);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            try
            {
                json = new JSONObject(extras.getString("EXTRA_USUARIO_JSON"));
                mensagem.setText(json.getString("mensagem"));
                result = (HashMap<String, Object>) JSONConvert.jsonToMap(json.getJSONObject("usuario"));
                getSupportActionBar().setTitle(getString(R.string.app_name)+" - "+result.get("Pessoa.nome"));



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        items = getResources().getStringArray(R.array.menu_aluno);

        /* Getting reference to the DrawerLayout */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.aluno_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        /* Creating an ArrayAdapter to add items to mDrawerList */
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.drawer_list_item, items);

        /* Setting the adapter to mDrawerList */
        mDrawerList.setAdapter(adapter);

        // Setting item click listener to mDrawerList
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                selectedPosition = position;


                mensagem.setText("");

                switch (selectedPosition)
                {
                    case VISUALIZAR_TREINOS : visualizarTreinos();
                        break;
                }

                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }

    private void visualizarTreinos()
    {
        listarTreinosTask = new AsynckTaskListarTreinos(this);
        listarTreinosTask.execute((String)result.get("Aluno.idAluno"), (String)result.get("Pessoa.login"),(String)result.get("Pessoa.senha"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aluno, menu);
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

    @Override
    public void onVisualizarTreinosCompleted() throws JSONException
    {

        VisualizarTreinosFragment fragmentListaTreinos = VisualizarTreinosFragment.newInstance(JSONConvert.toList(jsonReturned.getJSONArray("listaTreinos")));
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame_aluno, fragmentListaTreinos, "FragmentListaTreinos");
        ft.commit();

        //ft.addToBackStack(null);
        //fm.popBackStack();
    }


    @Override
    public void aoClicarNoTreino(Object object) {

    }

    private class AsynckTaskListarTreinos extends AsyncTask<String, String, JSONObject>
    {
        private OnVisualizarTreinosCompleted listener;

        public AsynckTaskListarTreinos(OnVisualizarTreinosCompleted listener)
        {
            this.listener=listener;
        }


        @Override
        protected JSONObject doInBackground(String... params) {

            try
            {
                JSONParser jsonParser = new JSONParser();
                List jsonParams = new ArrayList();
                jsonParams.add(new BasicNameValuePair("idAluno", params[0]));
                jsonParams.add(new BasicNameValuePair("login", params[1]));
                jsonParams.add(new BasicNameValuePair("senha", params[2]));
                JSONObject json = jsonParser.getJSONFromUrl(getResources().getString(R.string.web_service_listar_treinos_aluno), jsonParams);

                return json;

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonResult)
        {
            super.onPostExecute(jsonResult);
            jsonReturned = jsonResult;

            try
            {
                if (!jsonResult.getString("listaTreinos").equals("null"))
                {
                    listener.onVisualizarTreinosCompleted();
                }
                else
                {
                    Toast toast = Toast.makeText(getBaseContext(), jsonResult.getString("mensagem"), Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
