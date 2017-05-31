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


/**
 * A simple {@link Fragment} subclass.
 */
public class FestasFragment extends Fragment implements AdapterView.OnItemClickListener{

    RetornaListaFestas retornaListaFestas;
    public RecyclerView recyclerView;
    private static int prim_vez = 0;
    ConnectionDetector cd;

    View gView;


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


            new FestasJSONTask().execute("https://www.curtaja.com/api/v1/eventos/search");

            recyclerView.addOnItemTouchListener(new FestasFragment.FestasAdapter.RecyclerTouchListener(getContext(), recyclerView, new FestasFragment.FestasAdapter.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    if (prim_vez == 0) {

                        Intent i = new Intent(getActivity(), CadastrarActivity.class);
                        prim_vez = 1;
                        startActivity(i);
                    } else if (prim_vez == 1) {


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

    public class FestasJSONTask extends AsyncTask<String, String, List<FestasModel>> {

        List<FestasModel> festasModelsList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<FestasModel> doInBackground(String... params) {

            String colummn = "categorias_id";
            String operator = "=";
            int search = 4;
            String searchString = Integer.toString(search);
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("column", "UTF-8") + "=" + URLEncoder.encode(colummn, "UTF-8") + "&" +
                        URLEncoder.encode("operator", "UTF-8") + "=" + URLEncoder.encode(operator, "UTF-8") + "&" +
                        URLEncoder.encode("search", "UTF-8") + "=" + URLEncoder.encode(searchString, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String linha = "";

                while ((linha = bufferedReader.readLine()) != null) {

                    stringBuffer.append(linha);
                }
                String finalJson = stringBuffer.toString();
                // JSONObject jsonObject = new JSONObject(finalJson);
                //JSONArray jsonArray = jsonObject.getJSONArray("");

                festasModelsList = new ArrayList<>();

                //  Gson gson = new Gson();

                //  for(int i = 0; i<jsonArray.length();i++){

                //  JSONObject finalObject = jsonArray.getJSONObject(i);
                FestasModel[] festasModel = new Gson().fromJson(finalJson, FestasModel[].class);
                //baresModel.setId(finalObject.getInt("id"));
                    /*baresModel.setDescricao(finalObject.getString("descricao"));
                    baresModel.setImagem(finalObject.getString("imagem"));
                    baresModel.setObservacao(finalObject.getString("observacao"));*/

                //adicionando o objeto final na lista
                //   baresModelsList.add(baresModel);

//                }


                for (int i = 0; i < festasModel.length; i++) {
                    if(festasModel[i].getStatus() == 1){
                        festasModelsList.add(festasModel[i]);

                    }
                }
                return festasModelsList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<FestasModel> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            FestasFragment.FestasAdapter festasAdapter = new FestasFragment.FestasAdapter(getContext(), result);
            retornaListaFestas = new RetornaListaFestas(result);
            festasAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(festasAdapter);
        }

    }
}
