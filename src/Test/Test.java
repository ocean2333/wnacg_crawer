package Test;

import Console.Console;
import Crawer.*;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.useSystemProxies","true");
        Console c = new Console();
        c.work();
        /*SearchResultGetter c = new SearchResultGetter("as109");
        for(int i=1;i<=c.maxPage;i++){
            c.turnToPage(i);
            Downloader.downloadThisAlbum(c.getAlbumByIndex(3));
        }*/
        //c.showAlbumsStatus();
        //Downloader.downloadThisAlbum("/photos-index-aid-96742.html","123124");
    }

}
