/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package es.gva.edu.iesjuandegaray.bicis;
import org.apache.http.HttpEntity; 
import org.apache.http.HttpResponse; 
import org.apache.http.client.methods.HttpGet; 
import org.apache.http.impl.client.CloseableHttpClient; 
import org.apache.http.impl.client.HttpClients; 
import org.apache.http.util.EntityUtils; 
import org.json.JSONArray; 
import org.json.JSONObject; 
import java.io.IOException; 

/**
 *
 * @author Sanfe
 */
public class ValenbiciAPI {

     private static final String API_URL = "https://valencia.opendatasoft.com/api/explore/v2.1/catalog/datasets/valenbisi-disponibilitat-valenbisi-dsiponibilidad/records?limit=20";

    public static void main(String[] args) {
        if (API_URL.isEmpty()) {
            System.err.println("La URL de la API no está especificada.");
            return;
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                System.out.println("Respuesta de la API:");
                System.out.println(result);
                // Intentamos procesar la respuesta como JSON
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    for(int i = 0; i <= resultsArray.length() - 1; i++){
                        JSONObject jsonElement = resultsArray.getJSONObject(i);
                        System.out.println("----------------------------------------------------");
                        System.out.println("Dirección de estación: " + jsonElement.get("address"));
                        System.out.println("Bicis disponibles: " + jsonElement.get("available"));
                        System.out.println("Espacios libres: " + jsonElement.get("free"));
                    }
                } catch (
                        org.json.JSONException e) {
                    // Si la respuesta no es un array JSON, imprimimos el mensaje de error
                    System.err.println("Error al procesar los datos JSON: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
