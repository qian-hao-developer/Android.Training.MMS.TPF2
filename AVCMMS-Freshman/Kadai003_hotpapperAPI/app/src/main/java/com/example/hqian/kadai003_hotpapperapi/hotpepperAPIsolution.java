package com.example.hqian.kadai003_hotpapperapi;

import android.util.Xml;
import android.widget.ArrayAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by hqian on 16/06/24.
 */
public class hotpepperAPIsolution {

    public ArrayList<RssItem> getRssData(String urlString){
        ArrayList<RssItem> item = null;
        InputStream inputStream;
        HttpURLConnection conn = null;

        try{
            URL url = new URL(urlString);
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            inputStream = conn.getInputStream();

            if (inputStream != null){
                item = parseXmlContent(inputStream);
                inputStream.close();
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return item;
    }

    public ArrayList<RssItem> parseXmlContent(InputStream inputStream){
        ArrayList<RssItem> rssItemList = new ArrayList<RssItem>();
        XmlPullParser parser = null;
        try{
            parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");

            String rssItem = null;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT: break;
                    case XmlPullParser.START_TAG:{

                    }
                    case XmlPullParser.TEXT: break;
                    case XmlPullParser.END_TAG: break;
                    case XmlPullParser.END_DOCUMENT: break;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return  rssItemList;
    }

}
