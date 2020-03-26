package Console;

import Crawer.Downloader;
import Crawer.SearchResultGetter;
import FileOperation.Deleter;
import Settings.Setting;
import template.Album;
import template.Page;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Console {
    private static String path;
    private static SearchResultGetter searcher;

    public Console(){

    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        Console.path = path;
    }

    /*
    * 控制台，根据指令执行操作
    * search xxx 搜索xxx并展示第一页
    * turnto x 展示当前搜索内容的第x页
    * download x 下载第x个
    * download x,y,z 下载若干个 （没搞好）
    * download x-z 下载两个数（含自身）之间的 （没搞好）
    * shutdown 停止
    * cleanzip 清除下载目录下所有zip
    * delete xxx 删除下载目录下的xxx
    * */
    public void work() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
end:        while (true){
            System.out.println("请输入指令");
            String command = br.readLine();

            String commandHead = command.split(" ")[0];
            String commandBody;

            switch (commandHead){
                case "search":
                    commandBody = command.split(" ")[1];
                    search(commandBody);
                    break;
                case "turnto":
                    commandBody = command.split(" ")[1];
                    turnto(commandBody);
                    break;
                case "download":
                    commandBody = command.split(" ")[1];
                    download(commandBody);
                    break;
                case "cleanzip":
                    File f;
                    if(Setting.hasDownloadPath){
                        f = new File(Setting.downloadPath+"\\");
                    }else{
                        f = new File("\\");
                    }
                    for(File df: Objects.requireNonNull(f.listFiles())){
                        if(df.isFile()&&df.getName().split(".")[1]=="zip"){
                            df.delete();
                        }
                    }
                    break;
                case "delete":
                    commandBody = command.split(" ")[1];
                    Deleter.delete(commandBody);
                    break;
                case "shutdown":
                    break end;
                default:
                    System.out.println("未知的指令");
            }
        }
    }

    private int string2Int(String s){
        int res=0;
        for(char c:s.toCharArray()){
            res *= 10;
            res += (c-'0');
        }
        return res;
    }

    private void search(String word) throws IOException {
        searcher = new SearchResultGetter(word);
        clearConsole();
        showPage(searcher.getPage());
    }

    private void turnto(String page) throws IOException {
        searcher.turnToPage(page);
        clearConsole();
        showPage(searcher.getPage());
    }

    private void download(String index) throws IOException {
        Downloader.downloadThisAlbum(searcher.getPage().getAlbumList().get(string2Int(index)-1));
        clearConsole();
        showPage(searcher.getPage());
    }

    private void showPage(Page page){
        int count = 1;
        for(Album a:page.getAlbumList()){
            System.out.println(count+": " +a.getName()+" "+a.getInfo());
            count++;
        }
    }

    private static void clearConsole()
    {
        try{
            String os = System.getProperty("os.name");
            if (os.contains("Windows")){
                Runtime.getRuntime().exec("cls");
            }else{
                Runtime.getRuntime().exec("clear");
            }
        }catch (Exception exception){
            //  Handle exception.
        }
    }

}
