package es.gva.edu.iesjuandegaray.bicis.valenbiciapiv2;

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
 * * * @author Eva G.
 */
public class DatosJSon {

    private static String API_URL;
    private String datos = ""; //para mostrar en el jTextArea los datos de las estaciones

    private String[] values; //para añadir los datos de las estaciones Valenbici a la BDD
    private int numEst;

    public DatosJSon(int nE) {
        numEst = nE;
        datos = "";
        API_URL = "https://valencia.opendatasoft.com/api/explore/v2.1/catalog/datasets/valenbisi-disponibilitat-valenbisi-dsiponibilidad/records?f=json&location=39.46447,-0.39308&distance=10&limit=" + nE;

        values = new String[numEst];

        for (int i = 0; i < numEst; i++) {
            values[i] = "";
        }

    }

    public void mostrarDatos(int nE) {

        numEst = nE;
        datos = "";
        API_URL = "https://valencia.opendatasoft.com/api/explore/v2.1/catalog/datasets/valenbisi-disponibilitat-valenbisi-dsiponibilidad/records?f=json&location=39.46447,-0.39308&distance=10&limit=" + nE;

        values = new String[numEst];
        for (int i = 0; i < numEst; i++) {
            values[i] = "";
        }

        double lon, lat;

        if (API_URL.isEmpty()) {
            //System.err.println("La URL de la API no está especificada.");
            setDatos(getDatos().concat("La URL de la API no está especificada."));
            return;
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);

                // Intentamos procesar la respuesta como JSON
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    
                    for(int i = 0; i < nE; i++){
                        JSONObject jsonElement = resultsArray.getJSONObject(i); 
                        String estacionId = jsonElement.get("number").toString();
                        String direccion = jsonElement.get("address").toString();
                        String bicisDisponibles = jsonElement.get("available").toString();
                        String anclajesLibres = jsonElement.get("free").toString();
                        String estadoOperativo = jsonElement.get("open").toString();
                        JSONObject geo = jsonElement.getJSONObject("geo_point_2d");
                        lon = geo.getDouble("lon");
                        lat = geo.getDouble("lat");
                        
                        datos += "----------------------------------------------------\n" +
                                "ID: " + estacionId +
                                " | Dirección de estación: " + direccion +
                                " | Bicis disponibles: " + bicisDisponibles + 
                                " | Espacios libres: " + anclajesLibres + 
                                " | Operativo: " + estadoOperativo + 
                                " | Coordenadas: " + lat + "," + lon + "\n";
                        
                        values[i] = estacionId + "%" + direccion + "%" + bicisDisponibles + 
                                "%" + anclajesLibres + "%" + estadoOperativo + "%" + lon + "%" + lat;
                    }
                    
                    // Añade aquí el Código para recorrer el vector de objetos JSON, con los datos de las estaciones y preparar el vector de
                    // valores (atributo values de esta clase)
                } catch (org.json.JSONException e) {
                    // Si la respuesta no es un array JSON, imprimimos el mensaje de error
                    setDatos(getDatos().concat("Error al procesar los datos JSON: " + e.getMessage()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * @return the datos
     */
    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    /**
     * * @return the values
     */
    public String[] getValues() {
        return values;
    }

    /**
     * * @param values the values to set
     */
    public void setValues(String[] values) {
        this.values = values;
    }

    /**
     * * @return the numEst
     */
    public int getNumEst() {
        return numEst;
    }

    /**
     * * @param numEst the numEst to set
     */
    public void setNumEst(int numEst) {
        this.numEst = numEst;
    }
}
