package br.net.netfitness.netfitness;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Objects;

import interfaces.OnVisualizarExamesFisicosCompleted;
import interfaces.OnVisualizarTreinosCompleted;
import interfaces.clicouNoTreinoListener;
import utils.JSONConvert;


public class AlunoActivity extends ActionBarActivity implements OnVisualizarTreinosCompleted, clicouNoTreinoListener,
                                                                OnVisualizarExamesFisicosCompleted{

    JSONObject json;
    private String[] items;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int selectedPosition;
    private TextView mensagem;
    private HashMap<String,Object> result;
    protected JSONObject jsonReturned;
    protected ArrayList<Object> listaExamesFisicos;
    protected AsynckTaskListarTreinos listarTreinosTask;
    protected AsynkTaskListarGraficos listarGraficosTask;
    ProgressDialog progress;

    private static final int VISUALIZAR_TREINOS = 0;
    private static final int VISUALIZAR_GRAFICOS = 1;

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

                    case VISUALIZAR_GRAFICOS : visualizarGraficos();
                }



                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }

    private void showProgress()
    {
        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.loading));
        progress.setMessage(getResources().getString(R.string.wait_loading));
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    private void hideProgress()
    {
        progress.dismiss();
    }

    private void visualizarGraficos()
    {
        showProgress();
        listarGraficosTask = new AsynkTaskListarGraficos(this);
        listarGraficosTask.execute((String)result.get("Aluno.idAluno"), (String)result.get("Pessoa.login"),(String)result.get("Pessoa.senha"));
    }

    private void visualizarTreinos()
    {
        showProgress();
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

    private void mudarFragment(android.support.v4.app.Fragment fragment, int content, String nomeFragment, boolean addToBS)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(content, fragment, nomeFragment);
        if(addToBS)
        {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onVisualizarTreinosCompleted() throws JSONException
    {

        VisualizarTreinosFragment fragmentListaTreinos = VisualizarTreinosFragment.newInstance(JSONConvert.toList(jsonReturned.getJSONArray("listaTreinos")));
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame_aluno, fragmentListaTreinos, "FragmentListaTreinos");
        ft.commit();
    }


    @Override
    public void aoClicarNoTreino(Object object) {

    }

    @Override
    public void onVisualizarExamesFisicosCompleted(String message) throws JSONException {

        if(!jsonReturned.getString("mensagem").equals("notNull"))
        {
            Toast toast = Toast.makeText(this,jsonReturned.getString("mensagem"), Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {
            listaExamesFisicos = new ArrayList<Object>();
           // HashMap<String, S> objectMap = new HashMap<>();
            listaExamesFisicos = (ArrayList<Object>) JSONConvert.toList(jsonReturned.getJSONArray("listaExamesFisicos"));

            GraficoFragment fragmentGrafico = GraficoFragment.newInstance(listaExamesFisicos);
            mudarFragment(fragmentGrafico, R.id.content_frame_aluno, "FragmentGrafico",false);
        }
    }

    private class AsynkTaskListarGraficos extends AsyncTask<String, String, JSONObject>
    {

        private OnVisualizarExamesFisicosCompleted listener;

        private AsynkTaskListarGraficos(OnVisualizarExamesFisicosCompleted listener) {
            this.listener = listener;
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
                JSONObject json = jsonParser.getJSONFromUrl(getResources().getString(R.string.web_service_listar_exames_fisicos_aluno), jsonParams);

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
            
            hideProgress();

            try
            {
                if (!jsonResult.getString("listaExamesFisicos").equals("null"))
                {
                    listener.onVisualizarExamesFisicosCompleted("");
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

            hideProgress();

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
