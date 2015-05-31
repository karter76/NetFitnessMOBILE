package br.net.netfitness.netfitness;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections4.functors.StringValueTransformer;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import interfaces.clicouNoInserirTreinoListener;


public class InserirTreinoFragment extends Fragment
{
    private static final String BUNDLE_EXERCICIOS = "exercicios";
    private static final String BUNDLE_EDT_TEXT_SERIES = "edtTextSeries";
    private static final String BUNDLE_EDT_TEXT_REPETICOES = "edtTextRepeticoes";
    private static final int ALTERAR_TREINO = 3;
    private static final String BUNDLE_ACTION = "action";

    private ArrayList<HashMap<String,String>> listaExercicios;
    private ArrayList<HashMap<String,String>> listaDadosExercicios;


    private static String paramNomeTreino = "";
    private static String paramDescricaoTreino = "";

    private TextView txtNomeTreino;
    private TextView txtDescricaoTreino;
    private EditText edtNomeTreino;
    private EditText edtDescricaoTreino;
    private Button btnInserirTreino;

    RelativeLayout viewExercicio;
    ArrayList<EditText> listaEdtTextSeries;
    ArrayList<EditText> listaEdtTextRepeticoes;
    private int mAction;


    public static InserirTreinoFragment newInstance(List<Object> listaExercicios, String nomeTreino, String descricaoTreino, int action)
    {
        InserirTreinoFragment fragment = new InserirTreinoFragment();
        Bundle args = new Bundle();

        args.putSerializable(BUNDLE_EXERCICIOS, (java.io.Serializable) listaExercicios);
        args.putInt(BUNDLE_ACTION, action);

        paramNomeTreino = nomeTreino;
        paramDescricaoTreino = descricaoTreino;

        fragment.setArguments(args);
        return fragment;
    }

    public InserirTreinoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(BUNDLE_EXERCICIOS, listaExercicios);
        outState.putSerializable(BUNDLE_EDT_TEXT_SERIES, listaEdtTextSeries);
        outState.putSerializable(BUNDLE_EDT_TEXT_REPETICOES, listaEdtTextRepeticoes);
        outState.putInt(BUNDLE_ACTION, mAction);


    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        if(((InstrutorActivity)this.getActivity()).mAction == ALTERAR_TREINO)
        {
            //btnInserirTreino.setText(R.string.btnLabelAlterarTreino);
            mAction = ALTERAR_TREINO;
        }
*/
        if (getArguments() != null)
        {
            listaExercicios = (ArrayList<HashMap<String, String>>) getArguments().getSerializable(BUNDLE_EXERCICIOS);

            if( getArguments().getInt(BUNDLE_ACTION) == ALTERAR_TREINO)
            {
                //btnInserirTreino.setText(R.string.btnLabelAlterarTreino);
                mAction = ALTERAR_TREINO;
            }

        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inserir_treino, container, false);
        txtNomeTreino = (TextView) view.findViewById(R.id.txtLabelNomeTreino);
        txtDescricaoTreino = (TextView) view.findViewById(R.id.txtLabelDescricaoTreino);

        edtNomeTreino = (EditText) view.findViewById(R.id.edtNomeTreino);
        edtDescricaoTreino = (EditText) view.findViewById(R.id.edtDescricaoTreino);

        if (!paramNomeTreino.equals(""))
        {
            edtNomeTreino.setText(paramNomeTreino);
        }
        if(!paramDescricaoTreino.equals(""))
        {
            edtDescricaoTreino.setText(paramDescricaoTreino);
        }

        btnInserirTreino = (Button) view.findViewById(R.id.btnInserirTreino);

        if(mAction == ALTERAR_TREINO)
        {
            btnInserirTreino.setText(R.string.btnLabelAlterarTreino);
        }

        boolean isSaved = false;

