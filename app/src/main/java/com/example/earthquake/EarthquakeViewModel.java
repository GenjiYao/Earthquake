package com.example.earthquake;

import android.app.Application;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.earthquake.DAO.EarthquakeDatabaseAccessor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EarthquakeViewModel extends AndroidViewModel {
    private static  final String TAG = "EarthquakeUpdate";
    private LiveData<List<Earthquake>> earthquakes;
    public EarthquakeViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Earthquake>> getEarthquakes() {
        if (earthquakes == null) {
            earthquakes = EarthquakeDatabaseAccessor
                    .getInstance(getApplication())
                    .earthquakeDAO()
                    .loadAllEarthquakes();
            loadEarthquakes();
        }
        return earthquakes;
    }
    public void loadEarthquakes(){
        /* params , progress, result*/
        new AsyncTask<Void, Void, List<Earthquake>>(){
            @Override
            protected List<Earthquake> doInBackground(Void... voids) {
                ArrayList<Earthquake> earthquakes = new ArrayList<>(0);
                //获取URL
                URL url;
                try{
                    String quakeFeed = getApplication().getString(R.string.earthquake_feed);
                    url = new URL(quakeFeed);
                    URLConnection connection = url.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection)connection;
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK){
                        InputStream inputStream = httpConnection.getInputStream();
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        DocumentBuilder db= dbf.newDocumentBuilder();

                        //解析数据
                        Document dom = db.parse(inputStream);
                        Element docEle = dom.getDocumentElement();
                        NodeList nl = docEle.getElementsByTagName("entry");
                        if (nl != null && nl.getLength() > 0){
                            for (int i = 0; i < nl.getLength(); i++) {
                                //检查我们的加载是否已经取消，如果已经取消，则返回目前为止已经加载的数据
                                if (isCancelled()){
                                    Log.d(TAG, "Loading Cancelled");
                                    return earthquakes;
                                }
                                Element entry = (Element)nl.item(i);
                                Element id = (Element)entry.getElementsByTagName("id").item(0);
                                Element title = (Element)entry.getElementsByTagName("title").item(0);
                                Element g = (Element)entry.getElementsByTagName("georss:point").item(0);
                                Element when = (Element)entry.getElementsByTagName("updated").item(0);
                                Element link = (Element)entry.getElementsByTagName("link").item(0);

                                String idString = id.getFirstChild().getNodeValue();
                                String details = title.getFirstChild().getNodeValue();
                                String hostname = "http://earthquake.usgs.gov";
                                String linkString = hostname + link.getAttribute("href");
                                String point =g.getFirstChild().getNodeValue();
                                String dt = when.getFirstChild().getNodeValue();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                                Date qdate = new GregorianCalendar(0,0,0).getTime();
                                try {
                                    qdate = sdf.parse(dt);
                                }catch (ParseException pe){
                                    Log.e(TAG, "Data Parsing exception.", pe);
                                }

                                String[] location = point.split(" ");
                                Location l = new Location("dummyGPS");
                                l.setLatitude(Double.parseDouble(location[0]));
                                l.setLongitude(Double.parseDouble(location[1]));
                                String magnitudeString = details.split(" ")[1];
                                int end = magnitudeString.length() - 1;
                                double magnitude = Double.parseDouble(magnitudeString.substring(0, end));
                                if (details.contains("-")) {
                                    details = details.split("-")[1].trim();
                                } else {
                                    details = "";
                                }
                                final Earthquake earthquake = new Earthquake(idString, qdate, details, l, magnitude, linkString);
                                earthquakes.add(earthquake);
                            }
                        }
                    }
                    httpConnection.disconnect();
                }catch (MalformedURLException e){
                    Log.e(TAG, "MalformedURLException", e);
                } catch (IOException e){
                    Log.e(TAG, "IOException", e);
                } catch (ParserConfigurationException e) {
                    Log.e(TAG, "ParserConfigurationException", e);
                } catch (SAXException e) {
                    Log.e(TAG, "SAXException e", e);
                }

                EarthquakeDatabaseAccessor
                        .getInstance(getApplication())
                        .earthquakeDAO()
                        .insertEarthquakes(earthquakes);

                return earthquakes;
            }

            @Override
           protected void onPostExecute(List<Earthquake> data) {
//                earthquakes.setValue(data);
           }
        }.execute();
    }
}
