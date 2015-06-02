package br.net.netfitness.netfitness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

import interfaces.ClicouNoMudarFoto;


public class FotoFragment extends Fragment /*implements ClicouNaFoto*/
{

    private static final String FOTO_ALUNO = "fotoAluno";
    private static final String FOTO_RETORNADA = "fotoRetornada";

    private String nomeFotoAluno;
    private File fotoRetornada;

    public static FotoFragment newInstance(String fotoAluno, File fotoRetornada) {
        FotoFragment fragment = new FotoFragment();
        Bundle args = new Bundle();
        args.putString(FOTO_ALUNO, fotoAluno);
        args.putSerializable(FOTO_RETORNADA, fotoRetornada);
        fragment.setArguments(args);
        return fragment;
    }

    public FotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nomeFotoAluno = getArguments().getString(FOTO_ALUNO);
            fotoRetornada = (File) getArguments().getSerializable(FOTO_RETORNADA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foto, container, false);

        ImageView fotoAluno = (ImageView) view.findViewById(R.id.fotoAluno);
        Button btnMudarFoto = (Button) view.findViewById(R.id.buttonMudarFoto);
        Button btnConfirmarFoto = (Button) view.findViewById(R.id.buttonConfirmarFoto);

        String endereco_foto = "";

        if(fotoRetornada==null) {
            endereco_foto = getResources().getString(R.string.endereco_fotos_alunos) + nomeFotoAluno;
            Picasso.with(getActivity()).load(endereco_foto).fit().centerCrop().into(fotoAluno);
        }
        else
        {
            btnConfirmarFoto.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(fotoRetornada).fit().centerCrop().into(fotoAluno);
        }



        btnMudarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof ClicouNoMudarFoto)
                {
                    ClicouNoMudarFoto listener = (ClicouNoMudarFoto) getActivity();
                    listener.aoClicarNoMudarFoto(fotoRetornada);
                }
            }
        });

        return view;
    }

/*
    @Override
    public void aoClicarNaFoto(File arquivo) {
        Toast toast = Toast.makeText(getActivity(), arquivo.getName(), Toast.LENGTH_SHORT);
        toast.show();
    }
    */
}
