package br.net.netfitness.netfitness;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.FileAdapter;
import interfaces.ClicouNaFotoListener;


public class ListarArquivosFragment extends Fragment {

    private List<File> fileList = new ArrayList<File>();
    private File pastaAnterior;
    private File root;
    private File selected;
    private List<String> tipos = Arrays.asList("gif", "GIF", "jpg", "JPG", "png", "PNG", "jpeg", "JPEG");

    public ListarArquivosFragment() {
        // Required empty public constructor
    }


    public static ListarArquivosFragment newInstance()
    {

        ListarArquivosFragment fragment = new ListarArquivosFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = new File(Environment
                .getExternalStorageDirectory()
                .getAbsolutePath());
        pastaAnterior = root;
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
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_listar_arquivos, container, false);

        ListView listViewArquivos = (ListView) view.findViewById(R.id.lista_arquivos);
        final FileAdapter fileAdapter = new FileAdapter((ArrayList<File>) fileList);

        listViewArquivos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selected = fileList.get(position);
                if (selected.isDirectory()) {
                    pastaAnterior = new File(selected.getParent());
                    listarPasta(selected);
                    fileAdapter.notifyDataSetChanged();
                }
                else
                {
                    String ext3="", ext4 = "";
                    if(selected.getName().length()>=4){ext3 = selected.getName().substring(selected.getName().length() - 3);}
                    if(selected.getName().length()>=5){ext4 = selected.getName().substring(selected.getName().length() - 4);}

                    if(tipos.contains(ext3)||tipos.contains(ext4))
                    {
                        if (getActivity() instanceof ClicouNaFotoListener) {
                            ClicouNaFotoListener listener = (ClicouNaFotoListener) getActivity();
                            listener.aoClicarNaFoto(selected);
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.file_not_supported), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        Button btnBack = (Button) view.findViewById(R.id.buttonFileBack);

        btnBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String anterior = pastaAnterior.getName();
                if(!pastaAnterior.getName().equals(""))
                {
                    listarPasta(pastaAnterior);
                    pastaAnterior = new File(pastaAnterior.getParent());
                    fileAdapter.notifyDataSetChanged();
                }

            }
        });

        listViewArquivos.setAdapter(fileAdapter);

        return view;
    }



}
