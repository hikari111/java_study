package current.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import sun.jvm.hotspot.runtime.Threads;

@Slf4j
public class ThreadStatus {

    public static void main(String[] args) throws InterruptedException {
        log.info("线程的所有状态");
        showAllThreadState();
        log.info("单线程案例：创建、可运行状态、终止");
        singleThreadState();
        log.info("其他都是等待状态：Wait、Time_Wait、Block");
        log.info("Wait: Object.wait with no timeout\n" +
                "Thread.join with no timeout\n" +
                "LockSupport.par");
        log.info("Time_Waiting: Thread.sleep\n" +
                "Object.wait with timeout\n" +
                "Thread.join with timeout\n" +
                "LockSupport.parkNanos\n" +
                "LockSupport.parkUntil");
        log.info("Block: 没有进入同步块的代码");
    }

    private static void singleThreadState() throws InterruptedException {
        Thread thread = new Thread(()->{});
        log.info("new thread: {}", thread.getState());
        thread.start();
        log.info("thread start: {}", thread.getState());
        thread.join(); // 进程主线程等待thread线程执行完成
        log.info("thread after join: {}", thread.getState());
    }

    private static void showAllThreadState() {
        StringBuffer sb = new StringBuffer();
        for (Thread.State value : Thread.State.values()) {
            sb.append(value.toString());
            sb.append(" ");
        }
        log.info("Thread.State: {}", sb.toString());
    }
}
