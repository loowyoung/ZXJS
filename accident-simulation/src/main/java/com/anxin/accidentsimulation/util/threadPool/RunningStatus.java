package com.anxin.accidentsimulation.util.threadPool;
/**
 * 控制线程方法
 * @author zl_xu
 *
 */
public  class RunningStatus {

    private static boolean isRunning = false;
    
    private static int taskCnt = 0 ;
    
    public static boolean getIsRunning(){
        return isRunning;
    }

    public static void addRunningTask(){
        isRunning = true;
        operateRunningTask(1);
    }

    public static void deleteRunningTask(){
        operateRunningTask(-1);
    }
    
    /**
     *此处对于是否在运行的判断不严谨，
     * 如果消费的比产生的快，此时认为已处理完 此情况是bug
     */
    private static synchronized void operateRunningTask(int i){
        taskCnt += i;
        if(taskCnt==0){
            isRunning =false;
        }
    }
}
