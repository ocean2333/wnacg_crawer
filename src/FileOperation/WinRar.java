package FileOperation;

import Settings.Setting;

import java.io.File;

public class WinRar {
    public static String rarPath = "D:\\WinRAR\\WinRAR.exe";
    public static void setRarPath(String path){
        File winrar = new File(path);
        if(winrar.isFile()){
            rarPath = path;
        }else{
            System.out.println("winrar path error");
        }
    }

    /*
    * 使用winrar解压文件
    * @param winrarfile 需要解压的文件
    * @param folder 目标文件夹
    * */
    public static boolean winrar(String winrarfile, String folder) {

        File winrarFile=new File(rarPath);
        if(winrarFile.isFile()&&winrarFile.exists()){
            String cmd="";
            cmd = Setting.winrarPath + " x " + "\"" +winrarfile + "\"" + " "+ "\"" + folder + "\"";
            try {
                Process proc = Runtime.getRuntime().exec(cmd);
                if (proc.waitFor() != 0) {
                    if (proc.exitValue() == 0) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
