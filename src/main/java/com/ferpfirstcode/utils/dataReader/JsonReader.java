package com.ferpfirstcode.utils.dataReader;

import com.ferpfirstcode.utils.logs.LogsManager;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonReader {
    String jsonreader;
    String jsonfile;
    private final String TEST_DATA_PATh = "src/test/resources/test-data/";

    public JsonReader(String jsonfile) {
        this.jsonfile = jsonfile;
        try {
            JSONObject data = (JSONObject) new JSONParser().parse(new FileReader(TEST_DATA_PATh + jsonfile + ".json"));
            jsonreader = data.toJSONString();

        } catch (Exception e) {
            LogsManager.error("Error reading json file: ", jsonfile, e.getMessage());


        }

    }

    public String getJsonreader(String jsonpath) {
        try {
            return JsonPath.read(jsonreader, jsonpath);

        } catch (Exception e) {
            LogsManager.error("Error reading json file: ", jsonfile, e.getMessage());
            return "";
        }
    }
}
