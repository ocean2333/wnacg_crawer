package Console;

import Crawer.Downloader;
import Crawer.SearchResultGetter;
import Download.Pool;
import Download.WorkThread;
import FileOperation.Deleter;
import Settings.Setting;
import template.Album;
import template.Page;
import template.Work;

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
    * search xxx/s x 搜索xxx并展示第一页
    * turnto x/tt x 展示当前搜索内容的第x页
    * download x/d x 下载第x个
    * download x,y,z 下载若干个 （没搞好）
    * download x-z 下载两个数（含自身）之间的
    * shutdown/sd 停止
    * cleanzip/cz 清除下载目录下所有zip （测试中）
    * delete xxx 删除下载目录下的xxx （测试中）
    * md x 多线程下载（测试中）
    * dt x
    * st
    * da
    * */
    public void work() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        WorkThread wt = new WorkThread();
        wt.start();

end:        while (true){
            System.out.println("请输入指令");
            String command = br.readLine();

            String commandHead = command.split(" ")[0];
            String commandBody;

            switch (commandHead){
                case "s":
                case "search":
                    commandBody = command.split(" ")[1];
                    search(commandBody);
                    break;
                case "tt":
                case "turnto":
                    commandBody = command.split(" ")[1];
                    turnto(commandBody);
                    break;
                case "d":
                case "download":
                    commandBody = command.split(" ")[1];
                    if(commandBody.contains("-")){
                        int s = string2Int(commandBody.split("-")[0]);
                        int e = string2Int(commandBody.split("-")[1]);
                        for(int i = s; i<=e; i++){
                            download(Integer.toString(i));
                        }
                    }else{
                        download(commandBody);
                    }
                    break;
                case "cz":
                case "cleanzip":
                    File f;
                    if(Setting.hasDownloadPath){
                        f = new File(Setting.downloadPath+"\\");
                    }else{
                        f = new File("\\");
                    }
                    for(File df: Objects.requireNonNull(f.listFiles())){
                        if(df.getName().endsWith(".zip")){
                            System.out.println(df.getName()+" cleaned");
                            df.delete();
                        }
                    }
                    break;
                case "delete":
                    commandBody = command.split(" ")[1];
                    Deleter.delete(commandBody);
                    break;
                case "md":
                    commandBody = command.split(" ")[1];
                    if(commandBody.contains("-")){
                        int s = string2Int(commandBody.split("-")[0]);
                        int e = string2Int(commandBody.split("-")[1]);
                        for(int i = s; i<=e; i++){
                            Work w = new Work(searcher.getPage().getAlbumList().get(i-1));
                            Pool.addWork(w);
                            System.out.println(w.getA().getName()+" was added to pool");
                        }
                    }else{
                        Work w = new Work(searcher.getPage().getAlbumList().get(string2Int(commandBody)-1));
                        Pool.addWork(w);
                        System.out.println(w.getA().getName()+" was added to pool");
                    }
                    break;
                case "dt":
                    commandBody = command.split(" ")[1];
                    Pool.interruptThreadByNum(string2Int(commandBody));
                    break;
                case "st":
                    Pool.showThread();
                    break;
                case "sd":
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
