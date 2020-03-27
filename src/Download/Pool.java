package Download;

import template.Work;

import java.util.*;

public class Pool {
    private static volatile Deque<Work> workQueue = new ArrayDeque<>();
    private static volatile List<DownloadThread> downloadingWorkQueue = new LinkedList<>();

    public static void addWork(Work work){
        workQueue.offerLast(work);
    }

    public static Work getWork(){
        if(workQueue.isEmpty()){
            return null;
        }
        Work w = workQueue.getFirst();
        workQueue.pollFirst();
        return w;
    }

    public static void addThread(DownloadThread thread){
        downloadingWorkQueue.add(thread);
    }

    private static DownloadThread getThread(int threadNum){
        if(downloadingWorkQueue.isEmpty()){
            return null;
        }
        for(DownloadThread d:downloadingWorkQueue){
            if(d.threadNum==threadNum){
                return d;
            }
        }
        return null;
    }

    public static void interruptThreadByNum(int tn){
        DownloadThread dt = Pool.getThread(tn);
        if(dt==null){
            System.out.println("error: Thread "+ tn +" not found");
        }else{
            dt.interrupt();
            downloadingWorkQueue.remove(dt);
        }
    }

    public static void removeThread(DownloadThread dt){
        downloadingWorkQueue.remove(dt);
    }

    public static void showThread(){
        for(DownloadThread dt:downloadingWorkQueue){
            System.out.println("Thread: "+dt.threadNum+" name: "+dt.w.getA().getName());
        }
    }
}
