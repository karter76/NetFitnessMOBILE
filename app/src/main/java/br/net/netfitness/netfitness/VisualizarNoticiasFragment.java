package br.net.netfitness.netfitness;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.NoticiaAdapter;
import interfaces.ClicouNaNoticiaListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class VisualizarNoticiasFragment extends Fragment {


    private static final String JSON_LISTA_NOTICIAS = "JSONListaNoticias";

    ArrayList<Object> mapListaNoticias;
    ListView listViewNoticias;
    NoticiaAdapter adapter;

    public static VisualizarNoticiasFragment newInstance(List<Object> mapListaNoticias){

        VisualizarNoticiasFragment fragment = new VisualizarNoticiasFragment();
        Bundle args = new Bundle();
        args.putSerializable(JSON_LISTA_NOTICIAS, (java.io.Serializable) mapListaNoticias);
        fragment.setArguments(args);

        return fragment;
    }

    public VisualizarNoticiasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        if(getArguments() != null) {
            mapListaNoticias = (ArrayList<Object>) getArguments().getSerializable(JSON_LISTA_NOTICIAS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visualizar_noticias, container, false);

        listViewNoticias = new ListView(getActivity().getBaseContext());
        listViewNoticias = (ListView) view.findViewById(R.id.listViewNoticias);

        listViewNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                notificarClique(i);
                view.setSelected(true);
            }
        });

        adapter = new NoticiaAdapter(mapListaNoticias);
        listViewNoticias.setAdapter(adapter);

        return view;
    }

    private void notificarClique(int i){

        Activity act = getActivity();
        if(getActivity() instanceof ClicouNaNoticiaListener){
            ClicouNaNoticiaListener listener = (ClicouNaNoticiaListener)getActivity();
            listener.aoClicarNaNoticia(mapListaNoticias.get(i));
        }
    }
}
