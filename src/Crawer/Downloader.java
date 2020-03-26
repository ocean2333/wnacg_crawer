package Crawer;

import FileOperation.*;
import template.Album;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader {
    String albumUrl;
    String downloadUrl;
    String name;
    int downloadPortSelection = 0;
    final String url_head = "https://wnacg.net";
    final String download_url_head = "https://wnacg.net/download-index-aid-";
    final String download_url_tail = ".html";
    final int tryTimes = 5;
    Downloader(String albumUrl,String name){
        this.albumUrl = albumUrl;
        this.name = name;
    }

    public static void downloadThisAlbum(Album album) throws IOException {
        System.out.println("download start: "+album.getName());
        Downloader d = new Downloader(album.getUrl(),album.getName());
        d.getDownloadUrl();
        d.download();
    }

    private void getDownloadUrl(){
        String id="0";
        System.out.println(albumUrl);
        String patten = "\\d+";
        Pattern p = Pattern.compile(patten);
        Matcher m = p.matcher(albumUrl);
        if(m.find()){
            id = m.group(0);
        }else{
            System.out.println("get id error");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(download_url_head);
        sb.append(id);
        sb.append(download_url_tail);
        downloadUrl = sb.toString();

    }

    private void download() throws IOException {
        int tryTimes = 0;
        Connection connection = Jsoup.connect(downloadUrl);
        Connection.Response response =  Connecter.excute(connection.method(Connection.Method.GET).timeout(50000));
        if(response==null){
            return;
        }

        Document document = response.parse();
        Elements es = document.getElementsByClass("down_btn");
        String d = es.get(downloadPortSelection).attributes().get("href");
        connection = Jsoup.connect(d).maxBodySize(1024*1024*1024);
        System.out.println("download begin");
        System.out.println(d);
        response =  Connecter.excute(connection.method(Connection.Method.GET).ignoreHttpErrors(true).ignoreContentType(true).timeout(5000000));
        if(response==null){
            return;
        }
        String zipFileName = name.replace(":"," ")
                .replace("*"," ")
                .replace("?"," ")
                .replace("\""," ")
                .replace("<"," ")
                .replace(">"," ")
                .replace("\\"," ")
                .replace("/"," ")
                .replace("|"," ")
                .replace(":"," ");
        FileOutputStream fos = new FileOutputStream(zipFileName+".zip");
        fos.write(response.bodyAsBytes());
        fos.close();
        WinRar.winrar(zipFileName+".zip",zipFileName+"\\");
        System.out.println("download end");

    }


}
