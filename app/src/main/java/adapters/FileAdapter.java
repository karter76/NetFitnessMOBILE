package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.net.netfitness.netfitness.FotoFragment;
import br.net.netfitness.netfitness.R;

/**
 * Created by Daniele on 01/06/2015.
 */
public class FileAdapter extends BaseAdapter
{
    ArrayList<File> listaArquivos;
    List<String> tipos = Arrays.asList("gif", "GIF", "jpg", "JPG", "png", "PNG", "jpeg", "JPEG");

    public FileAdapter(ArrayList<File> listaArquivos)
    {
        this.listaArquivos = listaArquivos;
    }

    @Override
    public int getCount() {
        return listaArquivos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaArquivos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        File arquivo = listaArquivos.get(position);
        String ext3="", ext4 = "";
        if(arquivo.getName().length()>=4){ext3 = arquivo.getName().substring(arquivo.getName().length() - 3);}
        if(arquivo.getName().length()>=5){ext4 = arquivo.getName().substring(arquivo.getName().length() - 4);}

        if (convertView == null){
            convertView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_arquivo, null);
        }

        ImageView icone = (ImageView) convertView.findViewById(R.id.iconeArquivo);

        if(arquivo.isDirectory())
        {
            icone.setImageDrawable(convertView.getResources().getDrawable(R.drawable.folder));
        }

        if(arquivo.isFile())
        {
            if(tipos.contains(ext3)||tipos.contains(ext4))
            {
                Picasso.with(viewGroup.getContext()).load(arquivo).fit().centerCrop().into(icone);
            }
            else {
                icone.setImageDrawable(convertView.getResources().getDrawable(R.drawable.file));
            }
        }
        TextView txtNomeArquivo = (TextView)convertView.findViewById(R.id.txtNomeArquivo);

        txtNomeArquivo.setText(arquivo.getName());

        return convertView;
    }
}
