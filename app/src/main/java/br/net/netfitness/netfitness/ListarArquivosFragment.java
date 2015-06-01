package br.net.netfitness.netfitness;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import adapters.FileAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListarArquivosFragment extends Fragment {

    private List<File> fileList = new ArrayList<File>();
    private String pastaAnterior = "";
    private File root;
    private File rootLast;

    public ListarArquivosFragment() {
        // Required empty public constructor
    }


    public static ListarArquivosFragment newInstance()
    {

        ListarArquivosFragment fragment = new ListarArquivosFragment();
        //Bundle args = new Bundle();
        //args.putSerializable(JSON_TREINO, (java.io.Serializable) treino);
        //fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = new File(Environment
                .getExternalStorageDirectory()
                .getAbsolutePath());
        rootLast = root.getAbsoluteFile().getParentFile();
        listarPasta(root);

    }


    void listarPasta(File f) {
        File[] arquivos = f.listFiles();
        fileList.clear();
        for (File arquivo : arquivos) {
            fileList.add(arquivo);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_arquivos, container, false);

        ListView lv = (ListView) view.findViewById(R.id.lista_arquivos);
        //final ArrayAdapter<String> directoryList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fileList);
        final FileAdapter fileAdapter = new FileAdapter((ArrayList<File>) fileList);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selected = fileList.get(position);
                if(selected.isDirectory()){
                    pastaAnterior = rootLast+"/"+selected.getAbsoluteFile().getParentFile().getName();
                    listarPasta(selected);
                   // ArrayAdapter<String> directoryList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, fileList);
                    fileAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(),
                            selected.toString() + " selected",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        Button btnBack = (Button) view.findViewById(R.id.buttonFileBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarPasta(rootLast);
                fileAdapter.notifyDataSetChanged();
            }
        });

        lv.setAdapter(fileAdapter);

        return view;
    }



}
