# java_study
Java基础相关的学习

依赖：

* lombok-maven
* lombok插件

包结构分类

没有分类就先用temp
有分类确认分类和命名

问题记录：

[java中boolean类型占几个字节](https://juejin.im/post/5d539d1e6fb9a06b26508e87)
boolean只表示两种情况true或者false所以按照道理用最小计算机单位比特就可以了，所以是1bit吗？不是
但由于系统进行运算的时候，会取整进行字节运算，8位作为一字节，32位，64位计算机都是如此，以8位倍数，才是最省资源的。所以是1btyte吗？不是
java虚拟机在设计的时候使用int来存储boolean，所以boolean占用的字节是4b和int相同。
而当作为boolean[]的时候，里面的值就是1字节来存储的，这边是有区别的，也有专业的文章佐证。

[JAVA中为什么Map桶（链表）长度超过8才转为红黑树](https://blog.csdn.net/stuqbx/article/details/88897621?utm_source=distribute.pc_relevant.none-task)
大家都知道HashMap中的数据存储结构是链表+数组，但为什么在8位的时候要将链表转换为红黑树呢？
按道理来说在key值随机均匀的情况下出现碰撞的概率会低很多，符合泊松分布，作者进行了概率统计验证，能出现到8的概率为0.000006%几乎不会发生。
但是为了避免key无法随机分布的情况下导致hashmap的性能受到影响，阈值8是时间和空间的权衡，是根据概率统计决定的
代价就是用空间来换时间，树形节点的空间占用是链表节点的两倍。

[TreeMap中的键，值能否为null？](https://blog.csdn.net/u012156116/article/details/81073570)
TreeMap由于有排序功能，所以传入的对象的key必须实现Comparable接口或者Comparator接口，但一般默认情况下的基础数据类型有自己实现的接口，
但其中肯定不会对null进行处理，所以默认传递null会报空指针异常，但是实际上我们自己实现Comparable接口或者Comparator接口的时候，判断空的操作给定一个默认值，就允许为空的情况下
但null的值会如何被处理？直接获取null是获取不到的，但是可以用遍历的方式获取数据

[取模和取余一样吗](https://baike.baidu.com/item/MOD/2317040)
去模在都是正数的情况下是一样的，但为负数的情况下就有差别，取模是向0取整，取余是想负无穷取整。导致计算有差别。
但通俗的将就是求被整除后的整数余数，hashmap的下标计算方式就是通过hashcode对数组的长度取余，获取从0开始到长度减1的下标

[String、StringBuilder、StringBuffer的区别](https://zhuanlan.zhihu.com/p/27324173)
不可变类 可变线程安全   可变线程不安全
StringBuilder和StringBuffer都继承自抽象类AbstractStringBuilder,区别就在于Builder对方法加了锁，而Buffer没有加锁。
所以在单线程环境下StringBuffer的性能最好。
+ "" 在jvm中编译的时候也会变成StringBuilder
1. 初始长度好重要，值得说四次。
2. Liferay的StringBundler类，先将要放进去的String存在数组中，然后合理计算好长度后，在将其放进sb中
3. 但，还是浪费了一倍的char[]
4. 重用StringBuilder
5. ＋ 与 StringBuilder jvm会进行builder优化
6. StringBuffer 与 StringBuilder 同源，差别在于sync
7. 永远把日志的字符串拼接交给slf4j??，省事而已，实际性能更浪费，不过日志可能是异步的

[HashMap的为什么要做二次hash以及其他相关的问题](https://juejin.im/post/5d5d25e9f265da03f66dc517)
首先明白取模就是hash%length，但是在计算机中，这种运算不如位运算快，所以将其优化成为位运算
也就是说hash%length==hash&(length-1)
长度按照2n进行扩容，为什么是2n次方呢，因为只有2n次方，才能够每一位都能&1 ，会减少hash冲突
但是实际上当length为16的时候，无论hashcode的高位如何变化，最终的地位都是0，已进行取模操作就还是0会出现hash冲突。
所以HashMap会进行一次二次hash，由于hashcode是int值，总共位数是32位4字节，将无符号位右移动16位进行异或运算
为什么要进行异或操作呢？首先低四位一直不变的情况被排除了，但是右移16位后高四位的情况不变的情况有出现了，为了避免这种情况，进行一次异或保证所有不变的情况变得均匀
混合原始哈希码的高位和低位，以此来加大低位的随机性。而且混合后的低位掺杂了高位的部分特征，这样高位的信息也被变相保留下来。使得更加随机[知乎引用](https://www.zhihu.com/question/20733617)

[迭代器和枚举器和for循环和增强for循环](https://blog.csdn.net/qq496013218/article/details/74976992)
[迭代器和for循环快慢](https://www.cnblogs.com/redcoatjk/articles/4863340.html)
[java8的ConcurrentHashMap为何放弃分段锁](https://cloud.tencent.com/developer/article/1509556)

Java线程的状态：
https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html
线程的状态在java中定义了6中：new、runnable、termination、blocked、waiting、timed-waiting
基本状态：new、runable、termination
其他状态分为线程主动和被动：
blocked: 竞争锁失败进入等待队列
waiting: 主动进入等待状态
time-waiting: 主动进入等待状态，设定超时时间 
sleep(timeout) = 不释放锁的wait(timeout)
wait(timeout) = 释放锁的超时（但允许被提前通知唤醒）超时了的化同sleep
join(timeout) = 等待上一个线程处理完成，或者等待处理完成一段时间（内部也是wait和notify）

默认就是锁机制
使用wait和notify就是手动控制同步，或者在默认同步的情况下，实现特殊的调度
https://tech.meituan.com/2018/11/15/java-lock.html

锁的分类：
    乐观锁: 先不锁，乐观认为并发不会操作同一条数据，发生冲突之后，重试或者报错
    悲观锁: 直接锁定，无法并发操作
锁住资源需不需要阻塞
    阻塞：
    不阻塞：
        自旋
        适应性自旋
多个线程竞争同步资源有没有什么差别：
    无锁：不锁住资源，多个线程中只有一个能够修改成功，其他线程会重试
    偏向锁：同一个线执行获取同步资源时候，直接获取资源
    轻量级锁：多线程竞争同步资源的时，没有获取资源的线程自旋等待锁
    重量级锁：多线程竞争同步资源的时，没有获取资源的线程阻塞等待唤醒
多个线程竞争的时候需不需要排队：
    需要：公平锁
    不需要：先尝试插队，插队失败再排队，非公平锁
一个线程的多个流程中能不能获取同一把锁：
    能：可重入锁
    不能：非可重入锁
多线程能不能共享一把锁：
    能：共享锁
    不能：排他锁

