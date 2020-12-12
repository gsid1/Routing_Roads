window.onload = init;
let location_map = new Map();
var properties = {
    GET_LOCATIONS: "http://localhost:8080/api/location",
    SHORTEST_PATH_DIJKSTRAS: "http://localhost:8080/api/algorithm/Dijkstras",
    SHORTEST_PATH_ASTAR: "http://localhost:8080/api/algorithm/AStar"
};
var mymap;
function init() {


    get_locations = properties['GET_LOCATIONS']
    console.log("NG : ", get_locations)
    console.log('111')
    mymap = L.map('mapid').setView([ 29.864130, 77.893894 ], 16);
    var mytile
    try {
        mytile = L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
            maxZoom: 23,
            attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
                '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
                'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
            id: 'mapbox/streets-v11',
            tileSize: 512,
            zoomOffset: -1,
            errorTileUrl: ''
        });

    } catch (err) {
        console.log('tiles not found');
    }

    console.log('111')
    cnt = 0;
    mytile.addTo(mymap);
    // let location_map = new Map();
    fetch(get_locations).then(response => {
        return response.json();
    }).then(locations => {
        console.log("NG: ", locations);

        locations.forEach(location => {
            if (location != null) {
                cnt = cnt+1;
                $("#source").append("<option value=\""+ cnt + "\">"+location.name+"</option>" );
                $("#destination").append("<option value=\""+ cnt + "\">"+location.name+"</option>" );
                location_map.set(location.name, [location.geoLocation.latitude, location.geoLocation.longitude]);
                var marker = L.marker(location_map.get(location.name)).addTo(mymap);
                marker.bindPopup(location.name);
            }
        });


    })
    console.log('222')
}
var src = 0;
var dst = 0;
function sourceChange() {
    var src_option = document.getElementById("source");
    var source_location = src_option.options[src_option.selectedIndex].text;;
    if (location_map.get(source_location) != undefined) {
        src = 1;

        // document.getElementById("sourceError").innerHTML = "Valid";
        // document.getElementById("sourceError")["color"] = "green";
        if (Boolean(src) && Boolean(dst)) {
            shortestPath();
        }
    } else {
        // document.getElementById("source").innerHTML = "Invalid source";
        // document.getElementById("sourceError").style("color","red");
        src = 0;
        // document.getElementById("sourceError").innerHTML = "Invalid source";
        // document.getElementById("sourceError")["color"] = "red";
    }
}


function destinationChange() {
    var dest_option = document.getElementById("destination");
    var destination_location = dest_option.options[dest_option.selectedIndex].text;
    if (location_map.get(destination_location) != undefined) {
        dst = 1;

        // document.getElementById("destinationError").innerHTML = "Valid";
        // document.getElementById("destinationError")["color"] = "green";
        if (Boolean(src) && Boolean(dst)) {
            shortestPath();
        }
    } else {
        // document.getElementById("source").innerHTML = "Invalid source";
        // document.getElementById("destinationError").style("color","red");
        dst = 0;
        // document.getElementById("destinationError")["color"] = "red";
        // document.getElementById("destinationError").innerHTML = "Invalid destination";
    }
}

function clearMap() {
    for(i in mymap._layers) {
        if(mymap._layers[i]._path != undefined) {
            try {
                mymap.removeLayer(mymap._layers[i]);
            }
            catch(e) {
                console.log("problem with " + e + mymap._layers[i]);
            }
        }
    }
}

function shortestPath() {

    var lineCoordinate = [];
    var lineCoordinate2 = [];
    clearMap();
    var algo_1 = document.getElementById("algo");
    var algo = algo_1.options[algo_1.selectedIndex].text;
    var src_option = document.getElementById("source");
    var srcLocation = src_option.options[src_option.selectedIndex].text;
    var dest_option = document.getElementById("destination");
    var dstLocation = dest_option.options[dest_option.selectedIndex].text;
    dijkstras = properties['SHORTEST_PATH_DIJKSTRAS'];
    astar = properties['SHORTEST_PATH_ASTAR'];
    requestBody = {
        source: srcLocation,
        destination: dstLocation
    };
    console.log("NG : request body ", JSON.stringify(requestBody));
    if((Boolean(src) && Boolean(dst)))
    {
        var t0 = performance.now();
        if(algo == "Dijkstra")
        {
            fetch(dijkstras, {
                method: 'POST', // or 'PUT'
                headers: {
                    'Content-Type': 'text/plain',
                },
                body: JSON.stringify(requestBody),
            }).then(response => {
                return response.json();
            }).then(path => {
                console.log("NG :", path);
                if (path.length == 1) {
                    document.getElementById("sourceError").innerHTML = "No path exist between source and destination";
                    document.getElementById("sourceError")["color"] = "blue";
                }
                path.forEach(location => {
                    lineCoordinate.push(location_map.get(location.name))
                })
                console.log("NG :", lineCoordinate);
                L.polyline(lineCoordinate, { color: 'red' }).addTo(mymap);
            });
            

        }
        
        else{

            fetch(astar, {
                method: 'POST', // or 'PUT'
                headers: {
                    'Content-Type': 'text/plain',
                },
                body: JSON.stringify(requestBody),
            }).then(response => {
                return response.json();
            }).then(path => {
                console.log("NG :", path);
                if (path.length == 1) {
                    document.getElementById("sourceError").innerHTML = "No path exist between source and destination";
                    document.getElementById("sourceError")["color"] = "blue";
                }
                path.forEach(location => {
                    lineCoordinate2.push(location_map.get(location.name))
                })
                L.polyline(lineCoordinate2, { color: 'blue' }).addTo(mymap);
            });
        }
        var t1 = performance.now();
        // document.getElementById("time_taken").innerHTML = (t1-t0);
    }
    

    // lineCoordinate.push(location_map.get(srcLocation))

    // devices.forEach(device => {
    //     if(device != null){
    //         neighbors = device.neighbors;
    //         if(neighbors.length > 0){
    //             lineCoordinate.push([device.lat, device.lng]);
    //             neighbors.forEach(neighbor => {
    //                 if (devices_map.get(neighbor) != undefined){
    //                     lineCoordinate.push(devices_map.get(neighbor));
    //                     L.polyline(lineCoordinate, { color: 'red' }).addTo(mymap);
    //                 }
    //             })       
    //         }
    //     }
    // });
}