package br.net.netfitness.netfitness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import adapters.ExercicioAdapter;


public class DetalharTreinoFragment extends Fragment
{

    private static final String JSON_TREINO = "JSONTreino";
    private Object treino;
    private TextView txtNomeTreino;
    private TextView txtDescricaoTreino;
    ListView listView;
    ExercicioAdapter adapter;

    public static DetalharTreinoFragment newInstance(Object treino)
    {
        DetalharTreinoFragment fragment = new DetalharTreinoFragment();
        Bundle args = new Bundle();
        args.putSerializable(JSON_TREINO, (java.io.Serializable) treino);
        fragment.setArguments(args);
        return fragment;
    }

    public DetalharTreinoFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            treino = getArguments().getSerializable(JSON_TREINO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detalhar_treino, container, false);
        txtNomeTreino = (TextView) view.findViewById(R.id.txtNomeTreino);
        txtDescricaoTreino = (TextView) view.findViewById(R.id.txtDescricaoTreino);

        HashMap<String, ArrayList> mapTreinoArrayList = new HashMap<>();
        HashMap<String, String> mapTreinoAtributos = new HashMap<>();

        mapTreinoArrayList = (HashMap<String, ArrayList>) treino;
        ArrayList<HashMap<String,String>> listaExercicios = new ArrayList<>();

        listaExercicios = mapTreinoArrayList.get("listaExercicios");
        mapTreinoAtributos = (HashMap<String, String>) treino;

        txtNomeTreino.setText(mapTreinoAtributos.get("nome"));
        txtDescricaoTreino.setText(mapTreinoAtributos.get("descricao"));

        listView = new ListView(getActivity().getBaseContext());
        listView = (ListView) view.findViewById(R.id.listViewExercicios);


        adapter = new ExercicioAdapter(listaExercicios);
        listView.setAdapter(adapter);


        return view;

    }


}