        btnInserirTreino.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (getActivity() instanceof clicouNoInserirTreinoListener){
                    clicouNoInserirTreinoListener listener = (clicouNoInserirTreinoListener)getActivity();
                    try {

                        listaDadosExercicios = new ArrayList<HashMap<String, String>>();
                        String series;
                        String repeticoes;
                        boolean error = false;

                        if(TextUtils.isEmpty(edtNomeTreino.getText().toString()))
                        {
                            error = true;
                            Toast toast = Toast.makeText(getActivity(), R.string.empty_nome_treino, Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        if(TextUtils.isEmpty(edtDescricaoTreino.getText().toString()))
                        {
                            error = true;
                            Toast toast = Toast.makeText(getActivity(), R.string.empty_descricao_treino, Toast.LENGTH_SHORT);
                            toast.show();
                        }



                        for(int i = 0; i<listaExercicios.size();i++)
                        {
                            HashMap<String, String> dadosExercicio = new HashMap<String, String>();
                            series = listaEdtTextSeries.get(i).getText().toString();
                            repeticoes = listaEdtTextRepeticoes.get(i).getText().toString();
                            if(!((series.equals("0") && repeticoes.equals("0")) || (series.equals("") && repeticoes.equals(""))))
                            {
                                if( (series.equals("0") && !repeticoes.equals("0")) || (repeticoes.equals("") && !series.equals("")) ) {
                                    error = true;
                                    Toast toast = Toast.makeText(getActivity(), R.string.incomplete_exercicio, Toast.LENGTH_SHORT);
                                    toast.show();
                                    break;
                                }
                                else
                                {
                                    dadosExercicio.put("idExercicio", listaExercicios.get(i).get("idExercicio"));
                                    dadosExercicio.put("series", listaEdtTextSeries.get(i).getText().toString());
                                    dadosExercicio.put("repeticoes", listaEdtTextRepeticoes.get(i).getText().toString());
                                    listaDadosExercicios.add(dadosExercicio);

                                }
                            }

                        }


                        if(listaDadosExercicios.size()==0)
                        {
                            Toast toast = Toast.makeText(getActivity(), R.string.empty_lista_exercicios, Toast.LENGTH_LONG);
                            toast.show();
                        }
                        else
                        {
                            if(!error)
                            {
                                listener.aoInserirOTreino(edtNomeTreino.getText().toString(), edtDescricaoTreino.getText().toString(), listaDadosExercicios);
                            }
                        }


                    } catch (JSONException e) {
                        Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            }
        });


        if(savedInstanceState != null)
        {
            listaEdtTextSeries = (ArrayList<EditText>) savedInstanceState.getSerializable(BUNDLE_EDT_TEXT_SERIES);
            listaEdtTextRepeticoes = (ArrayList<EditText>) savedInstanceState.getSerializable(BUNDLE_EDT_TEXT_REPETICOES);
            //Toast toast = Toast.makeText(this.getActivity(),listaEdtTextSeries.get(0).getText().toString(), Toast.LENGTH_SHORT);
            //toast.show();
            isSaved = true;

        }
        else
        {
            listaEdtTextSeries = new ArrayList<>();
            listaEdtTextRepeticoes = new ArrayList<>();
            isSaved = false;
        }



        LinearLayout scroll = (LinearLayout) view.findViewById(R.id.ScrollViewExerciciosInserirTreino);




        for(int i = 0; i < listaExercicios.size(); i++)
        {
            int marginTop = 0;
            if(i==0) marginTop = 30;

            HashMap<String, String> exercicio = new HashMap<>();

            exercicio = (HashMap<String, String>)listaExercicios.get(i);

            viewExercicio = new RelativeLayout(this.getActivity());

            LinearLayout.LayoutParams itemExercicioLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemExercicioLayoutParams.setMargins(20,marginTop,20,30);
            viewExercicio.setPadding(10,10,10,20);
            viewExercicio.setLayoutParams(itemExercicioLayoutParams);
            viewExercicio.setBackgroundColor(getResources().getColor(R.color.background_color_exercicio));


            TextView txtViewNomeExercicio = new TextView(this.getActivity());
            txtViewNomeExercicio.setId(R.id.layout1);
            LinearLayout.LayoutParams txtViewNomeExercicioLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            txtViewNomeExercicio.setLayoutParams(txtViewNomeExercicioLayoutParams);
            txtViewNomeExercicio.setText(getResources().getString(R.string.txtLabelNomeExercicio) + " " + exercicio.get("nome"));


            TextView txtViewSeries = new TextView(this.getActivity());
            txtViewSeries.setId(R.id.layout2);
            RelativeLayout.LayoutParams txtViewSeriesLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtViewSeriesLayoutParams.addRule(RelativeLayout.BELOW, txtViewNomeExercicio.getId());
            txtViewSeries.setLayoutParams(txtViewSeriesLayoutParams);
            txtViewSeries.setPadding(0,10,0,0);
            txtViewSeries.setText(R.string.labelTxtSeries);

            EditText edtTextSeries = new EditText(this.getActivity());
            edtTextSeries.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtTextSeries.setTextSize(12);
            RelativeLayout.LayoutParams edtTextSeriesLayoutParams = new RelativeLayout.LayoutParams(50, RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtTextSeriesLayoutParams.addRule(RelativeLayout.BELOW, txtViewNomeExercicio.getId());
            edtTextSeriesLayoutParams.addRule(RelativeLayout.RIGHT_OF, txtViewSeries.getId());
            edtTextSeries.setLayoutParams(edtTextSeriesLayoutParams);
            try
            {
                edtTextSeries.setText(exercicio.get("series"));
            }
            catch (Exception e)
            {
                edtTextSeries.setText("");
            }


            TextView txtViewRepeticoes = new TextView(this.getActivity());
            txtViewRepeticoes.setId(R.id.layout3);
            RelativeLayout.LayoutParams txtViewRepeticoesLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtViewRepeticoesLayoutParams.addRule(RelativeLayout.BELOW, txtViewNomeExercicio.getId());
            txtViewRepeticoes.setPadding(0,50,0,0);
            txtViewRepeticoes.setLayoutParams(txtViewRepeticoesLayoutParams);
            txtViewRepeticoes.setText(R.string.txtLabelRepeticoes);


            EditText edtTextRepeticoes = new EditText(this.getActivity());
            edtTextRepeticoes.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtTextRepeticoes.setTextSize(12);
            edtTextRepeticoes.setPadding(0, 60, 0, 0);
            RelativeLayout.LayoutParams edtTextRepeticoesLayoutParams = new RelativeLayout.LayoutParams(50, RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtTextRepeticoesLayoutParams.addRule(RelativeLayout.RIGHT_OF, txtViewRepeticoes.getId());
            edtTextRepeticoesLayoutParams.addRule(RelativeLayout.BELOW, txtViewNomeExercicio.getId());
            edtTextRepeticoes.setLayoutParams(edtTextRepeticoesLayoutParams);
            try
            {
                edtTextRepeticoes.setText(exercicio.get("repeticoes"));
            }
            catch (Exception e)
            {
                edtTextRepeticoes.setText("");
            }


            viewExercicio.addView(txtViewNomeExercicio);
            viewExercicio.addView(txtViewSeries);
            viewExercicio.addView(txtViewRepeticoes);

            if(isSaved)
            {
                edtTextSeries.setText(listaEdtTextSeries.get(i).getText());
                edtTextRepeticoes.setText(listaEdtTextRepeticoes.get(i).getText());
                listaEdtTextSeries.set(i, edtTextSeries);
                listaEdtTextRepeticoes.set(i, edtTextRepeticoes);
            }
            else
            {
                listaEdtTextSeries.add(edtTextSeries);
                listaEdtTextRepeticoes.add(edtTextRepeticoes);
            }

            viewExercicio.addView(edtTextSeries);
            viewExercicio.addView(edtTextRepeticoes);

            scroll.addView(viewExercicio);

        }


        return view;
    }


}