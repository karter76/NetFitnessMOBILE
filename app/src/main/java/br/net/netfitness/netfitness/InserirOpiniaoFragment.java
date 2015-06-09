package br.net.netfitness.netfitness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import interfaces.ClicouNoInserirOpiniaoListener;


public class InserirOpiniaoFragment extends Fragment {

    public static InserirOpiniaoFragment newInstance()
    {
        InserirOpiniaoFragment fragment = new InserirOpiniaoFragment();
        return fragment;
    }

    public InserirOpiniaoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inserir_opiniao, container, false);

        Button btnInserirOpiniao = (Button) view.findViewById(R.id.buttonInserirOpiniao);
        final TextView edtTextOpiniao = (EditText) view.findViewById(R.id.editTextOpiniao);

        btnInserirOpiniao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() instanceof ClicouNoInserirOpiniaoListener) {
                    ClicouNoInserirOpiniaoListener listener = (ClicouNoInserirOpiniaoListener) getActivity();

                    if(edtTextOpiniao.getText().toString().trim().equals(""))
                    {
                        Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.opiniao_empty), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        listener.aoClicarNoInserirOpiniao(edtTextOpiniao.getText().toString());
                    }
                }

            }
        });

        return view;
    }


}
