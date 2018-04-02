package com.gbeatty.arxivexplorer.network;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ArxivAPI {

    public static String SORT_ORDER_ASCENDING = "ascending";
    public static final String SORT_ORDER_DESCENDING = "descending";
    public static final String SORT_BY_RELEVANCE = "relevance";
    public static String SORT_BY_LAST_UPDATED_DATE = "lastUpdatedDate";
    public static final String SORT_BY_SUBMITTED_DATE = "submittedDate";
    private static OkHttpClient client;
    private static final String baseURL = "http://export.arxiv.org/api/";
    private static final String querySegment = "query";

    public static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    public static void searchPapersFromCategory(String catKey, String category, String sortOrder,
                                                String sortBy, int maxResults, Callback call) {


        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL).newBuilder();
        urlBuilder.addPathSegment(querySegment);

        String query = "";

        query = addANDParamToQuery(query, catKey, category);

        urlBuilder.addQueryParameter("search_query", query);
        urlBuilder.addEncodedQueryParameter("sortOrder", sortOrder);
        urlBuilder.addEncodedQueryParameter("sortBy", sortBy);
        urlBuilder.addEncodedQueryParameter("max_results", String.valueOf(maxResults));

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        getClient().newCall(request).enqueue(call);
    }

    private static String addANDParamToQuery(String query, String key, String param) {
        if (param == null || param.equals("")) return query;
        if (query.equals("")) {
            query = query + key + ":" + param;
        } else {
            query = query + " " + "AND" + " " + key + ":" + param;
        }
        return query;
    }

    private static String addORParamToQuery(String query, String key, String param) {
        if (param == null || param.equals("")) return query;
        if (query.equals("")) {
            query = query + key + ":" + param;
        } else {
            query = query + " " + "OR" + " " + key + ":" + param;
        }
        return query;
    }

    public static void searchMultipleCategories(String[] catKeys, String[] categories,
                                 String sortOrder,
                                 String sortBy,
                                 int maxResults,
                                 Callback call) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL).newBuilder();
        urlBuilder.addPathSegment(querySegment);

        String query = "";

        if(categories != null && catKeys != null){
            for(int i = 0; i < categories.length; i++){
                query = addORParamToQuery(query, catKeys[i], categories[i]);
            }
        }

        urlBuilder.addQueryParameter("search_query", query);
        urlBuilder.addEncodedQueryParameter("sortOrder", sortOrder);
        urlBuilder.addEncodedQueryParameter("sortBy", sortBy);
        urlBuilder.addEncodedQueryParameter("max_results", String.valueOf(maxResults));

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();


        getClient().newCall(request).enqueue(call);
    }

    public static void searchAll(String searchQuery,
                                    String sortOrder,
                                    String sortBy,
                                    int maxResults,
                                    Callback call) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL).newBuilder();
        urlBuilder.addPathSegment(querySegment);

        String query = "";
        query = addANDParamToQuery(query, "all", searchQuery);

        urlBuilder.addQueryParameter("search_query", query);
        urlBuilder.addEncodedQueryParameter("sortOrder", sortOrder);
        urlBuilder.addEncodedQueryParameter("sortBy", sortBy);
        urlBuilder.addEncodedQueryParameter("max_results", String.valueOf(maxResults));


        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        getClient().newCall(request).enqueue(call);

    }


    public static void downloadFileFromURL(String url, Callback call) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        getClient().newCall(request).enqueue(call);
    }

    public static void paginateQuery(String query, int start, Callback call) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(query).newBuilder();
        urlBuilder.addQueryParameter("start", String.valueOf(start));
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        getClient().newCall(request).enqueue(call);
    }

}
