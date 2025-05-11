<!DOCTYPE html> 
<html lang="es"> 
    <head> 
        <meta charset="UTF-8"> 
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <title>Mapa de Estaciones Valenbisi JDG</title> 
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" /> 
        <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script> 
        <style> 
            #map { height: 600px; width: 100%; margin-top: 20px; } 
            body { margin: 0; font-family: Arial, sans-serif; text-align: center; background-color: #f9f9f9; } 
            h1 { color: green; font-size: 24px; margin-top: 20px; } 
        </style> 
    </head> 
    <body> 
        <h1>Mapeo de Bicicletas en Valencia</h1> 
        <div id="map"></div> 
        <div style="border: 2px green solid; margin-left: 40%; margin-right:40%; margin-top: 50px; padding: 10px">
            <div>Bicis disponibles mínimas:</div>
            <input type="number" id="filtro" value=0>
            <button onclick="getData()">Filtrar</button>
        </div>
        <script> 
        
            // Inicializa el mapa centrado en Valencia 
            var map = L.map('map').setView([39.47, -0.37], 13); 
            
            // Añadir capa base de OpenStreetMap 
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { 
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors' 
            }).addTo(map); 

            // Función para definir el color del marcador según las bicicletas disponibles 
            function getMarkerColor(available) { 
                if (available < 5) { 
                    return 'red'; 
                } else if (available >= 5 && available < 10) { 
                    return 'orange'; 
                } else if (available >= 10 && available < 20) {
                    return 'yellow'; 
                } else { 
                    return 'green'; 
                } } 

            // Cargar el archivo data.json 
            function getData(){
                map.eachLayer(function (layer) {
                    if (layer instanceof L.CircleMarker) {
                        map.removeLayer(layer);
                    }
                });
                fetch('data.json') 
                .then(response => { 
                    if (!response.ok) {
                        throw new Error(`Error al cargar data.json: ${response.statusText}`); 
                        } return response.json(); 
                    }) 
                .then(data => { 
                    // Iterar sobre las estaciones y agregar marcadores al mapa 
                    Object.values(data).forEach(station => { 
                        const { lat, lon, address, available, free, total } = station; 
                        let filtroDisponibles = document.getElementById("filtro").value;
                        console.log(filtroDisponibles);
                        if (lat && lon && filtroDisponibles <= available) { 

                            // Crear un círculo con un color dependiendo de las bicicletas disponibles 
                            L.circleMarker([lat, lon], {
                                color: getMarkerColor(available), 
                                radius: 8, 
                                fillOpacity: 0.8 
                            }) 
                            .addTo(map) 
                            .bindPopup(` 
                            <strong>${address}</strong><br> 
                            <b>Disponibles:</b> ${available}<br> 
                            <b>Libres:</b> ${free}<br> 
                            <b>Total:</b> ${total} 
                            `); 
                        } 
                    }); 
                }) 
                .catch(error => { 
                    console.error('Error cargando los datos:', error); 
                }); 
            }
            getData();
        </script> 
    </body> 
</html> 