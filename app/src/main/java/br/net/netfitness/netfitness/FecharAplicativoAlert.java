package br.net.netfitness.netfitness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by Daniele on 02/06/2015.
 */
public class FecharAplicativoAlert
{
    public static void fechar(final Activity act)
    {
        //Ask the user if they want to quit
         new AlertDialog.Builder(act)
        .setIcon(android.R.drawable.ic_dialog_info)
        .setTitle(R.string.fechar_titulo_dialog)
        .setMessage(R.string.mensagem_fechar_dialog)
        .setPositiveButton(R.string.sim_ao_fechar, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Stop the activity
                act.finish();
            }

        })
        .setNegativeButton(R.string.nao_ao_fechar, null)
        .show();
    }

}
