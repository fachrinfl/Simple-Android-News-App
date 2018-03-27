package me.fachrinfl.androidnewsapp.Common;

import me.fachrinfl.androidnewsapp.Interface.NewsService;
import me.fachrinfl.androidnewsapp.Remote.RetrofitClient;

/**
 * Created by fachrinfl on 27/03/18.
 */

public class Common {

    private static final String BASE_URL="https://newsapi.org/";

    public  static final String API_KEY="d1ce30a31cf7499bb0538e08b7c4e60d";

    public static NewsService getNewsService(){
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static String getAPIUrl(String source,String sortBy,String apiKEY)
    {
        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source)
                .append("&apiKey=")
                .append(apiKEY)
                .toString();
    }
}
