package current.threadpool.forkjoinpool;

/**
 * @Author: wulonghuai
 * @Description: 计算方法的接口
 * @Date: 2020/3/21 4:50 下午
 */
public interface Calculator {

    /**
     * 把传进来的所有numbers 做求和处理
     *
     * @param numbers
     * @return 总和
     */
    long sumUp(long[] numbers);

}
