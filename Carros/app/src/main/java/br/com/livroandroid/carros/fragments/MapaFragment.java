package br.com.livroandroid.carros.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.livroandroid.carros.R;
import br.com.livroandroid.carros.domain.Carro;

// Fragment que mostra o mapa no local da coordenada lat/lng do carro
public class MapaFragment extends BaseFragment implements OnMapReadyCallback {
    // Objeto que controla o Google Maps
    private GoogleMap map;
    private Carro carro;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        // Recupera o fragment que está no layout
        // Utiliza o getChildFragmentManager(), pois é um fragment dentro do outro
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.mapFragment);
        // Inicia o Google Maps dentro do fragment
        mapFragment.getMapAsync(this);
        this.carro = (Carro) getArguments().getParcelable("carro");
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // O método onMapReady(map) é chamado quando a inicialização do mapa estiver Ok
        this.map = map;
        if (carro != null) {
            // Ativa o botão para mostrar minha localização
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);

            // Cria o objeto LatLng com a coordenada da fábrica
            if(carro.latitude != null && carro.latitude.trim().length() > 0) {
                LatLng location = new LatLng(Double.parseDouble(carro.latitude), Double.parseDouble(carro.longitude));
                // Posiciona o mapa na coordenada da fábrica (zoom = 13)
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 13);
                map.moveCamera(update);
                // Marcador no local da fábrica
                map.addMarker(new MarkerOptions().title(carro.nome).snippet(carro.desc).
                        position(location));
                // Tipo do mapa:
                // (normal, satélite, terreno ou híbrido)
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        }
    }
}
