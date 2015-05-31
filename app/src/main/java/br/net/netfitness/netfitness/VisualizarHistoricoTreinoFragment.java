package br.net.netfitness.netfitness;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import utils.Data;
import utils.JSONConvert;


public class VisualizarHistoricoTreinoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String HISTORICO_TREINO = "historicoTreino";

    // TODO: Rename and change types of parameters
    private HashMap<String,Object> mapHistoricoTreino;


    public static VisualizarHistoricoTreinoFragment newInstance(JSONObject historicoTreino) throws JSONException
    {
        VisualizarHistoricoTreinoFragment fragment = new VisualizarHistoricoTreinoFragment();
        HashMap<String,Object> mapHistoricoTreino = (HashMap<String, Object>) JSONConvert.jsonToMap(historicoTreino);

        Bundle args = new Bundle();
        args.putSerializable(HISTORICO_TREINO, mapHistoricoTreino);

        fragment.setArguments(args);
        return fragment;
    }

    public VisualizarHistoricoTreinoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mapHistoricoTreino = (HashMap<String, Object>) getArguments().getSerializable(HISTORICO_TREINO);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_visualizar_historico_treino, container, false);

        ArrayList<String> listaDatasTreinosRealizados = new ArrayList<>();
        listaDatasTreinosRealizados = (ArrayList<String>) mapHistoricoTreino.get("datasTreinosRealizados");



        TextView txtNomeTreino = (TextView)view.findViewById(R.id.textViewNomeTreino);
            TextView txtDataVinculoTreino = (TextView)view.findViewById(R.id.textViewDataVinculoTreino);
            TextView qtdTreinos = (TextView)view.findViewById(R.id.textViewQtdTreinos);
            TextView txtTreinosFinalizados = (TextView)view.findViewById(R.id.textViewTreinosFinalizados);
            Button btnDatasTreinosRealizados = (Button) view.findViewById(R.id.buttonMostrarDatasTreinosRealizados);
            txtDataVinculoTreino.setText(Data.inverterData((String)mapHistoricoTreino.get("dataVinculoTreino")));
            txtNomeTreino.setText((String)mapHistoricoTreino.get("nomeTreino"));
            qtdTreinos.setText((String)mapHistoricoTreino.get("qtdTreinos"));
            txtTreinosFinalizados.setText(Integer.toString(listaDatasTreinosRealizados.size()));

            /*
            String string = "January 2, 2010";
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            Date date = format.parse(string);
            System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
            */

             final ArrayList<String> finalListaDatasTreinosRealizados = new ArrayList<>();
             Locale.setDefault (new Locale ("pt", "BR"));
             DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
             DateFormat newFormat = new SimpleDateFormat("d-m-yy, E");

             for (String dataString : listaDatasTreinosRealizados)
             {
                 Date data = null;
                 try
                 {
                     data = format.parse(dataString);
                 }
                 catch
                 (ParseException e)
                 {
                     Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                     toast.show();
                 }

                 finalListaDatasTreinosRealizados.add(newFormat.format(data));
             }

            btnDatasTreinosRealizados.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                View convertView = (View) inflater.inflate(R.layout.dialog_datas_treinos_realizados, null);
                alertDialog.setView(convertView);
                alertDialog.setTitle(R.string.datasTreinosRealizados);
                ListView lv = (ListView) convertView.findViewById(R.id.listViewDatasTreinosRealizados);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, finalListaDatasTreinosRealizados);
                lv.setAdapter(adapter);
                alertDialog.show();

            }
        });

        return view;
    }


}
