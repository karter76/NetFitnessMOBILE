package br.net.netfitness.netfitness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.DicaAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class VisualizarDicasFragment extends Fragment {


    private static final String JSON_LISTA_DICAS = "JSONListaDicas";

    ArrayList<Object> mapListaDicas;
    ListView listViewDicas;
    DicaAdapter adapter;

    public static VisualizarDicasFragment newInstance(List<Object> mapListaDicas){

        VisualizarDicasFragment fragment = new VisualizarDicasFragment();
        Bundle args = new Bundle();
        args.putSerializable(JSON_LISTA_DICAS, (java.io.Serializable) mapListaDicas);
        fragment.setArguments(args);

        return fragment;
    }

    public VisualizarDicasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if(getArguments() != null) {
            mapListaDicas = (ArrayList<Object>) getArguments().getSerializable(JSON_LISTA_DICAS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_visualizar_dicas, container, false);

        listViewDicas = new ListView(getActivity().getBaseContext());
        listViewDicas = (ListView) view.findViewById(R.id.listViewDicas);

        listViewDicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                notificarClique(i);
                view.setSelected(true);
            }
        });

        adapter = new DicaAdapter(mapListaDicas);
        listViewDicas.setAdapter(adapter);

        return view;
    }

    private void notificarClique(int i) {

    }


}
