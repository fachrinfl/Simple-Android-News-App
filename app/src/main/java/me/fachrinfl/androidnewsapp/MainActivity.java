package me.fachrinfl.androidnewsapp;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import me.fachrinfl.androidnewsapp.Adapter.ListSourceAdapter;
import me.fachrinfl.androidnewsapp.Common.Common;
import me.fachrinfl.androidnewsapp.Interface.NewsService;
import me.fachrinfl.androidnewsapp.Model.WebSite;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.list_source)
    RecyclerView listWebsite;
    ProgressDialog progressDialog;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListSourceAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Paper.init(this);
        mService = Common.getNewsService();

        String fontTitle = "fonts/Ubuntu-Bold.ttf";
        tvTitle.setTypeface(Typeface.createFromAsset(getBaseContext().getAssets(), fontTitle));

        swipeLayout.setColorSchemeColors(Color.parseColor("#1abc9c"), Color.parseColor("#f1c40f"), Color.parseColor("#379adc"), Color.parseColor("#e74c3c"), Color.parseColor("#34495e"));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });

        initRecyclerView();
        progressBarDialog();
        loadWebsiteSource(false);

    }

    private void loadWebsiteSource(boolean isRefreshed) {
        if (!isRefreshed) {

            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty() && !cache.equals("null")) {
                WebSite website = new Gson().fromJson(cache, WebSite.class);
                loadDataRecyclerView(website);
            } else {
                progressDialog.show();
                mService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        loadDataRecyclerView(response.body());
                        Paper.book().write("cache", new Gson().toJson(response.body()));
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Can't fetch data", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {

            swipeLayout.setRefreshing(true);
            mService.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    loadDataRecyclerView(response.body());
                    Paper.book().write("cache", new Gson().toJson(response.body()));
                    swipeLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });

        }
    }

    private void initRecyclerView() {
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);
    }

    private void loadDataRecyclerView(WebSite webSite) {
        adapter = new ListSourceAdapter(getBaseContext(), webSite);
        adapter.notifyDataSetChanged();
        listWebsite.setAdapter(adapter);
    }

    private void progressBarDialog() {
        progressDialog = new ProgressDialog(MainActivity.this, R.style.CircularDialogsTheme);
        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please wait...");
    }

}
