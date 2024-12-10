package com.isstracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIClient {
    public static final String apiURL = "http://api.open-notify.org/iss-now.json";

    public static String getAPIResponse(String apiURL) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = null;

        try {
            final URL url = new URL(apiURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Use try-with-resources to ensure BufferReader is closed //
            try (BufferedReader inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;

                while ((inputLine = inputStream.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response.toString();
    }
}
