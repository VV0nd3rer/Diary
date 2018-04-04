$(document).ready(function () {

});
function initMiniMap() {
    var x = parseFloat($('#map_coord_x').val());
    var y = parseFloat($('#map_coord_y').val());
    console.log("x: " + x + ", y: " + y);
    var center = {lat: x, lng: y };
    var mini_map = new google.maps.Map(document.getElementById('mini-map'), {
        zoom: 7,
        center: center
    });
    loadPostSight(mini_map);
}
function initMap() {

    var center = {lat: 49.817492, lng: 15.472962};

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 6,
        center: center
    });

    var infowindow = new google.maps.InfoWindow();
    var geocoder = new google.maps.Geocoder();

    var sightBaseURL = "/posts/sight/";

    loadSights(map, infowindow, sightBaseURL);


    $('#geocoding_form').submit(function (e) {
        e.preventDefault();
        geocodeAddress(geocoder, map, infowindow);
    });

    $("#modal-form-save-sight-btn").click(function (event) {
        event.preventDefault();
        saveNewSight(map, sightBaseURL);
    });
}

function loadPostSight(mini_map) {
    var marker = new google.maps.Marker({
        position: mini_map.center,
        map: mini_map
    });
}

function loadSights(map, infowindow, sightBaseURL) {
    $.get("get-sights", function (data) {
            for (var i in data) {
                var sight_id = data[i].sightId;
                var marker = new google.maps.Marker({
                    position: {lat: data[i].mapCoordX, lng: data[i].mapCoordY},
                    map: map,
                    title: data[i].label,
                    description: data[i].description,
                    id: data[i].sightId
                });
                marker.addListener('click', function () {
                    infowindow.setContent('<p>' + this.title + '</p><p>' + this.description + '</p><a href=' + sightBaseURL + this.id + '>See more</a> or <button onclick="updSight(' + this.id + ')" type="submit"/>Update sight</button>');
                    infowindow.open(map, this);
                });
            }
        })
        .done(function () {
            console.log("done");
        })
        .fail(function (e) {
            console.log("error" + e);
        })
}
function geocodeAddress(geocoder, map, infowindow) {

    var address = $('#address').val().trim();
    geocoder.geocode({'address': address}, function (results, status) {
        var isSightPresent = false;
        if (status === 'OK') {
            var latlng = results[0].geometry.location;
            map.setCenter(latlng);
            $.get("is-coord-stored", {x: latlng.lat(), y: latlng.lng()}, function (res) {
                if (res == true) {
                    isSightPresent = true;
                }
            });
            if(isSightPresent) {
                return;
            }
            var newMarker = new google.maps.Marker({
                map: map,
                position: {lat: latlng.lat(), lng: latlng.lng()},
                title: address
            });
            console.log('new marker title: ' + newMarker.title + ', new marker, address: ' + address);

            newMarker.addListener('click', function () {
                prepareNewSightModalForm(results);
                $("#modal-form").modal();
            });
        } else {
            alert('Geocode was not successful for the following reason: ' + status);
        }

    });
}
function prepareNewSightModalForm(results) {
    var latlng = results[0].geometry.location;
    var country_code;
    var country_name;
    var long_name;
    console.log("results[0]: " + JSON.stringify(results[0]));
    //find country name
    for (var i = 0; i < results[0].address_components.length; i++) {
            for (var j = 0; j < results[0].address_components[i].types.length; j++) {
                if (results[0].address_components[i].types[j] == "locality") {
                    long_name = results[0].address_components[i].long_name;
                }
                if (results[0].address_components[i].types[j] == "country") {
                    country = results[0].address_components[i];
                    country_code = country.short_name;
                    country_name = country.long_name;
                }
            }
    }
    $("#lat").val(latlng.lat());
    $("#lng").val(latlng.lng());
    console.log('country code: ' + country_code);
    console.log('country name: ' + country_name);
    $("#country_code").val(country_code);
    $("#country_name").val(country_name);
    $("#label").val(long_name);
}

function saveNewSight(map, sightBaseURL) {
    var id = $("#sight-id");
    var code = $("#country_code");
    console.log('country code: ' + code.val());
    var country_name = $("#country_name");
    var label = $("#label");
    var description = $("#description");
    var lat = $("#lat");
    var lng = $("#lng");
    var data = {}
    var country = {}
    country["countryCode"] = code.val();
    country["countryName"] = country_name.val();
    data["sightId"] = id.val();

    data["country"] = country;
    data["label"] = label.val();
    data["description"] = description.val();
    data["mapCoordX"] = lat.val();
    data["mapCoordY"] = lng.val();

    tips = $(".validateTips");
    var valid = false;
    valid = checkMinMaxLength(code, "code", 2, 2);
    valid = valid && checkMinMaxLength(label, "label", 3, 80);
    valid = valid && checkMinMaxLength(description, "description", 3, 80);

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var save_sight_url = "../sights/add-sight";

    if (valid) {
        $.ajax({
            url: save_sight_url,
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (obj) {
                hideModalDialog();
                console.log("sight id: " + obj.sightId);
                window.location.href = sightBaseURL + obj.sightId;
            },
            error: function (e) {
                console.log("Error: ", e);
                display(e);
            },
            done: function (e) {
                alert("DONE");
            }
        });
    }
}
function updSight(id) {
    alert('ok, ' + id);
    var upd_sight_url = "../sights/edit/" + id;
    $.get(upd_sight_url, function (data) {
        //event.preventDefault();
        $("#sight-id").val(id);
        console.log('country code: ' + data.country.country_code);
        $("#country_code").val(data.country.country_code);
        $("#label").val(data.sight_label);
        $("#description").val(data.sight_description);
        $("#lat").val(data.map_coord_x);
        $("#lng").val(data.map_coord_y);
        $("#modal-form").modal();
    });
}
function addNewMarker(map, obj) {
    var infowindow = new google.maps.InfoWindow();

    var newMarker = new google.maps.Marker({
        map: map,
        position: {lat: obj.map_coord_x, lng: obj.map_coord_y},
        title: obj.sight_label,
        description: obj.sight_description,
        id: obj.sight_id
    });
    console.log('new marker title: ' + newMarker.title);

    newMarker.addListener('click', function () {
        infowindow.setContent('<p>' + this.title + '</p><p>' + this.description + '</p><a href="../posts/sight/' + this.id + '">See more</a> or <button onclick="updSight(' + this.id + ')" type="submit"/>Update sight</button>');
        infowindow.open(map, this);
    });
}
