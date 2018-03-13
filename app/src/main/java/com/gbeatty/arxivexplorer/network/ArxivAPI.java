package com.gbeatty.arxivexplorer.network;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ArxivAPI {

    public static String SORT_ORDER_ASCENDING = "ascending";
    public static String SORT_ORDER_DESCENDING = "descending";
    public static String SORT_BY_RELEVANCE = "relevance";
    public static String SORT_BY_LAST_UPDATED_DATE = "lastUpdatedDate";
    public static String SORT_BY_SUBMITTED_DATE = "submittedDate";
    private static OkHttpClient client;
    private static String baseURL = "http://export.arxiv.org/api/";
    private static String querySegment = "query";

    public static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    public static void downloadPapersFromCategory(String catKey, String category, String sortOrder,
                                                  String sortBy, int maxResults, Callback call) throws Exception {

        searchPapers(null,
                null,
                null,
//                null,
//                null,
                catKey,
                category,
//                null,
                null,
//                null,
                sortOrder,
                sortBy,
                maxResults,
                call);
    }

    private static String addParamToQuery(String query, String key, String param) {
        if (param == null || param.equals("")) return query;
        if (query.equals("")) {
            query = query + key + ":" + param;
        } else {
            query = query + "+" + "AND" + "+" + key + ":" + param;
        }
        return query;
    }

    public static void searchPapers(String title,
                                    String[] authors,
                                    String summary,
//                                    String comment,
//                                    String journalReference,
                                    String catKey,
                                    String category,
//                                    String reportNumber,
                                    String id,
//                                    String all,
                                    String sortOrder,
                                    String sortBy,
                                    int maxResults,
                                    Callback call) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseURL).newBuilder();
        urlBuilder.addPathSegment(querySegment);

        String query = "";
        query = addParamToQuery(query, "title", title);
        if(authors != null){
            for(String author: authors){
                query = addParamToQuery(query, "au", author);
            }
        }

        query = addParamToQuery(query, "abs", summary);
//        query = addParamToQuery(query, "co", comment);
//        query = addParamToQuery(query, "jr", journalReference);
        query = addParamToQuery(query, catKey, category);
//        query = addParamToQuery(query, "rn", reportNumber);
        query = addParamToQuery(query, "id", id);
//        query = addParamToQuery(query, "all", all);

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
