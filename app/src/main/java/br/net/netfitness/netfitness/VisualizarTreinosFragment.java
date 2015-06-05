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

import adapters.TreinoAdapter;
import interfaces.ClicouNoTreinoListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisualizarTreinosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisualizarTreinosFragment extends Fragment {
    private static final String JSON_LISTA_TREINOS = "JSONListaTreinos";

    ArrayList<Object> mapListaTreinos;
    ListView listView;
    TreinoAdapter adapter;

    public static VisualizarTreinosFragment newInstance(List<Object> mapListaTreinos) {
        VisualizarTreinosFragment fragment = new VisualizarTreinosFragment();
        Bundle args = new Bundle();
        args.putSerializable(JSON_LISTA_TREINOS, (java.io.Serializable) mapListaTreinos);
        fragment.setArguments(args);
        return fragment;
    }

    public VisualizarTreinosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments()!= null) {

            mapListaTreinos = (ArrayList<Object>) getArguments().getSerializable(JSON_LISTA_TREINOS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visualizar_treinos, container, false);
        listView = new ListView(getActivity().getBaseContext());
        listView = (ListView) view.findViewById(R.id.lista_treinos);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                notificarClique(position);
                view.setSelected(true);
            }
        });

        adapter = new TreinoAdapter(mapListaTreinos);
        listView.setAdapter(adapter);

        return view;
    }



    private void notificarClique(int i) {
        // Estado estado = mEstados.get(i);
        Activity act = getActivity();
        if (getActivity() instanceof ClicouNoTreinoListener){
            ClicouNoTreinoListener listener = (ClicouNoTreinoListener)getActivity();
            listener.aoClicarNoTreino(mapListaTreinos.get(i));
            // Toast toast = Toast.makeText(this.getActivity(), "cliccato", Toast.LENGTH_SHORT);
            // toast.show();
        }
    }


}
