package com.example.tolavio.curta;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Camera;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class BaresDetalhes extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public static final String EXTRA_IMAGE = "extra_image";
    public static final String EXTRA_NOME = "extra_nome";
    public static final String EXTRA_LOCAL = "extra_local";
    public static final String EXTRA_OBS = "extra_obs";
    public static final String EXTRA_DETALHES = "extra_detalhes";
    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_CIDADE = "extra_cidade";
    public static final String EXTRA_NUMERO = "extra_numero";

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    View mView;
    SupportMapFragment mapFragment;
    Geocoder gc;
    double lat, lng;
    GoogleApiClient mGoogleApiClient;
    MapView mapView;
    String cidade;
    LatLng ll, eventoLocation;
    Location mLocation;
    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<BarComment> adapter;

    Marker mCurrLoacationMarker;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN_REQUEST_CODE){

            if(resultCode == RESULT_OK){
                Toast.makeText(getBaseContext(),"Login com sucesso.",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getBaseContext(),"Erro no login. Tente novamente",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bares_detalhes);


        ImageView imageView = (ImageView) findViewById(R.id.imagem_bar_ampliada);
        TextView title = (TextView) findViewById(R.id.title_bares);
        TextView bar_local = (TextView) findViewById(R.id.local_bares);
        TextView bar_obs = (TextView) findViewById(R.id.observacoes_bares);
        TextView bar_detalhe = (TextView) findViewById(R.id.bares_detalhes);
        TextView barData = (TextView) findViewById(R.id.data_hora_bares);
        Button bares_addButton = (Button) findViewById(R.id.bares_add);
        Button bares_likeButton = (Button) findViewById(R.id.bares_like);
        TextView bares_comment = (TextView) findViewById(R.id.bares_commennt);
        FloatingActionButton bar_comment = (FloatingActionButton) findViewById(R.id.snd_cmnt_bar);


        if (getIntent() != null && getIntent().getExtras() != null) {

            if (getIntent().getExtras().containsKey(EXTRA_IMAGE)) {
                ImageLoader.getInstance().displayImage(getIntent().getExtras().getString(EXTRA_IMAGE), imageView);
            }

            if (getIntent().getExtras().containsKey(EXTRA_NOME)) {
                title.setText(getIntent().getExtras().getString(EXTRA_NOME));
            }

            if (getIntent().getExtras().containsKey(EXTRA_LOCAL)) {
                bar_local.setText("Local: " + getIntent().getExtras().getString(EXTRA_LOCAL));
                cidade = getIntent().getExtras().getString(EXTRA_CIDADE) + " " + getIntent().getExtras().getString(EXTRA_LOCAL) + " " + getIntent().getExtras().getString(EXTRA_NUMERO);
            }

            if (getIntent().getExtras().containsKey(EXTRA_OBS)) {
                bar_obs.setText("Observações: " + getIntent().getExtras().getString(EXTRA_OBS));
            }

            if (getIntent().getExtras().containsKey(EXTRA_DETALHES)) {
                bar_detalhe.setText(getIntent().getExtras().getString(EXTRA_DETALHES));
            }

            if (getIntent().getExtras().containsKey(EXTRA_DATA)) {
                barData.setText("Data: " + getIntent().getExtras().getString(EXTRA_DATA));
            }
        }


        bares_addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getBaseContext(),"Adicionado aos seus eventos", Toast.LENGTH_LONG).show();
            }
        });

        bares_likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getBaseContext(),"Like ao evento", Toast.LENGTH_LONG).show();
            }
        });


        gc = new Geocoder(this);

        try {
            List<Address> list = gc.getFromLocationName(cidade, 1);

            if(list.size()== 0){

            }else {
                Address address = list.get(0);

                lat = address.getLatitude();
                lng = address.getLongitude();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        mapView = (MapView) findViewById(R.id.baresMap);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        bar_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input_bares = (EditText) findViewById(R.id.bar_comentario_input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new BarComment(input_bares.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input_bares.setText("");
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser()==null){

            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);

        }else {

            Toast.makeText(this, "WELCOME" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            //carregando conteudo
            displayCommentUser();
        }
    }

    private void displayCommentUser() {

        ListView listComment = (ListView) findViewById(R.id.list_of_comments);
        adapter = new FirebaseListAdapter<BarComment>(this,BarComment.class,R.layout.lista_bar_comentario,FirebaseDatabase.getInstance().getReference()) {

            @Override
            protected void populateView(View v, BarComment model, int position) {

                //pegar as referencias para as views list_bar_comentario.xml
                TextView commentText = (TextView)v.findViewById(R.id.comentario_text);
                TextView commentUser = (TextView)v.findViewById(R.id.comentario_user);
                TextView commentTime = (TextView)v.findViewById(R.id.comentario_time);

                commentText.setText(model.getBarCommentText());
                commentUser.setText(model.getBarCommentUser());
                commentTime.setText(android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getBarCommentTime()));
            }
        };
        listComment.setAdapter(adapter);
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

        if(eventoLocation == null){
            Toast.makeText(this,"Falha na localização",Toast.LENGTH_LONG).show();
        }else {
            mMap.addMarker(new MarkerOptions().position(eventoLocation).title("Bar"));
            CameraPosition cameraPosition = CameraPosition.builder().target(eventoLocation).zoom(15).bearing(0).tilt(45).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    LocationRequest locationRequest;

    @Override
    public void onConnected(Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
