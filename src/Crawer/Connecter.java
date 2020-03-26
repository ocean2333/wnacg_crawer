package Crawer;

import org.jsoup.Connection;

public class Connecter {
    public static Connection.Response excute(Connection c){
        Connection.Response r=null;
        int count=1;
        while(count<5){
            try{
                r = c.execute();
                if (r.statusCode()==200||r.statusCode()==202){
                    break;
                }
                System.out.println("failed: status code "+r.statusCode());
            }catch (Exception e){
                System.out.println("failed: connection error at "+count+" time(s)");
            }
            count++;
        }
        return r;
    }
}
