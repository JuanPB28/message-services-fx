package com.juanpascual.messagesfx.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestUtils {
    private static String token = null;
    public static void setToken(String token) {
        RequestUtils.token = token;
    }
    public static void removeToken() {
        RequestUtils.token = null;
    }
    public static String getResponse(String uri, String json, String method){
        StringBuilder response = new StringBuilder();
        try {
            HttpURLConnection connection = getHttpURLConnection(uri, json, method);

            // Obtener la respuesta de la API
            int responseCode = connection.getResponseCode();
            System.out.println("Código de respuesta: " + responseCode);

            // Leer la respuesta de la API
            if (responseCode >= 200 && responseCode < 300) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
            } else {
                // Si el código de respuesta indica un error, leer desde el ErrorStream
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        response.append(errorLine);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return response.toString();
    }

    private static HttpURLConnection getHttpURLConnection(String uri, String json, String method) throws IOException {
        URL url = new URL(uri);

        // Abrir una conexión HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Configurar la conexión para enviar una solicitud
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");

        // Añadir token
        if (token != null){
            connection.setRequestProperty("Authorization", token);
        }

        connection.setDoOutput(true);

        // Enviar la solicitud
        if(json != null) {
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(json);
                wr.flush();
            }
        }
        return connection;
    }
}