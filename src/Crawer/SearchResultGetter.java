package Crawer;

import template.Album;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import template.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchResultGetter{
    String keyWord;
    Connection connection;
    Connection.Response response;
    Document document;
    int nowPage;
    public int maxPage;
    List<String> albumNames;
    List<String> albumImgs;
    List<String> albumUrls;
    List<String> albumPages;
    final String search_url_head = "https://wnacg.net/albums-index-page-";
    final String search_url_mid = "-sname-";
    final String search_url_tail = ".html";
    final int reTryTimes = 5;

    public SearchResultGetter(String word) throws IOException {
        keyWord = word;
        nowPage = 1;
        getterInit();
        if(response==null){
            System.out.println(word +"getter build failed because internet error");
            return;
        }
        containerInit();
        connectToWeb();
        getTargetWeb();
        getOnePageWebContent();
        System.out.println("getter for "+word+" build successfully");
    }

    private void containerInit(){
        albumImgs = new ArrayList<>();
        albumUrls = new ArrayList<>();
        albumPages = new ArrayList<>();
        albumNames = new ArrayList<>();
    }

    private String targetUrlBuilder(int page){
        StringBuilder res = new StringBuilder();
        res.append(search_url_head);
        res.append(page);
        res.append(search_url_mid);
        res.append(keyWord);
        res.append(search_url_tail);
        return res.toString();
    }

    private void connectToWeb(){
        connection = Jsoup.connect(targetUrlBuilder(nowPage));
    }

    private void getTargetWeb() throws IOException {
        int tryTimes = 0;
        response = Connecter.excute(connection.method(Connection.Method.GET).timeout(50000).ignoreHttpErrors(true));
    }

    private void getOnePageWebContent() throws IOException {
        document = response.parse();
        Elements albums = document.getElementsByClass("li");
        for(Element e:albums){
            albumNames.add(e.getElementsByTag("a").get(1).text());
            albumPages.add(e.getElementsByAttributeValue("class","info_col").get(0).text());
            albumUrls.add(e.getElementsByTag("a").get(1).attributes().get("href"));
            albumImgs.add(e.getElementsByTag("img").get(0).attributes().get("src"));
        }
    }

    private void getMaxPage() throws IOException {
        document = response.parse();
        Elements leftPaginator = document.getElementsByClass("f_left paginator");
        if(leftPaginator.size()==0){
            maxPage=0;
            System.out.println("find nothing");
            return;
        }
        Element lp = leftPaginator.get(0);
        if (lp.childrenSize() == 1) {
            maxPage = 1;
        } else {
            maxPage = string2Int(lp.getElementsByIndexEquals(lp.childrenSize() - 2).text());
        }
        System.out.println("maxpage:"+maxPage);
    }

    private int string2Int(String s){
        int res=0;
        for(char c:s.toCharArray()){
            res *= 10;
            res += (c-'0');
        }
        return res;
    }

    private void getterInit() throws IOException {
        nowPage = 1;
        connectToWeb();
        getTargetWeb();
        if(response==null){
            return;
        }
        getMaxPage();
    }

    /*
    * 展示所有相册的内容
    * */
    public void showAlbumsStatus(){
        for(int i=0;i<albumNames.size();i++){
            System.out.println(albumNames.get(i)+albumPages.get(i)+albumImgs.get(i)+albumUrls.get(i));
        }
    }

    /*
    * 获得一个相册
    * */
    public Album getAlbumByIndex(int index){
        if(index-1 > albumUrls.size()){
            System.out.println("error:out of index");
            return null;
        }
        Album a = new Album(albumUrls.get(index-1),albumNames.get(index-1),albumPages.get(index-1),albumImgs.get(index-1));
        return a;
    }

    /*
     * 转到第page页
     * @param page
     * */
    public void turnToPage(String p) throws IOException {
        containerInit();
        int page = string2Int(p);
        if(page != nowPage){
            nowPage = page;

            if(nowPage>=maxPage){
                nowPage=maxPage;
            }
            connectToWeb();
            getTargetWeb();
        }
        getOnePageWebContent();
        System.out.println("now is page "+page+", has "+albumUrls.size()+" album");
    }

    /*
    * 获得一个页面
    *
    * */
    public Page getPage(){
        List<Album> albumList = new ArrayList<>();
        for(int i=1;i<=albumUrls.size();i++){
            albumList.add(getAlbumByIndex(i));
        }
        return new Page(albumList);
    }
}

