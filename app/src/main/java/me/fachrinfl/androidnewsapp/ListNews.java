package me.fachrinfl.androidnewsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.fachrinfl.androidnewsapp.Adapter.ListNewsAdapter;
import me.fachrinfl.androidnewsapp.Common.Common;
import me.fachrinfl.androidnewsapp.Interface.NewsService;
import me.fachrinfl.androidnewsapp.Model.Article;
import me.fachrinfl.androidnewsapp.Model.News;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNews extends AppCompatActivity {

    @BindView(R.id.top_image)
    KenBurnsView kbv;
    NewsService mService;
    @BindView(R.id.top_title)
    TextView top_title;
    @BindView(R.id.top_headline)
    TextView top_headline;
    @BindView(R.id.topHeadline)
    RelativeLayout topHeadline;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeLayout;

    String source="",sortBy="",webHotURL="";
    ProgressDialog progressDialog;
    ListNewsAdapter adapter;
    @BindView(R.id.lstNews)
    RecyclerView lstNews;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);
        ButterKnife.bind(this);
        String fontHeadline = "fonts/Ubuntu-Bold.ttf";
        String fontTitle = "fonts/Ubuntu-Medium.ttf";

        mService = Common.getNewsService();

        swipeLayout.setColorSchemeColors(Color.parseColor("#1abc9c"), Color.parseColor("#f1c40f"), Color.parseColor("#379adc"), Color.parseColor("#e74c3c"), Color.parseColor("#34495e"));
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source,true);
            }
        });

        topHeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Tes", Toast.LENGTH_LONG).show();
                Intent detail = new Intent(getBaseContext(),DetailArticle.class);
                detail.putExtra("webURL",webHotURL);
                startActivity(detail);
            }
        });

        top_title.setTypeface(Typeface.createFromAsset(getBaseContext().getAssets(), fontTitle));
        top_headline.setTypeface(Typeface.createFromAsset(getBaseContext().getAssets(), fontHeadline));

        initRecyclerView();
        progressBarDialog();

        if(getIntent() != null)
        {
            source = getIntent().getStringExtra("source");
            if(!source.isEmpty())
            {
                loadNews(source,false);
            }
        }
    }

    private void loadNews(String source, boolean isRefreshed) {
        if(!isRefreshed) {
            progressDialog.show();
            mService.getNewestArticles(Common.getAPIUrl(source,sortBy,Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            progressDialog.dismiss();
                            Picasso.with(getBaseContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);

                            top_title.setText(response.body().getArticles().get(0).getTitle());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> removeFristItem = response.body().getArticles();
                            removeFristItem.remove(0);
                            loadDataRecyclerView(removeFristItem);

                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        } else {
            progressDialog.show();
            mService.getNewestArticles(Common.getAPIUrl(source,sortBy,Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            progressDialog.dismiss();
                            Picasso.with(getBaseContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);

                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            webHotURL = response.body().getArticles().get(0).getUrl();

                            List<Article> removeFristItem = response.body().getArticles();
                            removeFristItem.remove(0);
                            loadDataRecyclerView(removeFristItem);

                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
            swipeLayout.setRefreshing(false);
        }
    }

    private void loadDataRecyclerView(List<Article> removeFristItem) {
        adapter = new ListNewsAdapter(removeFristItem, getBaseContext());
        adapter.notifyDataSetChanged();
        lstNews.setAdapter(adapter);
    }

    private void initRecyclerView() {
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);
    }

    private void progressBarDialog() {
        progressDialog = new ProgressDialog(ListNews.this, R.style.CircularDialogsTheme);
        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please wait...");
    }
}
