package Download;

import Crawer.Downloader;
import template.Work;

import java.io.IOException;

public class DownloadThread extends Thread{
    int threadNum;
    Work w;
    DownloadThread(Work w,int n){
        this.w = w;
        threadNum = n;
    }
    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        super.run();
        try {
            System.out.println(w.getA().getName()+"start to download, threadNum: "+threadNum);
            Downloader.downloadThisAlbum(w.getA());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error in download "+w.getA().getName());
        }
        Pool.removeThread(this);
    }
}
