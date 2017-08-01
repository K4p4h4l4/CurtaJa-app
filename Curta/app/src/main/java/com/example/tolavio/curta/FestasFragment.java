package com.example.tolavio.curta;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tolavio.curta.models.BaresModel;
import com.example.tolavio.curta.models.FestasModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 */
public class FestasFragment extends Fragment implements AdapterView.OnItemClickListener{

    RetornaListaFestas retornaListaFestas;
    public RecyclerView recyclerView;
    private static int prim_vez = 0;
    ConnectionDetector cd;
    private static int SIGN_IN_REQUEST_CODE = 1;

    View gView;

    private ApiFestasInterface apiFestasInterface;
    private List<FestasModel> festas;
    FestasAdapter festasAdapter;
    private ProgressDialog progressDialog;


    public FestasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        gView = inflater.inflate(R.layout.fragment_festas, container, false);
        cd = new ConnectionDetector(gView.getContext());

        if(cd.isConnected()) {

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Carregando. Espere por favor...");

            recyclerView = (RecyclerView) gView.findViewById(R.id.festasRecycleView);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new FestasFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());


            apiFestasInterface = ApiFestasClient.getApiClient().create(ApiFestasInterface.class);

            Call<List<FestasModel>> call = apiFestasInterface.getFestasModel();

            call.enqueue(new Callback<List<FestasModel>>() {
                @Override
                public void onResponse(Call<List<FestasModel>> call, retrofit2.Response<List<FestasModel>> response) {
                    festas = response.body();
                    /*List<BaresModel> baresLocal = null;
                    for (int i = 0; i < bares.size(); i++) {
                        if(bares.get(i).getStatus() == 1){

                            baresLocal.add(bares.get(i));

                        }
                    }*/
                    festasAdapter = new FestasFragment.FestasAdapter(getContext(), festas);
                    retornaListaFestas = new RetornaListaFestas(festas);
                    festasAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(festasAdapter);
                }

                @Override
                public void onFailure(Call<List<FestasModel>> call, Throwable t) {

                }
            });


            recyclerView.addOnItemTouchListener(new FestasFragment.FestasAdapter.RecyclerTouchListener(getContext(), recyclerView, new FestasFragment.FestasAdapter.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    if(FirebaseAuth.getInstance().getCurrentUser()==null){

                        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);

                    }else {

                        List<FestasModel> festasList = retornaListaFestas.RetornaListaFestas();

                        Intent i = new Intent(getActivity(), FestasDetalhes.class);
                        i.putExtra(FestasDetalhes.EXTRA_IMAGE, festasList.get(position).getBanner());
                        i.putExtra(FestasDetalhes.EXTRA_NOME, festasList.get(position).getNome());
                        i.putExtra(FestasDetalhes.EXTRA_LOCAL, festasList.get(position).getEndereco());
                        i.putExtra(FestasDetalhes.EXTRA_OBS, festasList.get(position).getObservacao());
                        i.putExtra(FestasDetalhes.EXTRA_DETALHES, festasList.get(position).getDescricao());
                        i.putExtra(FestasDetalhes.EXTRA_DATA, festasList.get(position).getDataHora());
                        i.putExtra(FestasDetalhes.EXTRA_CIDADE, festasList.get(position).getCidade());

                        startActivity(i);
                    }
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }else{
            gView = inflater.inflate(R.layout.no_connection,container,false);
        }

        return gView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(mTracker != null){

            mTracker.setScreenName(getClass().getSimpleName());
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }*/
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    /**
     * Created by Tolavio on 28-02-2017.
     */

    public static class FestasAdapter extends RecyclerView.Adapter<FestasFragment.FestasAdapter.MyViewHolder> {

        private Context c;
        private List<FestasModel> festasModelList;


        public FestasAdapter(Context context, List<FestasModel> objects) {

            festasModelList = objects;
            this.c = context;

        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.festas_model, parent, false);

            return new FestasFragment.FestasAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final FestasFragment.FestasAdapter.MyViewHolder holder, int position) {
            //BaresModel bares = baresModelList.get(position);

            holder.mes.setText(festasModelList.get(position).getNome());
            holder.dia.setText(festasModelList.get(position).getDataHora());
            //carregando imagem
            //Glide.with(c).load(bares.getImagem()).into(holder.imagem);

            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(festasModelList.get(position).getBanner(), holder.imagem, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.progressBar.setVisibility(View.VISIBLE);

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            }); // Default options will be used
            //holder.imagem.setImageResource(bares.getImagem());


            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(holder.overflow);
                }
            });
        }

        private void showPopupMenu(View view) {
            //inflate Menu
            /*PopupMenu popupMenu = new PopupMenu(c, view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_festas, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new FestasFragment.FestasAdapter.MyMenuClickListener());
            popupMenu.show();*/
        }

        @Override
        public int getItemCount() {
            return festasModelList.size();
        }

        public interface ClickListener {
            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }

        public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

            private GestureDetector gestureDetector;
            private FestasFragment.FestasAdapter.ClickListener clickListener;

            public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FestasFragment.FestasAdapter.ClickListener clickListener) {
                this.clickListener = clickListener;
                gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                        }
                    }
                });
            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                    clickListener.onClick(child, rv.getChildPosition(child));
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            final ProgressBar progressBar;
            private TextView mes, dia;
            private ImageView imagem, overflow;


            public MyViewHolder(View view) {
                super(view);
                progressBar = (ProgressBar) view.findViewById(R.id.progressBarFestas);
                mes = (TextView) view.findViewById(R.id.mesEventoFestas);
                imagem = (ImageView) view.findViewById(R.id.imagemEventoFestas);
                dia = (TextView) view.findViewById(R.id.diaEventoFestas);
                overflow = (ImageView) view.findViewById(R.id.festasDots);

            }
        }



        class MyMenuClickListener implements PopupMenu.OnMenuItemClickListener {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_eventos:
                        Toast.makeText(c, "Adicionar aos seus eventos", Toast.LENGTH_LONG).show();
                        return true;

                    case R.id.action_comprar:
                        Toast.makeText(c, "Comprar ingresso para eventos", Toast.LENGTH_LONG).show();
                        return true;

                    default:
                }

                return false;
            }
        }

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
