package br.net.netfitness.netfitness;

import android.app.ProgressDialog;
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

import interfaces.ClicouNoInserirTreinoListener;
import interfaces.OnExcluirTreinoCompleted;
import interfaces.OnInserirTreinoCompleted;
import interfaces.OnTaskTreinoCompleted;
import interfaces.OnVisualizarTreinosCompleted;
import interfaces.ClicouNoExcluirTreinoListener;
import interfaces.ClicouNoTreinoListener;
import utils.JSONConvert;


public class InstrutorActivity extends ActionBarActivity implements OnVisualizarTreinosCompleted,
                                                                    OnExcluirTreinoCompleted,
                                                                    OnInserirTreinoCompleted,
                                                                    OnTaskTreinoCompleted,
                                                                    ClicouNoTreinoListener,
                                                                    ClicouNoExcluirTreinoListener,
                                                                    ClicouNoInserirTreinoListener
{

    JSONObject json;
    private String[] items;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int selectedPosition;
    private TextView mensagem;
    private HashMap<String,Object> result;
    protected JSONObject jsonReturned;
    protected AsyncTaskTreino treinoTask;
    List<Object> listaExerciciosTreino;
    List<Object> listaExercicios;
    List<Object> listaExerciciosNaoSelecionados;
    String nomeTreino;
    String descricaoTreino;
    String idTreino;
    ProgressDialog progress;
    boolean foiAlterado = false;

    private static final int INICIALIZAR_LISTA = -1;
    private static final int INSERIR_TREINO = 0;
    private static final int EXCLUIR_TREINO = 1;
    private static final int VISUALIZAR_TREINOS = 2;
    private static final int ALTERAR_TREINO = 3;


    public int mAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrutor);

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
                Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }


        items = getResources().getStringArray(R.array.menu_instrutor);

        /* Getting reference to the DrawerLayout */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.instrutor_layout);
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

              mensagem.setVisibility(View.GONE);
               //mensagem.setTextColor(Color.parseColor("#ffffff"));

                switch (selectedPosition)
                {
                    case VISUALIZAR_TREINOS : visualizarTreinos();
                         mAction = VISUALIZAR_TREINOS;
                         break;

                    case EXCLUIR_TREINO: excluirTreino();
                        break;

                    case INSERIR_TREINO: inserirTreino();

                        break;

                    case ALTERAR_TREINO: alterarTreino();
                }

                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });

        showProgress();
        mAction = INICIALIZAR_LISTA;

        treinoTask = new AsyncTaskTreino(this);

        treinoTask.execute((String) result.get("Instrutor.idInstrutor"), (String) result.get("Pessoa.login"), (String) result.get("Pessoa.senha"), "", getResources().getString(R.string.web_service_listar_exercicios),"","","");


    }


    private void alterarTreino()
    {
        mAction = ALTERAR_TREINO;
        executarTask();
    }


    private void inserirTreino()
    {
        mAction = INSERIR_TREINO;
        executarTask();
    }

    private void visualizarTreinos()
    {
        mAction = VISUALIZAR_TREINOS;
        executarTask();
    }

    private void excluirTreino()
    {
        mAction = EXCLUIR_TREINO;
        executarTask();
    }

    private void showProgress()
    {
        progress = new ProgressDialog(this);
        progress.setTitle(getResources().getString(R.string.loading));
        progress.setMessage(getResources().getString(R.string.wait_loading));
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    private void executarTask()
    {

        showProgress();

        treinoTask = new AsyncTaskTreino(this);

        if(mAction == INSERIR_TREINO)
        {
            treinoTask.execute((String) result.get("Instrutor.idInstrutor"), (String) result.get("Pessoa.login"), (String) result.get("Pessoa.senha"), "", getResources().getString(R.string.web_service_listar_exercicios),"","","");
        }
        else
        {
            treinoTask.execute((String) result.get("Instrutor.idInstrutor"), (String) result.get("Pessoa.login"), (String) result.get("Pessoa.senha"), "", getResources().getString(R.string.web_service_listar_treinos_instrutor),"","","");
        }
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



    @Override
    public void onVisualizarTreinosCompleted() throws JSONException
    {

        if (mAction == VISUALIZAR_TREINOS)
        {
            VisualizarTreinosFragment fragmentListaTreinos = VisualizarTreinosFragment.newInstance(JSONConvert.toList(jsonReturned.getJSONArray("listaTreinos")));
            mudarFragment(fragmentListaTreinos, R.id.content_frame_instrutor, "FragmentListaTreinos",false);
        }

        if(mAction == EXCLUIR_TREINO)
        {
            ExcluirTreinoFragment fragmentExcluirTreino = ExcluirTreinoFragment.newInstance(JSONConvert.toList(jsonReturned.getJSONArray("listaTreinos")));
            mudarFragment(fragmentExcluirTreino, R.id.content_frame_instrutor, "FragmentExcluirTreino",false);
        }
        //ft.addToBackStack(null);
        //fm.popBackStack();

    }

    private void mudarFragment(android.support.v4.app.Fragment fragment, int content, String nomeFragment, boolean addToBS)
    {
        mensagem.setText("");
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
    public void aoClicarNoTreino(Object object)
    {
        if(mAction == VISUALIZAR_TREINOS)
        {
            DetalharTreinoFragment fragmentDetalharTreino = DetalharTreinoFragment.newInstance(object);
            mudarFragment(fragmentDetalharTreino, R.id.content_frame_instrutor, "FragmentDetalharTreino", true);
        }

        if(mAction == ALTERAR_TREINO)
        {

                listaExerciciosTreino = new ArrayList<>();
                HashMap<String, Object> objectMap = new HashMap<>();
                objectMap = (HashMap<String, Object>) object;
                listaExerciciosTreino = (List<Object>) objectMap.get("listaExercicios");

                listaExerciciosNaoSelecionados = new ArrayList<>(listaExercicios);

                for(Object exercicio : listaExercicios)
                {
                    HashMap<String, String> hashExercicio = new HashMap<>();
                    hashExercicio = (HashMap<String, String>) exercicio;
                    String idExercicio = hashExercicio.get("idExercicio");

                    for (Object exercicioTreino: listaExerciciosTreino)
                    {
                        HashMap<String, String> hashExercicioTreino = new HashMap<>();
                        hashExercicioTreino = (HashMap<String, String>) exercicioTreino;
                        String idExercicioTreino = hashExercicioTreino.get("idExercicio");
                        if(idExercicio.equals(idExercicioTreino))
                        {
                            listaExerciciosNaoSelecionados.remove(exercicio);
                            break;
                        }
                    }


                }

            listaExerciciosTreino.addAll(listaExerciciosNaoSelecionados);
            idTreino = (String)objectMap.get("idTreino");
            nomeTreino = (String) objectMap.get("nome");
            descricaoTreino = (String) objectMap.get("descricao");
            InserirTreinoFragment fragmentInserirTreino = InserirTreinoFragment.newInstance(listaExerciciosTreino, nomeTreino, descricaoTreino, mAction);
            mudarFragment(fragmentInserirTreino, R.id.content_frame_instrutor, "FragmentInserirTreino", false);

        }
    }

    @Override
    public void aoExcluirOTreino(Object object) {

       HashMap<String,String> mapObject = (HashMap<String, String>) object;
       String idTreino = mapObject.get("idTreino");
       JSONObject Pessoa;
       String login;
       String senha;

        try
        {
           Pessoa = json.getJSONObject("usuario");
           login = Pessoa.getString("Pessoa.login");
           senha = Pessoa.getString("Pessoa.senha");

           //excluirTreinoTask = new AsyncTaskExcluirTreino(this);
           //excluirTreinoTask.execute(login, senha, idTreino);
            showProgress();
            treinoTask = new AsyncTaskTreino(this);
            treinoTask.execute("",login, senha, idTreino, getResources().getString(R.string.web_service_excluir_treino),"","","");

        } catch (JSONException e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    @Override
    public void onExcluirTreinoCompleted(String message) throws JSONException {

        Toast toast = Toast.makeText(getBaseContext(),message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onTaskTreinoCompleted(String message) throws JSONException {

        if(mAction == INICIALIZAR_LISTA)
        {
            listaExercicios = new ArrayList<>();
            listaExercicios = JSONConvert.toList(jsonReturned.getJSONArray("listaExercicios"));
        }

        if (mAction == INSERIR_TREINO)
        {
            if(!message.equals(""))
            {
                onInserirTreinoCompleted(message);
            }
            else
            {
                listaExercicios = new ArrayList<>();
                listaExercicios = JSONConvert.toList(jsonReturned.getJSONArray("listaExercicios"));
                InserirTreinoFragment fragmentInserirTreino = InserirTreinoFragment.newInstance(listaExercicios, "", "", mAction);
                mudarFragment(fragmentInserirTreino, R.id.content_frame_instrutor, "FragmentInserirTreino", false);
            }
        }

        if (mAction == VISUALIZAR_TREINOS)
        {
            VisualizarTreinosFragment fragmentListaTreinos = VisualizarTreinosFragment.newInstance(JSONConvert.toList(jsonReturned.getJSONArray("listaTreinos")));
            mudarFragment(fragmentListaTreinos, R.id.content_frame_instrutor, "FragmentListaTreinos",false);
        }

        if (mAction == ALTERAR_TREINO )
        {
            if(!foiAlterado) {
                VisualizarTreinosFragment fragmentListaTreinos = VisualizarTreinosFragment.newInstance(JSONConvert.toList(jsonReturned.getJSONArray("listaTreinos")));
                mudarFragment(fragmentListaTreinos, R.id.content_frame_instrutor, "FragmentListaTreinos", false);
                foiAlterado = true;
            }
            else
            {
                foiAlterado = false;
                Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
                toast.show();
                alterarTreino();
            }
        }

        if(mAction == EXCLUIR_TREINO)
        {
            if(message.equals(getResources().getString(R.string.treino_deleted)))
            {
                Toast toast = Toast.makeText(this,message, Toast.LENGTH_SHORT);
                toast.show();
                excluirTreino();
            }
            else
            {
                ExcluirTreinoFragment fragmentExcluirTreino = ExcluirTreinoFragment.newInstance(JSONConvert.toList(jsonReturned.getJSONArray("listaTreinos")));
                mudarFragment(fragmentExcluirTreino, R.id.content_frame_instrutor, "FragmentExcluirTreino",false);
            }
        }


    }

    @Override
    public void aoInserirOTreino(String nomeTreino, String descricaoTreino, ArrayList<HashMap<String, String>> listaDadosExercicios) throws JSONException {
       // Toast toast = Toast.makeText(this,listObjects.toString(), Toast.LENGTH_SHORT);
       // toast.show();

        JSONObject Pessoa;
        String login;
        String senha;

        try
        {
            Pessoa = json.getJSONObject("usuario");
            login = Pessoa.getString("Pessoa.login");
            senha = Pessoa.getString("Pessoa.senha");

            showProgress();
            treinoTask = new AsyncTaskTreino(this);

            if(mAction == INSERIR_TREINO)
            {
                treinoTask.execute((String) result.get("Instrutor.idInstrutor"), login, senha, "", getResources().getString(R.string.web_service_inserir_treino), nomeTreino, descricaoTreino, listaDadosExercicios.toString());
            }

            if(mAction == ALTERAR_TREINO)
            {
                treinoTask.execute((String) result.get("Instrutor.idInstrutor"), login, senha, idTreino, getResources().getString(R.string.web_service_alterar_treino), nomeTreino, descricaoTreino, listaDadosExercicios.toString());
            }

        } catch (JSONException e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public void onInserirTreinoCompleted(String message) throws JSONException {
        Toast toast = Toast.makeText(getBaseContext(),message, Toast.LENGTH_SHORT);
        toast.show();
        visualizarTreinos();


    }

    private class AsyncTaskTreino extends  AsyncTask<String, Void, JSONObject>
    {
        private OnTaskTreinoCompleted listener;
        private Exception asynkTaskException;

        private AsyncTaskTreino(OnTaskTreinoCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject json = new JSONObject();
            try
            {

                JSONParser jsonParser = new JSONParser();
                List jsonParams = new ArrayList();
                jsonParams.add(new BasicNameValuePair("idInstrutor",params[0] ));
                jsonParams.add(new BasicNameValuePair("login",new String(params[1].getBytes("UTF-8"), "ISO-8859-1") ));
                jsonParams.add(new BasicNameValuePair("senha", new String(params[2].getBytes("UTF-8"), "ISO-8859-1") ));
                jsonParams.add(new BasicNameValuePair("idTreino", params[3]));

                if(!params[5].equals(""))
                {
                    jsonParams.add(new BasicNameValuePair("nomeTreino", new String(params[5].getBytes("UTF-8"), "ISO-8859-1") ));
                    jsonParams.add(new BasicNameValuePair("descricaoTreino", new String(params[6].getBytes("UTF-8"), "ISO-8859-1") ));
                    jsonParams.add(new BasicNameValuePair("listaExercicios", params[7]));
                }

                json = jsonParser.getJSONFromUrl(params[4], jsonParams);





            }
            catch (Exception e)
            {
                asynkTaskException = e;
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonResult)
        {
            super.onPostExecute(jsonResult);
            jsonReturned = jsonResult;
            progress.dismiss();
            if (asynkTaskException == null) {
                try {
                    if (mAction == INICIALIZAR_LISTA) {
                        listener.onTaskTreinoCompleted("");
                    }

                    if (mAction == ALTERAR_TREINO) {
                        listener.onTaskTreinoCompleted(jsonResult.getString("mensagem"));
                    }

                    if (mAction == INSERIR_TREINO) {
                        if (!jsonResult.getString("listaExercicios").equals("null")) {
                            listener.onTaskTreinoCompleted("");
                        } else {
                            listener.onTaskTreinoCompleted(jsonResult.getString("mensagem"));
                        }
                    }


                    if (mAction == VISUALIZAR_TREINOS) {
                        if (!jsonResult.getString("listaTreinos").equals("null")) {
                            listener.onTaskTreinoCompleted("");
                        } else {
                            listener.onTaskTreinoCompleted(jsonResult.getString("mensagem"));
                        }

                    }

                    if (mAction == EXCLUIR_TREINO) {
                        listener.onTaskTreinoCompleted(jsonResult.getString("mensagem"));
                    }


                } catch (JSONException e) {
                    Toast toast = Toast.makeText(InstrutorActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else
            {
                Toast toast = Toast.makeText(InstrutorActivity.this, asynkTaskException.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }



}
