package utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Daniele on 28/05/2015.
 */
public class Data {
    private static String characters = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String inverterData(String data) {
        String[] arrayData = data.split("-");
        return arrayData[2] + "-" + arrayData[1] + "-" + arrayData[0];
    }

    public static String generateString(int length) {
        Random rng = new Random();
        rng.setSeed(System.currentTimeMillis());
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    public static String[] removeEmptyValues(String split[])
    {
        ArrayList<String> arrayReturn = new ArrayList<>();

        for(int i = 0; i<split.length; i++)
        {
            if(!split[i].equals(""))
            {
                arrayReturn.add(split[i]);
            }
        }
        return arrayReturn.toArray(new String[arrayReturn.size()]);
    }

    public static String joinString(String split[], String glue)
    {
        StringBuilder sb = new StringBuilder();
        String stReturn;

        for(int i = 0; i<split.length; i++)
        {
            if(i!= split.length-1) {
                sb.append(split[i] + glue);
            }
            else {
                sb.append(split[i]);
            }
            if (i != split.length - 1)
            {
                sb.append(" ");
            }
        }
        stReturn = sb.toString();
        stReturn.replace("/ ","/");
        return sb.toString();
    }


}

