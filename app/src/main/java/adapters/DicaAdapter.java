package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import br.net.netfitness.netfitness.R;
import utils.Data;

/**
 * Created by Schmitz on 07/06/2015.
 */
public class DicaAdapter extends BaseAdapter {

    List<Object> listaDicas;

    public DicaAdapter(List<Object> listaDicas){

        this.listaDicas = listaDicas;
    }

    @Override
    public int getCount() {
        return listaDicas.size();
    }

    @Override
    public Object getItem(int i) {
        return listaDicas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        Object dica = new Object();

        dica = listaDicas.get(i);
        if(convertView == null){
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dica, null);
        }

        TextView txtNome = (TextView)convertView.findViewById(R.id.txtTitulo);
        TextView txtDesc = (TextView)convertView.findViewById(R.id.txtDesc);

        HashMap<String, String> mapDica = new HashMap<>();
        mapDica = (HashMap<String, String>) dica;

        txtNome.setText(mapDica.get("titulo"));
        txtDesc.setText(mapDica.get("descricao"));


        return convertView;
    }
}
