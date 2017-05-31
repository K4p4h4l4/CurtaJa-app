package com.example.tolavio.curta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.tolavio.curta.models.BaresModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
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

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaresFragment extends Fragment implements AdapterView.OnItemClickListener {

    RetornaListaBares retornaListaBares;
    public RecyclerView recyclerView;
    private Tracker mTracker;

    ViewFlipper viewFlipper;
    Animation fadeIn, fadeOut;
    boolean login = false;
    private static int SIGN_IN_REQUEST_CODE = 1;

    boolean cadastro = false;
    View gView;
    ConnectionDetector cd;


    private ProgressDialog progressDialog;

    public BaresFragment() {
        // Required empty public co nstructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        gView = inflater.inflate(R.layout.fragment_bares, container, false);

        cd = new ConnectionDetector(gView.getContext());
        // Inflate the layout for this fragment

        //if(cd.isConnected() && retornaListaBares.RetornaListaBares() == null)
        if(cd.isConnected()) {


            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Carregando. Espere por favor...");

            //inicializando viewflipper
        /*viewFlipper = (ViewFlipper) gView.findViewById(R.id.viewFlipperBares);

        //Passando animação para as variáveis fadeIn e FadeOut
        fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);


        //dando inicialização automática e animação
        viewFlipper.setAutoStart(true);
        viewFlipper.setInAnimation(fadeIn);
        viewFlipper.setOutAnimation(fadeOut);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.startFlipping();*/

            recyclerView = (RecyclerView) gView.findViewById(R.id.baresRecycleView);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            new JSONTask().execute("https://www.curtaja.com/api/v1/eventos/search");


            recyclerView.addOnItemTouchListener(new BaresAdapter.RecyclerTouchListener(getContext(), recyclerView, new BaresAdapter.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    List<BaresModel> baresList = retornaListaBares.RetornaListaBares();

                    Intent i = new Intent(getActivity(), BaresDetalhes.class);
                    i.putExtra(BaresDetalhes.EXTRA_IMAGE, baresList.get(position).getBanner());
                    i.putExtra(BaresDetalhes.EXTRA_NOME, baresList.get(position).getNome());
                    i.putExtra(BaresDetalhes.EXTRA_LOCAL, baresList.get(position).getEndereco());
                    i.putExtra(BaresDetalhes.EXTRA_OBS, baresList.get(position).getObservacao());
                    i.putExtra(BaresDetalhes.EXTRA_DETALHES, baresList.get(position).getDescricao());
                    i.putExtra(BaresDetalhes.EXTRA_DATA, baresList.get(position).getDataHora());
                    i.putExtra(BaresDetalhes.EXTRA_CIDADE, baresList.get(position).getCidade());
                    i.putExtra(BaresDetalhes.EXTRA_NUMERO, baresList.get(position).getNumero());

                    startActivity(i);


                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }else{
            gView = inflater.inflate(R.layout.no_connection,container, false);
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

    public static class BaresAdapter extends RecyclerView.Adapter<BaresAdapter.MyViewHolder> {

        private Context c;
        private List<BaresModel> baresModelList;


        public BaresAdapter(Context context, List<BaresModel> objects) {

            baresModelList = objects;
            this.c = context;

        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bares_model, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            //BaresModel bares = baresModelList.get(position);

            holder.mes.setText(baresModelList.get(position).getNome());
            holder.dia.setText(baresModelList.get(position).getDataHora());
            //carregando imagem
            //Glide.with(c).load(bares.getImagem()).into(holder.imagem);

            // Then later, when you want to display image
            ImageLoader.getInstance().displayImage(baresModelList.get(position).getBanner(), holder.imagem, new ImageLoadingListener() {
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
            PopupMenu popupMenu = new PopupMenu(c, view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_bares, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new MyMenuClickListener());
            popupMenu.show();
        }

        @Override
        public int getItemCount() {
            return baresModelList.size();
        }

        public interface ClickListener {
            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }

        public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

            private GestureDetector gestureDetector;
            private BaresAdapter.ClickListener clickListener;

            public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final BaresAdapter.ClickListener clickListener) {
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
                progressBar = (ProgressBar) view.findViewById(R.id.progressBarBares);
                mes = (TextView) view.findViewById(R.id.mesEventoBares);
                imagem = (ImageView) view.findViewById(R.id.imagemEventoBares);
                dia = (TextView) view.findViewById(R.id.diaEventoBares);
                overflow = (ImageView) view.findViewById(R.id.baresDots);

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

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
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

    public class JSONTask extends AsyncTask<String, String, List<BaresModel>> {

        List<BaresModel> baresModelsList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<BaresModel> doInBackground(String... params) {

            String colummn = "categorias_id";
            String operator = "=";
            int search = 5;
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

                baresModelsList = new ArrayList<>();

                //  Gson gson = new Gson();

                //  for(int i = 0; i<jsonArray.length();i++){

                //  JSONObject finalObject = jsonArray.getJSONObject(i);
                BaresModel[] baresModel = new Gson().fromJson(finalJson, BaresModel[].class);
                //baresModel.setId(finalObject.getInt("id"));
                    /*baresModel.setDescricao(finalObject.getString("descricao"));
                    baresModel.setImagem(finalObject.getString("imagem"));
                    baresModel.setObservacao(finalObject.getString("observacao"));*/

                //adicionando o objeto final na lista
                //   baresModelsList.add(baresModel);

//                }


                for (int i = 0; i < baresModel.length; i++) {
                    if(baresModel[i].getStatus() == 1){
                        baresModelsList.add(baresModel[i]);

                    }
                }
                return baresModelsList;

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
        protected void onPostExecute(List<BaresModel> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            BaresAdapter baresAdapter = new BaresAdapter(getContext(), result);
            retornaListaBares = new RetornaListaBares(result);
            baresAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(baresAdapter);
        }

    }
}

