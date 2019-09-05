package com.example.practicing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JavaPostRequest {

    private AnomalyPoint[] anomalies;

    public JavaPostRequest(AnomalyPoint[] anomalies) {
        this.anomalies = anomalies;
    }

    public static void main (String []args) throws IOException{
        //Change the URL with any other publicly accessible POST resource, which accepts JSON request body
        URL url = new URL ("/anomalies");

        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");

        con.setDoOutput(true);

        //JSON String need to be constructed for the specific resource.

//        JSONObject jsonInput = createJSON(this.anomalies);
//        String jsonInputString = "{\"name\": \"Upendra\", \"job\": \"Programmer\"}";

//        try(OutputStream os = con.getOutputStream()){
//            byte[] input = jsonInputString.getBytes("utf-8");
//            os.write(input, 0, input.length);
//        }
//
//        int code = con.getResponseCode();
//        System.out.println(code);
//
//        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
//            StringBuilder response = new StringBuilder();
//            String responseLine = null;
//            while ((responseLine = br.readLine()) != null) {
//                response.append(responseLine.trim());
//            }
//            System.out.println(response.toString());
//        }
    }

    private static JSONObject createJSON(AnomalyPoint[] anomalyInput) throws JSONException {
        JSONObject anomalyPoints = new JSONObject();
        JSONArray anomalyArray = new JSONArray();

        for (int i = 0; i < anomalyInput.length; i++) {
            JSONObject anomalyPoint = new JSONObject();
            JSONObject position = new JSONObject();

            position.put("x", anomalyInput[i].getX());
            position.put("y", anomalyInput[i].getY());

            anomalyPoint.put("position", position);
            anomalyPoint.put("severity", anomalyInput[i].getSeverity());

//            anomalyArray.add(anomalyPoint);
        }

        anomalyPoints.put("anomalyPoints", anomalyArray);
        return anomalyPoints;
    }
}