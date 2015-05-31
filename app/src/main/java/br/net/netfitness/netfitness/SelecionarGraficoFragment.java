package br.net.netfitness.netfitness;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

import interfaces.ClicouNoCompararGraficos;


public class SelecionarGraficoFragment extends Fragment {


    public static SelecionarGraficoFragment newInstance() {
        SelecionarGraficoFragment fragment = new SelecionarGraficoFragment();
        return fragment;
    }

    public SelecionarGraficoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_selecionar_grafico, container, false);

        final CheckBox checkBoxPeso = (CheckBox) view.findViewById(R.id.checkBoxPeso);
        final CheckBox checkBoxIMC = (CheckBox) view.findViewById(R.id.checkBoxIMC);
        final CheckBox checkBoxTorax = (CheckBox) view.findViewById(R.id.checkBoxTorax);
        final CheckBox checkBoxAbdomen = (CheckBox) view.findViewById(R.id.checkBoxAbdomen);
        final CheckBox checkBoxBraco = (CheckBox) view.findViewById(R.id.checkBoxBraco);
        final CheckBox checkBoxAntebraco = (CheckBox) view.findViewById(R.id.checkBoxAntebraco);
        final CheckBox checkBoxCoxa = (CheckBox) view.findViewById(R.id.checkBoxCoxa);
        final CheckBox checkBoxPanturrilha = (CheckBox) view.findViewById(R.id.checkBoxPanturrilha);
        Button btnCompararGraficos = (Button) view.findViewById(R.id.buttonCompararGraficos);


        btnCompararGraficos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String graficosSelecionados = "#comparar#";
                int count = 0;

                if(checkBoxPeso.isChecked())
                {
                    graficosSelecionados+="peso,";
                    count++;
                }

                if(checkBoxIMC.isChecked())
                {
                    graficosSelecionados+="imc,";
                    count++;
                }

                if(checkBoxTorax.isChecked())
                {
                    graficosSelecionados+="circTorax,";
                    count++;
                }

                if(checkBoxAbdomen.isChecked())
                {
                    graficosSelecionados+="circAbdomen,";
                    count++;
                }

                if(checkBoxBraco.isChecked())
                {
                    graficosSelecionados+="circBraco,";
                    count++;
                }

                if(checkBoxAntebraco.isChecked())
                {
                    graficosSelecionados+="circAntebraco,";
                    count++;
                }

                if(checkBoxCoxa.isChecked())
                {
                    graficosSelecionados+="circCoxa,";
                    count++;
                }

                if(checkBoxPanturrilha.isChecked())
                {
                    graficosSelecionados+="circPanturrilha";
                    count++;
                }

                if( graficosSelecionados.substring(graficosSelecionados.length() - 1).equals(",") )
                {
                    graficosSelecionados = graficosSelecionados.substring(0, graficosSelecionados.length()-1);
                }

                if (getActivity() instanceof ClicouNoCompararGraficos)
                {
                    if(count < 2)
                    {
                        Toast toast = Toast.makeText(getActivity(), R.string.no_element_comparar_grafico, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else if(count>4)
                    {
                        Toast toast = Toast.makeText(getActivity(), R.string.too_much_elements_comparar_grafico, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    {
                        ClicouNoCompararGraficos listener = (ClicouNoCompararGraficos) getActivity();
                        listener.aoCompararGraficos(graficosSelecionados);
                    }
                }

            }
        });

        return view;
    }


}
