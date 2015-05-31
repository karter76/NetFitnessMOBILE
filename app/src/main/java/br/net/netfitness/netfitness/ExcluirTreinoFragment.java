package br.net.netfitness.netfitness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import adapters.TreinoAdapter;
import interfaces.clicouNoExcluirTreinoListener;


public class ExcluirTreinoFragment extends Fragment {

    private static final String JSON_LISTA_TREINOS = "JSONListaTreinos";
    ArrayList<Object> mapListaTreinos;
    ListView listView;
    TreinoAdapter adapter;
    Button btnExcluir;
    int mSelectedItem;


    public static ExcluirTreinoFragment newInstance(List<Object> mapListaTreinos) {
        ExcluirTreinoFragment fragment = new ExcluirTreinoFragment();
        Bundle args = new Bundle();
        args.putSerializable(JSON_LISTA_TREINOS, (java.io.Serializable) mapListaTreinos);
        fragment.setArguments(args);
        return fragment;
    }

    public ExcluirTreinoFragment() {
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
        mSelectedItem=-1;
        View view = inflater.inflate(R.layout.fragment_excluir_treino, container, false);
        listView = new ListView(getActivity().getBaseContext());
        listView = (ListView) view.findViewById(R.id.lista_treinos);

        btnExcluir = (Button)view.findViewById(R.id.buttonExcluir);

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedItem==-1)
                {
                    Toast toast = Toast.makeText(getActivity(),getResources().getText(R.string.treino_not_selected), Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    notificarClique(mSelectedItem);
                }

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mSelectedItem = position;
                view.setSelected(true);

            }
        });

        adapter = new TreinoAdapter(mapListaTreinos);
        listView.setAdapter(adapter);

        return view;
    }



    private void notificarClique(int i) {

        if (getActivity() instanceof clicouNoExcluirTreinoListener){
            clicouNoExcluirTreinoListener listener = (clicouNoExcluirTreinoListener)getActivity();
            try {
                listener.aoExcluirOTreino(mapListaTreinos.get(i));
            } catch (JSONException e) {
                Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


}
