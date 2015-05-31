package utils;

import java.util.ArrayList;

/**
 * Created by Daniele on 28/05/2015.
 */
public class Data
{
    public static String inverterData(String data)
    {
        String[] arrayData = data.split("-");
        return arrayData[2]+"-"+arrayData[1]+"-"+arrayData[0];
    }
}
