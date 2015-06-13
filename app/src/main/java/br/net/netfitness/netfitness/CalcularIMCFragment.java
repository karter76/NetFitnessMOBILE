package br.net.netfitness.netfitness;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalcularIMCFragment extends Fragment {


    private static final String ALTURA = "Altura";
    private static final String PESO = "Peso";

    private EditText mTxtAltura;
    private double mAltura;
    private EditText mTxtPeso;
    private double mPeso;
    private Button mBtnCalcular;
    private String mMsg;

    public static CalcularIMCFragment newInstance()
    {
        CalcularIMCFragment fragment = new CalcularIMCFragment();
        Bundle args = new Bundle();
        //args.putSerializable(JSON_TREINO, (java.io.Serializable) treino);
        fragment.setArguments(args);
        return fragment;
    }

    public CalcularIMCFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mAltura = getArguments().getDouble(ALTURA);
            mPeso = getArguments().getDouble(PESO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calcular_imc, container, false);

        mTxtAltura = (EditText) view.findViewById(R.id.editTextAltura);
        mTxtPeso = (EditText) view.findViewById(R.id.editTextPeso);
        mBtnCalcular = (Button) view.findViewById(R.id.btnCalcular);
        mBtnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    mAltura = Double.parseDouble(String.valueOf(mTxtAltura.getText()));
                    mPeso = Double.parseDouble(String.valueOf(mTxtPeso.getText()));

                    if (!String.valueOf(mAltura).startsWith(".") || !String.valueOf(mPeso).startsWith(".")){

                        Toast toast = Toast.makeText(getActivity(), R.string.altura_peso_invalido, Toast.LENGTH_LONG);
                        toast.show();

                    }else{
                        if (mAltura <= 0 || mPeso <=0){

                            Toast toast = Toast.makeText(getActivity(), R.string.altura_peso_zero, Toast.LENGTH_SHORT);
                            toast.show();
                        }else{
                            mMsg = calcularIMC(mAltura, mPeso);

                            Toast toast = Toast.makeText(getActivity(), mMsg, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }catch (NumberFormatException e){
                    Toast toast = Toast.makeText(getActivity(), R.string.altura_peso_invalido, Toast.LENGTH_LONG);
                    toast.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private String calcularIMC(double altura, double peso){

        double imc = peso / (altura * altura);

        if (imc < 17){
            return "IMC: " + imc + " - Muito abaixo do peso";
        }else if(imc <= 18.49){
            return "IMC: " + imc + " - Abaixo do peso";
        }else if(imc <= 24.99){
            return "IMC: " + imc + " - Peso normal";
        }else if(imc <= 29.99){
            return "IMC: " + imc + " - Obesidade I";
        }else if(imc <= 39.99){
            return "IMC: " + imc + " - Obesidade II(severa)";
        }else{
            return "IMC: " + imc + " - Obesidade III(mórbida)";
        }
    }
}
