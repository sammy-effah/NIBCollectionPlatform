package com.nib.gh.donations.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class DoRequestSMS {

    Logger logger = LogManager.getLogger(DoRequest.class);

    public String sendGet(String url) {
        StringBuffer response = new StringBuffer();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            // con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            logger.info("\nSending 'GET' request to URL : " + url);
            logger.info("Response Code : " + responseCode);

            BufferedReader in = null;
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (Exception ex) {
            // Logger.getLogger(DoRequest.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("sorry, something wrong!", ex);
        }

        String returnValue = response.toString();
        logger.info(returnValue);
        return returnValue;
    }


     public String sendGet(String url, JSONObject params) {
        StringBuffer response = new StringBuffer();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Length", "application/JSON");

            // con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            logger.info("\nSending 'GET' request to URL : " + url);
            logger.info("Response Code : " + responseCode);

            BufferedReader in = null;
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (Exception ex) {
            // Logger.getLogger(DoRequest.class.getName()).log(Level.SEVERE, null, ex);
            logger.error("sorry, something wrong!", ex);
        }

        String returnValue = response.toString();
        logger.info(returnValue);
        return returnValue;
    }

    public String PostURLEncode(String URL, String source) {
        StringBuffer response = new StringBuffer();

        try {
            String postData = buildFormData(source);
            logger.info("POST DATA:: " + postData.replace(source, "*****"));
            byte[] postDataBytes = postData.getBytes("UTF-8");

            URL url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data;  boundary=----MyBoundary");
            // conn.setRequestProperty("bodyValue", bookUploadBuilder.build());
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.getOutputStream().write(postDataBytes);
            conn.setConnectTimeout(30000);

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postDataBytes);
            }
            int responseCode = conn.getResponseCode();

            System.out.println("Response Code : " + responseCode);
            BufferedReader in;
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return response.toString();
    }

    private static String buildFormData(String source) {
        StringBuilder formData = new StringBuilder();
    
        formData.append("------MyBoundary\r\n");  // Replace with your chosen boundary
        formData.append("Content-Disposition: form-data; name=\"action\"\r\n\r\n");
        formData.append("upload\r\n");
    
        formData.append("------MyBoundary\r\n");  // Replace with your chosen boundary
        formData.append("Content-Disposition: form-data; name=\"source\"\r\n\r\n");
        formData.append(source + "\r\n");  // Replace with your actual source value
    
        formData.append("------MyBoundary\r\n");  // Replace with your chosen boundary
        formData.append("Content-Disposition: form-data; name=\"format\"\r\n\r\n");
        formData.append("json\r\n");
    
        formData.append("------MyBoundary\r\n");  // Replace with your chosen boundary
        formData.append("Content-Disposition: form-data; name=\"key\"\r\n\r\n");
        formData.append("6d207e02198a847aa98d0a2a901485a5\r\n");
    
        formData.append("------MyBoundary--\r\n");   // Replace with your chosen boundary (end)
    
        return formData.toString();
    }

}
