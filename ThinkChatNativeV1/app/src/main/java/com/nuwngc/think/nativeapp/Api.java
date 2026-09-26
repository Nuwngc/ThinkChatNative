package com.nuwngc.think.nativeapp;

import android.content.Context;
import android.content.SharedPreferences;
import okhttp3.*;
import org.json.*;
import java.io.IOException;
import java.util.*;

public final class Api {
    public static final String BASE="https://thinkchat.id.vn";
    private static SharedPreferences prefs;
    private static final CookieJar jar = new CookieJar() {
        public synchronized void saveFromResponse(HttpUrl u, List<Cookie> cookies) {
            for (Cookie c : cookies) if (c.name().equals("sid")) prefs.edit().putString("sid", c.value()).apply();
        }
        public synchronized List<Cookie> loadForRequest(HttpUrl u) {
            String sid=prefs.getString("sid","");
            if(sid.isEmpty()) return Collections.emptyList();
            return Collections.singletonList(new Cookie.Builder().name("sid").value(sid).domain(u.host()).path("/").secure().httpOnly().build());
        }
    };
    public static OkHttpClient client;
    public static void init(Context c){
        if(prefs!=null)return;
        prefs=c.getApplicationContext().getSharedPreferences("thinkchat",Context.MODE_PRIVATE);
        client=new OkHttpClient.Builder().cookieJar(jar).build();
    }
    public static boolean hasSession(){return prefs!=null && !prefs.getString("sid","").isEmpty();}
    public static void clearSession(){prefs.edit().remove("sid").remove("user_json").apply();}
    public static JSONObject request(String method,String path,JSONObject body) throws Exception{
        RequestBody rb=body==null?null:RequestBody.create(body.toString(),MediaType.get("application/json; charset=utf-8"));
        Request.Builder b=new Request.Builder().url(BASE+path).method(method,rb).header("Accept","application/json");
        try(Response r=client.newCall(b.build()).execute()){
            String s=r.body()==null?"{}":r.body().string(); JSONObject j;
            try{j=new JSONObject(s);}catch(Exception e){j=new JSONObject().put("raw",s);}
            if(!r.isSuccessful()) throw new ApiException(r.code(),j.optString("error","HTTP "+r.code()),j.optString("code"));
            return j;
        }
    }
    public static JSONObject get(String p)throws Exception{return request("GET",p,null);}
    public static JSONObject post(String p,JSONObject j)throws Exception{return request("POST",p,j);}
    public static JSONObject patch(String p,JSONObject j)throws Exception{return request("PATCH",p,j);}
    public static JSONObject delete(String p)throws Exception{return request("DELETE",p,null);}
    public static String uploadImage(byte[] bytes,String mime)throws Exception{
        RequestBody rb=RequestBody.create(bytes,MediaType.get(mime));
        Request r=new Request.Builder().url(BASE+"/api/upload").post(rb).header("Accept","application/json").build();
        try(Response x=client.newCall(r).execute()){
            String s=x.body()==null?"{}":x.body().string(); JSONObject j=new JSONObject(s);
            if(!x.isSuccessful())throw new ApiException(x.code(),j.optString("error","Upload lỗi"),j.optString("code"));
            return j.getString("url");
        }
    }
    public static class ApiException extends IOException{public final int code;public final String errorCode;public ApiException(int c,String m,String ec){super(m);code=c;errorCode=ec;}}
    public static void cache(String key,String value){prefs.edit().putString(key,value).apply();}
    public static String cached(String key){return prefs.getString(key,null);}
}
