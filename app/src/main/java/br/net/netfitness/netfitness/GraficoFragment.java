package br.net.netfitness.netfitness;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class GraficoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String JSON_LISTA_EXAMES_FISICOS = "listaExamesFisicos";



    // TODO: Rename and change types of parameters
    private ArrayList<Object> listaExamesFisicos;



    // TODO: Rename and change types and number of parameters
    public static GraficoFragment newInstance(ArrayList<Object> listaExamesFisicos) {
        GraficoFragment fragment = new GraficoFragment();
        Bundle args = new Bundle();
        args.putSerializable(JSON_LISTA_EXAMES_FISICOS, listaExamesFisicos);
        fragment.setArguments(args);
        return fragment;
    }

    public GraficoFragment() {
        // Required empty public constructor
    }

    private ArrayList<Integer> hex2Rgb(String colorStr)
    {
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Integer.valueOf( colorStr.substring( 1, 3 ), 16 ));
        colors.add(Integer.valueOf( colorStr.substring( 3, 5 ), 16 ));
        colors.add(Integer.valueOf( colorStr.substring( 5, 7 ), 16 ));
        return colors;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listaExamesFisicos = (ArrayList<Object>) getArguments().getSerializable(JSON_LISTA_EXAMES_FISICOS);
        }
    }

    private void visualizarGrafico(View view, String chave, int idGrafico, int idLayoutGrafico, String corBarras, String corValores) throws ParseException {

        Date dataTreino;
        ArrayList<String> listaDatasString = new ArrayList<>();
        BarGraphSeries<DataPoint> serieDados;

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
            dataTreino = dataInicial.parse(mapExameFisico.get("data"));

            listaDatasString.add(dataFinal.format(dataTreino));
        }



        scale = this.getActivity().getResources().getDisplayMetrics().density;

        graphWidth = (int) (100*listaExamesFisicos.size() * scale + 0.5f);
        graphHeight = (int) (250 * scale + 0.5f);
        graphLayoutHeight = (int) (300 * scale + 0.5f);

        GraphView grafico = (GraphView) view.findViewById(idGrafico);
        LinearLayout layoutGrafico = (LinearLayout) view.findViewById(idLayoutGrafico);

        grafico.setLayoutParams(new LinearLayout.LayoutParams(graphWidth, graphHeight));
        layoutGrafico.setLayoutParams(new HorizontalScrollView.LayoutParams(graphWidth, graphLayoutHeight));

        serieDados = new BarGraphSeries<DataPoint>();

        count = 0;
        max = 0;
        for(Object exameFisico : listaExamesFisicos)
        {
            DataPoint dado = new DataPoint(count, Double.parseDouble(listaMapExameFisico.get(count).get(chave)));
            if(Double.parseDouble(listaMapExameFisico.get(count).get(chave))>max)
            {
                max = Double.parseDouble(listaMapExameFisico.get(count).get(chave));
            }
            serieDados.appendData(dado,true,100);
            count++;
        }

        grafico.getViewport().setYAxisBoundsManual(true);
        grafico.getViewport().setMinY(0);
        grafico.getViewport().setMaxY(max+Math.round(max/4));

        ArrayList<Integer> arrayCorBarras= hex2Rgb(corBarras);
        ArrayList<Integer> arrayCorValores = hex2Rgb(corValores);
        serieDados.setColor(Color.rgb(arrayCorBarras.get(0), arrayCorBarras.get(1), arrayCorBarras.get(2)));
        serieDados.setSpacing(30);
        serieDados.setDrawValuesOnTop(true);
        serieDados.setValuesOnTopColor(Color.rgb(arrayCorValores.get(0), arrayCorValores.get(1), arrayCorValores.get(2)));
        grafico.addSeries(serieDados);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(grafico);
        staticLabelsFormatter.setHorizontalLabels(listaDatasString.toArray(new String[listaDatasString.size()]));
        grafico.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_grafico, container, false);

        try
        {
            visualizarGrafico(view, "peso", R.id.graphPeso, R.id.layoutGraphPeso, "#93bfca", "#b72700");
            visualizarGrafico(view, "imc", R.id.graphIMC, R.id.layoutGraphIMC, "#93bfca", "#b72700");
            visualizarGrafico(view, "circTorax", R.id.graphTorax, R.id.layoutGraphTorax, "#93bfca", "#b72700");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }


}
