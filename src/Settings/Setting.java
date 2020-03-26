package Settings;

import java.io.*;
import java.util.Properties;

public class Setting {
    public static String winrarPath;
    public static boolean hasWinrar;
    public static String downloadPath;
    public static boolean hasDownloadPath;
    public static String unzipPath;
    public static boolean hasUnzipPath;

    public static void getSettingsInPropetries() throws IOException {
        String path = "setting.properties";
        File f = new File(path);
        if(!f.exists()){
            System.out.println("setting missed,using defult setting");
            winrarPath = new String("");
            hasWinrar = false;
            downloadPath = new String("");
            hasDownloadPath = false;
            unzipPath = new String("");
            hasUnzipPath = false;
        }else{
            Properties properties = new Properties();
            BufferedReader br = new BufferedReader(new FileReader(path));
            properties.load(br);
            winrarPath = properties.getProperty("winrarPath");
            hasWinrar= !"none".equals(winrarPath);
            downloadPath = properties.getProperty("downloadPath");
            hasDownloadPath= !"none".equals(downloadPath);
            unzipPath = properties.getProperty("unzipPath");
            hasUnzipPath= !"none".equals(unzipPath);
        }
    }

    public static void showSettings(){
        System.out.println(winrarPath);
        System.out.println(downloadPath);
        System.out.println(unzipPath);
        System.out.println(hasWinrar);
        System.out.println(hasDownloadPath);
        System.out.println(hasUnzipPath);
    }
}
