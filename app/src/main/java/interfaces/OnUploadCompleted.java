package interfaces;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniele on 02/06/2015.
 */
public interface OnUploadCompleted {
    void onUploadCompleted(JSONObject jsonResult) throws JSONException;
}
