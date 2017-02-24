package com.example.martinrgb.a22photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by MartinRGB on 2017/2/24.
 */

public class WebFetchr {

    private static final String TAG = "WebFetchr";
    private static final String APP_ID = "4e54787c9f3bf477d7ffef4869e130795667c754096d4fc65d3fd39a76ce42f1";
    //private static final String BASE_URL = "https://api.unsplash.com";
    private static final String BASE_URL = "https://unsplash.it/list";

    //从指定URL获取原始数据，返回字节流数组
    public byte[] getUrlBytes(String urlSpec) throws IOException{
        //接受Url地址
        URL url = new URL(urlSpec);
        //创建链接对象
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //只有在调用getInputStream方法时候，才能真正链接到指定 URL;
            InputStream in = connection.getInputStream();

            //连不上
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead =0;
            byte[] buffer = new byte[1024];
            //用 Read 方法读取网络数据，知道读完
            while ((bytesRead = in.read(buffer)) > 0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            //数据完全取回，关闭网络
            connection.disconnect();
        }
    }

    //将UrlBytes 的结果转化成 String
    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    public void fetchItems() {
        try {
            String url = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            //解析Json数据
            JSONObject jsonBody = new JSONObject(jsonString);
        }catch (JSONException je){
            Log.e(TAG, "Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonObject) throws IOException,JSONException {
        JSONObject photosJsonObject = jsonObject.getJSONObject("");
    }
}
