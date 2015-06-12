package adapters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

import br.net.netfitness.netfitness.R;

/**
 * Created by Daniele on 01/05/2015.
 */
public class ExercicioAdapter extends BaseAdapter {

    ArrayList<HashMap<String,String>> listaExercicios;

    public ExercicioAdapter(ArrayList<HashMap<String,String>> listaExercicios)
    {
        this.listaExercicios = listaExercicios;
    }

    @Override
    public int getCount() {
        return listaExercicios.size();
    }

    @Override
    public Object getItem(int position) {
        return listaExercicios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String, String> exercicio = new HashMap<String, String>();
        exercicio = listaExercicios.get(position);

        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_exercicio, null);
        }

        TextView txtNomeExercicio = (TextView)convertView.findViewById(R.id.txtNomeExercicio);


        txtNomeExercicio.setText(Html.fromHtml("<b>"+exercicio.get("nome")+":</b><br>"+
                                  exercicio.get("descricao")+"<br><br>"+
                                  "Ser. "+exercicio.get("series")+" - Rep. "+exercicio.get("repeticoes")));
        return convertView;
    }
}
