package adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.net.netfitness.netfitness.R;
import utils.JSONConvert;

/**
 * Created by Daniele on 27/04/2015.
 */
public class TreinoAdapter extends BaseAdapter
{
    List<Object> listaTreinos;

    public TreinoAdapter(List<Object> listaTreinos) {
        this.listaTreinos = listaTreinos;
    }

    @Override
    public int getCount() {
        return listaTreinos.size();
    }

    @Override
    public Object getItem(int i) {
        return listaTreinos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Object treino = new Object();
        treino = listaTreinos.get(i);

        if (convertView == null){
            convertView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_treino, null);
        }
        TextView txtNome = (TextView)convertView.findViewById(R.id.txtNome);

        //no caso em que o dato que queremos ler seja um objeto, o tipo deve ser HasMap<String, Object(do tipo que necessario)>
        HashMap<String, String> mapTreino = new HashMap<>();
        mapTreino = (HashMap<String, String>) treino;

        txtNome.setText( mapTreino.get("nome")+" - "+mapTreino.get("descricao"));
        return convertView;
    }
}
