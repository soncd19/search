package com.home.search.json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by soncd on 20/12/2018
 */
public class JSONValidate {

    public static JSONObject validate(String data, String... params) throws Exception {
        try {
            JSONObject jsonObject = new JSONObject(data);
            String[] fields = params;
            int fieldsLength = params.length;

            for (int i = 0; i < fieldsLength; i++) {
                String field = fields[i];
               if (!jsonObject.has(field)) {
                   throw new Exception("do not see field : {\"" + field + "\"} in param");
               }
            }

            return jsonObject;
        } catch (JSONException var8) {
            throw new Exception("format of params is not json");
        } catch (Exception var9) {
            throw new Exception(var9);
        }
    }

    public static JSONObject validate(JSONObject jsonObject, String... params) throws Exception {
        try {
            String[] fields = params;
            int fieldsLength = params.length;

            for (int i = 0; i < fieldsLength; i++) {
                String field = fields[i];
                if (!jsonObject.has(field)) {
                    throw new Exception("do not see field : {\"" + field + "\"} in param");
                }
            }

            return jsonObject;
        } catch (JSONException var7) {
            throw new Exception("format of params is not json");
        } catch (Exception var8) {
            throw new Exception(var8);
        }
    }
}
