package me.fachrinfl.androidnewsapp.Interface;

import me.fachrinfl.androidnewsapp.Common.Common;
import me.fachrinfl.androidnewsapp.Model.News;
import me.fachrinfl.androidnewsapp.Model.WebSite;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by fachrinfl on 27/03/18.
 */

public interface NewsService {

    @GET("v2/sources?language=en&apiKey="+ Common.API_KEY)
    Call<WebSite> getSources();

    @GET
    Call<News> getNewestArticles(@Url String url);

}
