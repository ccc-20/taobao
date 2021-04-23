package com.example.baekhyun.utils;

public class UrlUtiles {
    public static String createHomePagerUrl(int id, int page) {
        return "discovery/" + id + "/" + page;
    }

    public static String getCoverPath(String pict_url) {
        return "https:" + pict_url;
        //+"_"+size+"×"+size+".jpg"
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http") || url.startsWith("https")) {
            return url;
        } else {
            return "https:" + url;
        }
    }

    public static String getContentUrl(int targetId) {
        return "recommend/" + targetId;
    }

    public static String getSpecialUrl(int mpage) {
        return "onSell/" + mpage;
    }
}
