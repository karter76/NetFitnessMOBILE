package br.net.netfitness.netfitness;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import interfaces.ClicouNaFotoListener;
import interfaces.ClicouNaNoticiaListener;
import interfaces.ClicouNoCompararGraficos;
import interfaces.ClicouNoConfirmarMudarFoto;
import interfaces.ClicouNoHistoricoTreinoListener;
import interfaces.ClicouNoMudarFoto;
import interfaces.OnUploadCompleted;
import interfaces.OnVisualizarExamesFisicosCompleted;
import interfaces.OnVisualizarHistoricoTreinoCompleted;
import interfaces.OnVisualizarNoticiasCompleted;
import interfaces.OnVisualizarTreinosCompleted;
import interfaces.ClicouNoTreinoListener;
import utils.Data;
import utils.JSONConvert;

import static org.apache.http.HttpStatus.SC_OK;


public class AlunoActivity extends ActionBarActivity implements OnVisualizarTreinosCompleted, ClicouNoTreinoListener,
                                                                OnVisualizarExamesFisicosCompleted, ClicouNoHistoricoTreinoListener,
                                                                OnVisualizarHistoricoTreinoCompleted, ClicouNoCompararGraficos,
                                                                ClicouNoMudarFoto, ClicouNaFotoListener, ClicouNoConfirmarMudarFoto,
                                                                OnUploadCompleted, OnVisualizarNoticiasCompleted,
                                                                ClicouNaNoticiaListener {

    JSONObject json;
    private String[] items;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private int selectedPosition;
    private TextView mensagem;
    private String nomeFotoAluno = "";
    private HashMap<String,Object> result;
    private  String mGraficosSelecionados;
    protected JSONObject jsonReturned;
    protected ArrayList<Object> listaExamesFisicos;
    protected AsynckTaskListarTreinos listarTreinosTask;
    protected AsynkTaskListarGraficos listarGraficosTask;
    protected AsynkTaskVisualizarHistoricoTreinos visualizarHistoricoTreinosTask;
    protected AsynkTaskListarNoticias listarNoticiasTask;

    ProgressDialog progress;

    private static final int VISUALIZAR_TREINOS = 0;
    private static final int VISUALIZAR_GRAFICOS = 1;
    private static final int COMPARAR_GRAFICOS = 2;
    private static final int CALCULAR_IMC = 3;
    private static final int MOSTRAR_FOTO = 4;
    private static final int VISUALIZAR_NOTICIAS = 5;

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
                Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
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

                    case VISUALIZAR_GRAFICOS : visualizarGraficos(null);
                        break;

                    case COMPARAR_GRAFICOS : compararGraficos();
                        break;

                    case CALCULAR_IMC : calcularIMC();
                        break;

                    case MOSTRAR_FOTO : mostarFoto();
                        break;

                    case VISUALIZAR_NOTICIAS : visualizarNoticias();
                        break;
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

    private void visualizarNoticias(){

        showProgress();
        listarNoticiasTask = new AsynkTaskListarNoticias(this);
        listarNoticiasTask.execute((String) result.get("Aluno.idAluno"), (String) result.get("Pessoa.login"), (String) result.get("Pessoa.senha"));
    }

    private void hideProgress()
    {
        progress.dismiss();
    }



    private void mostarFoto()
    {
        String fotoAluno;
        try
        {
            if(nomeFotoAluno.equals("")) {
                fotoAluno = json.getJSONObject("usuario").getString("Aluno.foto");
            }
            else
            {
                fotoAluno = nomeFotoAluno;
            }
            FotoFragment fragmentFoto = FotoFragment.newInstance(fotoAluno, null);
            mudarFragment(fragmentFoto, R.id.content_frame_aluno, "FragmentFoto", false);
        }
        catch (JSONException e)
        {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }


    private void compararGraficos()
    {
        SelecionarGraficoFragment fragmentSelecionarGraficos = SelecionarGraficoFragment.newInstance();
        mudarFragment(fragmentSelecionarGraficos, R.id.content_frame_aluno, "FragmentSelecionarGraficos", false);
    }

    private void calcularIMC(){

        CalcularIMCFragment fragmentIMC = CalcularIMCFragment.newInstance();
        mudarFragment(fragmentIMC, R.id.content_frame_aluno, "FragmentIMC", false);
    }

    private void visualizarHistoricosTreinos(Object treino)
    {
        showProgress();
        HashMap<String, String> mapTreino = (HashMap<String, String>) treino;
        visualizarHistoricoTreinosTask = new AsynkTaskVisualizarHistoricoTreinos(this);
        visualizarHistoricoTreinosTask.execute((String) result.get("Aluno.idAluno"), (String) result.get("Pessoa.login"), (String) result.get("Pessoa.senha"), mapTreino.get("idTreino"));
    }

    private void visualizarGraficos(String listaGraficosSelecionados)
    {
        showProgress();
        mGraficosSelecionados = listaGraficosSelecionados;
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

        int id = item.getItemId();
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
        mudarFragment(fragmentListaTreinos, R.id.content_frame_aluno, "FragmentListaTreinos", false);
    }


    @Override
    public void aoClicarNoTreino(Object object) {
        DetalharTreinoFragment fragmentDetalharTreino = DetalharTreinoFragment.newInstance(object);
        mudarFragment(fragmentDetalharTreino, R.id.content_frame_aluno, "FragmentDetalharTreino", true);

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
            if(message.equals("")) {
                listaExamesFisicos = new ArrayList<Object>();
                listaExamesFisicos = (ArrayList<Object>) JSONConvert.toList(jsonReturned.getJSONArray("listaExamesFisicos"));

                GraficoFragment fragmentGrafico = GraficoFragment.newInstance(listaExamesFisicos);
                mudarFragment(fragmentGrafico, R.id.content_frame_aluno, "FragmentGrafico", false);
            }
            else
            {
                if(message.contains("#comparar#"))
                {
                    message = message.replace("#comparar#","");

                    listaExamesFisicos = new ArrayList<Object>();
                    listaExamesFisicos = (ArrayList<Object>) JSONConvert.toList(jsonReturned.getJSONArray("listaExamesFisicos"));

                    CompararGraficosFragment fragmentCompararGraficos = CompararGraficosFragment.newInstance(listaExamesFisicos, message);
                    mudarFragment(fragmentCompararGraficos, R.id.content_frame_aluno, "FragmentCompararGraficos", false);

                    //Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
                    //toast.show();
                }
                else
                {
                    Toast toast = Toast.makeText(this, jsonReturned.getString("mensagem"), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

    @Override
    public void aoClicarNoHistoricoTreinoListener(Object treino) {
        visualizarHistoricosTreinos(treino);
    }

    @Override
    public void onVisualizaHistoricoTreinoCompleted(String message) throws JSONException {


        VisualizarHistoricoTreinoFragment fragmentHistoricoTreino = VisualizarHistoricoTreinoFragment.newInstance(jsonReturned.getJSONObject("listaTreinosRealizados"));
        mudarFragment(fragmentHistoricoTreino, R.id.content_frame_aluno, "FragmentHistoricoTreino", false);
          //Toast toast = Toast.makeText(this,"Historico", Toast.LENGTH_SHORT);
          //toast.show();
    }

    @Override
    public void aoCompararGraficos(String graficosSelecionados) {
        visualizarGraficos(graficosSelecionados);
    }

    @Override
    public void aoClicarNoMudarFoto(File arquivo) {
        //Toast toast = Toast.makeText(this, nomeNovaFotoAluno, Toast.LENGTH_SHORT);
        //toast.show();

        ListarArquivosFragment fragmentListarArquivos = ListarArquivosFragment.newInstance(arquivo);
        mudarFragment(fragmentListarArquivos, R.id.content_frame_aluno, "FragmentListarArquivos", false);
    }

    @Override
    public void aoClicarNaFoto(File arquivo) {
        FotoFragment fragmentFoto = FotoFragment.newInstance("", arquivo);
        mudarFragment(fragmentFoto, R.id.content_frame_aluno, "FragmentFoto", false);
    }

    @Override
    public void aoClicarNoConfirmarMudarFoto(File arquivo) {
        showProgress();
        AsyncTaskMudarFoto mudarFotoTask = new AsyncTaskMudarFoto(this);
        mudarFotoTask.execute((String)result.get("Aluno.idAluno"), (String)result.get("Pessoa.login"),(String)result.get("Pessoa.senha"), arquivo.getPath());
    }

    @Override
    public void onUploadCompleted(JSONObject json)
    {
        Toast toast = null;
        String endereco_foto;
        ImageView fotoAtualizada;
        Button buttonFragmentFoto;

        try
        {
           // JSONObject person =  jsonArray.getJSONObject(0).getJSONObject("person");
           // person.put("name", "Sammie");


            toast = Toast.makeText(this, json.getString("mensagem"), Toast.LENGTH_SHORT);
            toast.show();
            buttonFragmentFoto = (Button) findViewById(R.id.buttonConfirmarFoto);
            buttonFragmentFoto.setVisibility(View.GONE);
            fotoAtualizada = (ImageView) findViewById(R.id.fotoAluno);
            endereco_foto = getResources().getString(R.string.endereco_fotos_alunos) + nomeFotoAluno;
            Picasso.with(this).load(endereco_foto).fit().centerCrop().into(fotoAtualizada);

        } catch (JSONException e) {
            toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public void aoClicarNaNoticia(Object noticia) {

    }

    @Override
    public void onVisualizarNoticiasCompleted() throws JSONException {

        VisualizarNoticiasFragment fragmentNoticias = VisualizarNoticiasFragment.newInstance(JSONConvert.toList(jsonReturned.getJSONArray("listaNoticias")));
        mudarFragment(fragmentNoticias, R.id.content_frame_aluno, "FragmentListaNoticias", false);

        VisualizarTreinosFragment fragmentListaTreinos = VisualizarTreinosFragment.newInstance(JSONConvert.toList(jsonReturned.getJSONArray("listaTreinos")));
        mudarFragment(fragmentListaTreinos, R.id.content_frame_aluno, "FragmentListaTreinos", false);
    }

    private class AsyncTaskMudarFoto extends AsyncTask<String, Void, JSONObject>
    {
        private OnUploadCompleted listener;


        private AsyncTaskMudarFoto(OnUploadCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject json = null;
            String splittedNomeFoto[];
            String nomeFotoNovo;

            splittedNomeFoto = params[3].split("/");
            nomeFotoNovo = FilenameUtils.removeExtension(splittedNomeFoto[splittedNomeFoto.length-1]);

            nomeFotoNovo += "_"+Data.generateString(5)+".";
            nomeFotoNovo += FilenameUtils.getExtension(splittedNomeFoto[splittedNomeFoto.length-1]);

            try
            {
                UploadFile uf= new UploadFile(params[0], params[1], params[2], params[3], nomeFotoNovo, getResources().getString(R.string.web_service_upload_file));
                json = uf.SendFile();
            }
            catch (Exception e)
            {
                Toast toast = Toast.makeText(AlunoActivity.this, getResources().getString(R.string.file_not_uploaded) + e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }


            try {
                nomeFotoAluno = json.getString("nomeFoto");
            } catch (JSONException e) {
                Toast toast = Toast.makeText(AlunoActivity.this,  e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
            return json;

        }

        @Override
        protected void onPostExecute(JSONObject jsonResult) {
            super.onPostExecute(jsonResult);

            super.onPostExecute(jsonResult);

            jsonReturned = jsonResult;


            hideProgress();
            try {
                listener.onUploadCompleted(jsonResult);
            } catch (JSONException e) {
                Toast toast = Toast.makeText(AlunoActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }

    private class AsynkTaskVisualizarHistoricoTreinos extends AsyncTask<String, String, JSONObject>
    {
        private OnVisualizarHistoricoTreinoCompleted listener;
        private Exception asynkTaskException;

        private AsynkTaskVisualizarHistoricoTreinos(OnVisualizarHistoricoTreinoCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject json = new JSONObject();
            try
            {
                JSONParser jsonParser = new JSONParser();
                List jsonParams = new ArrayList();
                jsonParams.add(new BasicNameValuePair("idAluno", params[0]));
                jsonParams.add(new BasicNameValuePair("login", params[1]));
                jsonParams.add(new BasicNameValuePair("senha", params[2]));
                jsonParams.add(new BasicNameValuePair("idTreino", params[3]));
                json = jsonParser.getJSONFromUrl(getResources().getString(R.string.web_service_listar_treinos_realizados), jsonParams);

                return json;

            }
            catch (Exception e)
            {
                asynkTaskException = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonResult) {
            super.onPostExecute(jsonResult);

            jsonReturned = jsonResult;

            hideProgress();
            if (asynkTaskException == null) {
                try {
                    listener.onVisualizaHistoricoTreinoCompleted("");
                } catch (JSONException e) {
                    Toast toast = Toast.makeText(AlunoActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            else
            {
                Toast toast = Toast.makeText(AlunoActivity.this, asynkTaskException.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
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
                Toast toast = Toast.makeText(AlunoActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
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
                    if(mGraficosSelecionados == null) {
                        listener.onVisualizarExamesFisicosCompleted("");
                    }
                    else
                    {
                        listener.onVisualizarExamesFisicosCompleted(mGraficosSelecionados);
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(getBaseContext(), jsonResult.getString("mensagem"), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            catch (JSONException e)
            {
                Toast toast = Toast.makeText(AlunoActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
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
                Toast toast = Toast.makeText(AlunoActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
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
                Toast toast = Toast.makeText(AlunoActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private class AsynkTaskListarNoticias extends AsyncTask<String, String, JSONObject>{

        private OnVisualizarNoticiasCompleted listener;

        public AsynkTaskListarNoticias(OnVisualizarNoticiasCompleted listener){
            this.listener = listener;
        }


        @Override
        protected JSONObject doInBackground(String... params) {

            try{
                JSONParser jsonParser = new JSONParser();
                List jsonParams = new ArrayList();
                jsonParams.add(new BasicNameValuePair("idAluno", params[0]));
                jsonParams.add(new BasicNameValuePair("login", params[1]));
                jsonParams.add(new BasicNameValuePair("senha", params[2]));
                JSONObject json = jsonParser.getJSONFromUrl(getResources().getString(R.string.web_service_visualizar_noticias_aluno), jsonParams);

                return json;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonResult){

            super.onPostExecute(jsonResult);
            jsonReturned = jsonResult;

            hideProgress();

            try{
                if(!jsonResult.getString("listaNoticias").equals("null")){
                    listener.onVisualizarNoticiasCompleted();
                }else{
                    Toast toast = Toast.makeText(getBaseContext(), jsonResult.getString("mensagem"), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private class AsyncTaskMostrarFoto extends AsyncTask <String, String, JSONObject>
    {

        @Override
        protected JSONObject doInBackground(String... params) {
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {

            FecharAplicativoAlert.fechar(this);

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
