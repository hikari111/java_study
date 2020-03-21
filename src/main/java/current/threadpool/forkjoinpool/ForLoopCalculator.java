package current.threadpool.forkjoinpool;

/**
 * @Author: wulonghuai
 * @Description: 遍历实现计算
 * @Date: 2020/3/21 4:57 下午
 */
public class ForLoopCalculator implements Calculator {
    @Override
    public long sumUp(long[] numbers) {
        long rs = 0;
        for (int i = 0; i < numbers.length; i++) {
            rs += numbers[i];
        }
        return rs;
    }
}
