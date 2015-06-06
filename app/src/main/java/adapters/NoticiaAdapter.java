package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import br.net.netfitness.netfitness.R;

/**
 * Created by Schmitz on 30/05/2015.
 */
public class NoticiaAdapter extends BaseAdapter{

    List<Object> listaNoticias;

    public NoticiaAdapter(List<Object> listaNoticias){

        this.listaNoticias = listaNoticias;
    }

    @Override
    public int getCount() {
        return listaNoticias.size();
    }

    @Override
    public Object getItem(int i) {
        return listaNoticias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        Object noticia = new Object();

        noticia = listaNoticias.get(i);
        if(convertView == null){
            convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_noticia, null);
        }

       /* TextView txtNome = (TextView)convertView.findViewById(R.id.txtNome);

        //no caso em que o dato que queremos ler seja um objeto, o tipo deve ser HasMap<String, Object(do tipo que necessario)>
        HashMap<String, String> mapTreino = new HashMap<>();
        mapTreino = (HashMap<String, String>) treino;

        txtNome.setText( mapTreino.get("nome")+" - "+mapTreino.get("descricao"));*/
        TextView txtNome = (TextView)convertView.findViewById(R.id.txtTitulo);
        TextView txtData = (TextView)convertView.findViewById(R.id.txtData);

        HashMap<String, String> mapNoticia = new HashMap<>();
        mapNoticia = (HashMap<String, String>) noticia;

        txtNome.setText(mapNoticia.get("titulo"));
        txtData.setText(mapNoticia.get("data"));

        /*txtNome.setText("Teste");
        txtData.setText("10/05/2015");*/

        return convertView;
    }
}
