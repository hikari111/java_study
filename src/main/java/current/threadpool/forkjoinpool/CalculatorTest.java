package current.threadpool.forkjoinpool;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;

/**
 * @Author: wulonghuai
 * @Description: 计算测试，四种方式的计算测试
 * @Date: 2020/3/21 4:53 下午
 */
@Slf4j
public class CalculatorTest {

    long[] numbers = LongStream.rangeClosed(1, 10000000).toArray();

    @Test
    public void testForLoopCalculator() {
        Instant start = Instant.now();
        Calculator calculator = new ForLoopCalculator();
        long result = calculator.sumUp(numbers);
        Instant end = Instant.now();
        System.out.println("testForLoopCalculator");
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result);
    }

    @Test
    public void testExecutorServiceCalculator() {
        Instant start = Instant.now();
        Calculator calculator = new ExecutorServiceCalculator();
        long result = calculator.sumUp(numbers);
        Instant end = Instant.now();
        System.out.println("testExecutorServiceCalculator");
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result);
    }

    @Test
    public void testForkJoinCalculator() {
        Instant start = Instant.now();
        Calculator calculator = new ForkJoinCalculator();
        long result = calculator.sumUp(numbers);
        Instant end = Instant.now();
        System.out.println("testForkJoinCalculator");
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result);
    }

    @Test
    public void testJDK8Parallel() {
        Instant start = Instant.now();
        long result = LongStream.rangeClosed(0, 10000000L).parallel().reduce(0, Long::sum);
        Instant end = Instant.now();
        System.out.println("testJDK8Parallel");
        System.out.println("耗时：" + Duration.between(start, end).toMillis() + "ms");
        System.out.println("结果为：" + result); // 打印结果500500
    }

}
