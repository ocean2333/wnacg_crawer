package Download;

import template.Work;

public class WorkThread extends Thread {
    private volatile int threadNum;

    @Override
    public synchronized void start() {
        super.start();
        threadNum=0;
    }

    @Override
    public void run() {
        super.run();
        while(true){
            Work w = Pool.getWork();
            if(w!=null){
                DownloadThread d = new DownloadThread(w,threadNum+=1);
                Pool.addThread(d);
                d.start();
            }
        }
    }


}
