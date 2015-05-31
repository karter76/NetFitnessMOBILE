package br.net.netfitness.netfitness;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class CompararGraficosFragment extends Fragment {
    private static final String JSON_LISTA_EXAMES_FISICOS = "listaExamesFisicos";
    private static final String GRAFICOS_SELECIONADOS = "graficosSelecionados";

    private static String graficosSelecionados;
    private static ArrayList<Object> listaExamesFisicos;
    private String[] listaGraficos;
    //BarGraphSeries<DataPoint> serieDados;


    public static CompararGraficosFragment newInstance(ArrayList<Object> listaExamesFisicos, String graficosSelecionados) {
        CompararGraficosFragment fragment = new CompararGraficosFragment();
        Bundle args = new Bundle();
        args.putSerializable(JSON_LISTA_EXAMES_FISICOS, listaExamesFisicos);
        args.putString(GRAFICOS_SELECIONADOS, graficosSelecionados);
        fragment.setArguments(args);
        return fragment;
    }

    public CompararGraficosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listaExamesFisicos = (ArrayList<Object>) getArguments().getSerializable(JSON_LISTA_EXAMES_FISICOS);
            graficosSelecionados = getArguments().getString(GRAFICOS_SELECIONADOS);
        }
    }

    private ArrayList<Integer> hex2Rgb(String colorStr)
    {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Integer.valueOf( colorStr.substring( 1, 3 ), 16 ));
        colors.add(Integer.valueOf( colorStr.substring( 3, 5 ), 16 ));
        colors.add(Integer.valueOf( colorStr.substring( 5, 7 ), 16 ));
        return colors;
    }

    private String getLegenda(String chave)
    {
        switch (chave)
        {
            case "peso" : return "Peso";
            case "imc" : return "IMC";
            case "circTorax" : return "Torax";
            case "circAbdomen" : return "Abdomen";
            case "circBraco" : return "Braço";
            case "circAntebraco" : return "Antebraço";
            case "circCoxa" : return "Coxa";
            case "circPanturrilha" : return "Panturrilha";
        }

        return "";
    }

    private LineGraphSeries<DataPoint> visualizarGrafico(String chave,  ArrayList<HashMap<String, String>> listaMapExameFisico, String color) {
        int count;
        LineGraphSeries<DataPoint> serieDados;
        String nomeLegenda;

        if (graficosSelecionados.contains(chave))
        {


            count = 0;
            serieDados = new LineGraphSeries<>();

            for (Object exameFisico : listaExamesFisicos) {

                DataPoint dado = new DataPoint(count, Double.parseDouble(listaMapExameFisico.get(count).get(chave)));
                serieDados.appendData(dado, true, 100);
                count++;
            }
            //serieDados.setSpacing(30);
            // serieDados.isDrawDataPoints();
            ArrayList<Integer> arrayCorValores = hex2Rgb(color);

            serieDados.setColor(Color.rgb(arrayCorValores.get(0), arrayCorValores.get(1), arrayCorValores.get(2)));
            serieDados.setTitle(getLegenda(chave));
            return serieDados;
        }

        return null;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comparar_graficos, container, false);

        Date dataTreino;
        ArrayList<String> listaDatasString = new ArrayList<>();
        LineGraphSeries<DataPoint> serieDados;
        String[] arrayGraficosSelecionados;
        String[] arrayColors = {"#d3222f","#f9a33e","#ffe700","#71bf44","#007c37","#68aee0","#374ea1","#2e1950"};

        HashMap<String, String> mapExameFisico;
        ArrayList<HashMap<String, String>> listaMapExameFisico;

        SimpleDateFormat dataInicial = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat dataFinal = new SimpleDateFormat("d/m/yy");
        int count, graphWidth, graphHeight, graphLayoutHeight;
        float scale;
        double max;


        listaMapExameFisico = new ArrayList<>();

        for(Object exameFisico : listaExamesFisicos)
        {
            mapExameFisico = new HashMap<>();
            mapExameFisico = (HashMap<String, String>) exameFisico;
            listaMapExameFisico.add(mapExameFisico);

            dataTreino = new Date();
            try
            {
                dataTreino = dataInicial.parse(mapExameFisico.get("data"));
            }
            catch (ParseException e)
            {
                Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }

            listaDatasString.add(dataFinal.format(dataTreino));
        }


        scale = this.getActivity().getResources().getDisplayMetrics().density;

        graphWidth = (int) (120*listaExamesFisicos.size() * scale + 0.5f);
        graphHeight = (int) (250 * scale + 0.5f);
        graphLayoutHeight = (int) (300 * scale + 0.5f);

        GraphView grafico = (GraphView) view.findViewById(R.id.graphComparacao);
        LinearLayout layoutGrafico = (LinearLayout) view.findViewById(R.id.layoutGraphComparacao);

        grafico.setLayoutParams(new LinearLayout.LayoutParams(graphWidth, graphHeight));
        layoutGrafico.setLayoutParams(new HorizontalScrollView.LayoutParams(graphWidth, graphLayoutHeight));

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(grafico);
        staticLabelsFormatter.setHorizontalLabels(listaDatasString.toArray(new String[listaDatasString.size()]));
        grafico.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        arrayGraficosSelecionados = graficosSelecionados.split(",");

        serieDados = new LineGraphSeries<DataPoint>();

        count = 0;
        for(String nomeGrafico : arrayGraficosSelecionados)
        {
            serieDados = visualizarGrafico(nomeGrafico, listaMapExameFisico, arrayColors[count]);
            grafico.addSeries(serieDados);
            grafico.getLegendRenderer().setVisible(true);
            grafico.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
            grafico.getLegendRenderer().setWidth(150);
            grafico.getLegendRenderer().setTextSize(20);
            count++;
        }
        return view;
    }


}
