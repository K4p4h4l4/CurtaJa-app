package com.example.tolavio.curta;

import android.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;

public class FestasDetalhes extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    public static final String EXTRA_IMAGE = "extra_image";
    public static final String EXTRA_NOME = "extra_nome";
    public static final String EXTRA_LOCAL = "extra_local";
    public static final String EXTRA_OBS = "extra_obs";
    public static final String EXTRA_DETALHES = "extra_detalhes";
    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_CIDADE = "extra_cidade";
    public static final String EXTRA_NUMERO = "extra_numero";

    Geocoder gc;
    double lat, lng;
    GoogleApiClient mGoogleApiClient;
    MapView mapView;
    String cidade;
    private GoogleMap mMap;
    LatLng ll, eventoLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festas_detalhes);

        ImageView imageView = (ImageView) findViewById(R.id.imagem_festa_ampliada);
        TextView title = (TextView) findViewById(R.id.title_festas);
        TextView festa_local = (TextView) findViewById(R.id.local_festas);
        TextView festa_obs = (TextView) findViewById(R.id.observacoes_festas);
        TextView festa_detalhe = (TextView) findViewById(R.id.festas_detalhes);
        TextView festaData = (TextView) findViewById(R.id.data_hora_festas);
        Button festas_addButton = (Button) findViewById(R.id.festas_add);
        Button festas_likeButton = (Button) findViewById(R.id.festas_like);
        Button festas_comment = (Button) findViewById(R.id.festas_commennt);


        if (getIntent() != null && getIntent().getExtras() != null) {

            if (getIntent().getExtras().containsKey(EXTRA_IMAGE)) {
                ImageLoader.getInstance().displayImage(getIntent().getExtras().getString(EXTRA_IMAGE), imageView);
            }

            if (getIntent().getExtras().containsKey(EXTRA_NOME)) {
                title.setText(getIntent().getExtras().getString(EXTRA_NOME));
            }

            if (getIntent().getExtras().containsKey(EXTRA_LOCAL)) {
                festa_local.setText("Local: " + getIntent().getExtras().getString(EXTRA_LOCAL)); //+ " " + getIntent().getExtras().getString(EXTRA_NUMERO));
                cidade = getIntent().getExtras().getString(EXTRA_CIDADE) + " " + getIntent().getExtras().getString(EXTRA_LOCAL) + " " + getIntent().getExtras().getString(EXTRA_NUMERO);
            }

            if (getIntent().getExtras().containsKey(EXTRA_OBS)) {
                festa_obs.setText("Observações: " + getIntent().getExtras().getString(EXTRA_OBS));
            }

            if (getIntent().getExtras().containsKey(EXTRA_DETALHES)) {
                festa_detalhe.setText(getIntent().getExtras().getString(EXTRA_DETALHES));
            }

            if (getIntent().getExtras().containsKey(EXTRA_DATA)) {
                festaData.setText("Data: " + getIntent().getExtras().getString(EXTRA_DATA));
            }

        }

        festas_addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        festas_likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        festas_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        gc = new Geocoder(this);

        try {
            List<Address> list = gc.getFromLocationName(cidade, 1);
            if(list.size() == 0){
                Toast.makeText(this,"Falha ao carregar ao map",Toast.LENGTH_LONG).show();
            }else {
                Address address = list.get(0);

                lat = address.getLatitude();
                lng = address.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mapView = (MapView) findViewById(R.id.festasMap);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(mapView.getContext());

        mMap = googleMap;

        eventoLocation = new LatLng(lat, lng);

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);*/



        /*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, true);

        Location myLocation = locationManager.getLastKnownLocation(provider);*/

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions().position(eventoLocation).title("Festa"));
        CameraPosition cameraPosition = CameraPosition.builder().target(eventoLocation).zoom(15).bearing(0).tilt(45).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    LocationRequest locationRequest;

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this,"Falha na localização...",Toast.LENGTH_LONG).show();
        }else{
            ll = new LatLng(location.getLatitude(),location.getLongitude());


            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,15);
            mMap.animateCamera(update);
            mMap.addPolyline(new PolylineOptions().add(
                    eventoLocation, ll
                    ).width(4).color(Color.BLUE)
            );

        }
    }
}
