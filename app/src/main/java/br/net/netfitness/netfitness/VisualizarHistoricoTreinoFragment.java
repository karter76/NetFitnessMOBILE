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

import interfaces.ClicouNoAtualizarDatasTreino;
import interfaces.OnAtualizarDatasTreinoCompleted;
import utils.Data;
import utils.JSONConvert;


public class VisualizarHistoricoTreinoFragment extends Fragment {

    private static final String HISTORICO_TREINO = "historicoTreino";
    private static final String NUM_TREINOS = "numTreinos";
    private HashMap<String,Object> mapHistoricoTreino;
    private int numMaxTreinos;
    private int idTreino;
    private int numMinTreinos;
    private int numTreinos=0;

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

        if(savedInstanceState != null)
        {
            numTreinos = savedInstanceState.getInt(NUM_TREINOS);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(NUM_TREINOS, numTreinos);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.fragment_visualizar_historico_treino, container, false);

         ArrayList<String> listaDatasTreinosRealizados = new ArrayList<>();
         listaDatasTreinosRealizados = (ArrayList<String>) mapHistoricoTreino.get("datasTreinosRealizados");
         idTreino = Integer.valueOf((String)mapHistoricoTreino.get("idTreino"));



         TextView txtNomeTreino = (TextView)view.findViewById(R.id.textViewNomeTreino);
         TextView txtDataVinculoTreino = (TextView)view.findViewById(R.id.textViewDataVinculoTreino);
         TextView qtdTreinos = (TextView)view.findViewById(R.id.textViewQtdTreinos);
         final TextView txtTreinosFinalizados = (TextView)view.findViewById(R.id.textViewTreinosFinalizados);
         Button btnDatasTreinosRealizados = (Button) view.findViewById(R.id.buttonMostrarDatasTreinosRealizados);
         Button btnMaisTreinos = (Button) view.findViewById(R.id.buttonMore);
         Button btnMenosTreinos = (Button) view.findViewById(R.id.buttonLess);
         Button btnAtualizar  = (Button) view.findViewById(R.id.buttonAtualizar);

         txtDataVinculoTreino.setText(Data.inverterData((String)mapHistoricoTreino.get("dataVinculoTreino")));
         txtNomeTreino.setText((String)mapHistoricoTreino.get("nomeTreino"));
         qtdTreinos.setText((String)mapHistoricoTreino.get("qtdTreinos"));

         if(numTreinos==0) {
             txtTreinosFinalizados.setText(Integer.toString(listaDatasTreinosRealizados.size()));
             numTreinos = listaDatasTreinosRealizados.size();
         }
        else
         {
             txtTreinosFinalizados.setText(Integer.toString(numTreinos));
         }


         numMinTreinos = listaDatasTreinosRealizados.size();
         numMaxTreinos = Integer.parseInt((String) mapHistoricoTreino.get("qtdTreinos"));


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

         btnDatasTreinosRealizados.setOnClickListener(new View.OnClickListener()
         {

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

        btnMaisTreinos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(numTreinos<numMaxTreinos) {
                    numTreinos++;
                    txtTreinosFinalizados.setText(String.valueOf(numTreinos));
                }
            }
        });

        btnMenosTreinos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(numTreinos>numMinTreinos) {
                    numTreinos--;
                    txtTreinosFinalizados.setText(String.valueOf(numTreinos));
                }
            }
        });

        btnAtualizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(numTreinos != numMinTreinos) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    View convertView = (View) inflater.inflate(R.layout.dialog_confirmar_atualizar_treino, null);
                    alertDialog.setView(convertView);
                    alertDialog.setTitle(R.string.datasTreinosRealizados);
                    Button btnSim = (Button) convertView.findViewById(R.id.buttonSim);
                    Button btnNao = (Button) convertView.findViewById(R.id.buttonNao);
                    final AlertDialog alert = alertDialog.create();

                    btnSim.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getActivity()instanceof ClicouNoAtualizarDatasTreino)
                            {
                                ClicouNoAtualizarDatasTreino listener = (ClicouNoAtualizarDatasTreino) getActivity();
                                listener.aoClicarNoAtualizarDatasTreino(idTreino, numTreinos-numMinTreinos);
                                alert.cancel();
                            }
                        }
                    });

                    btnNao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.cancel();
                        }
                    });


                    alert.show();
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.treino_not_updated), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });



        return view;
    }


}
