package interfaces;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daniele on 11/05/2015.
 */
public interface clicouNoInserirTreinoListener {
    void aoInserirOTreino(String nomeTreino, String descricaoTreino, ArrayList<HashMap<String, String>> listaDadosExercicios) throws JSONException;
}
