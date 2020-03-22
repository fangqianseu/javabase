package lambda;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qianfang, at 2019-12-02, 10:16
 *
 * Intermediate：中间 stream不中断
 *  map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、
 *  skip、 parallel、 sequential、 unordered
 *
 * Terminal： 计算结果
 *  forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、
 *  anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
 *
 * Short-circuiting：
 * anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 limit
 **/
public class baseLambda {
    private List<Integer> list = null;
    private Map<Integer, Integer> map = null;

    @Before
    public void before() {
        list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        map = new HashMap<>();
        map.put(1, 2);
        map.put(2, 4);
        map.put(3, 9);
    }

    /**
     * 是否有一个满足 是否全满足
     */
    @Test
    public void anyAndAll() {
        System.out.println(list.stream().anyMatch(x -> x.equals(1)));
        System.out.println(list.stream().allMatch(x -> x > 0));
    }

    /**
     * 不能修改自己包含的本地变量值，
     * 不能用 break/return 之类的关键字提前结束循环。
     */
    @Test
    public void foreach() {
        // list
        list.forEach(System.out::println);

        // map
        map.forEach((k, v) -> System.out.println(String.format("%d , %d", k, v)));
    }

    /**
     * filter()等 返回 stream对象的都为惰性计算
     * count() 等 为立即计算
     */
    @Test
    public void filler() {
        Stream<Integer> stream = list.stream().filter(i -> {
            System.out.println("----" + i);
            return i > 5;
        });
        System.out.println("****before count");
        long count = stream.count();
        System.out.println(count);
    }

    /**
     * map ： 对 stream 中每个元素，进行一次函数变化
     */
    @Test
    public void map() {
        List<Integer> l = list.stream().map(i -> i - 1)
                .collect(Collectors.toList());
        System.out.println(l);
    }

    /**
     * 限制 stream 集合的元素个数
     */
    @Test
    public void limit() {
        list.stream().limit(5).forEach(System.out::println);
    }

    /**
     * 排序 传入的为 comparator 类型
     * distinct() ： 去除重复
     */
    @Test
    public void sort() {
        // 标准比较器
        list.stream().distinct().sorted(Integer::compareTo).
                forEach(System.out::println);

        // 自定义比较器
        list.stream().sorted((x, y) -> x * 2 - y).
                forEach(System.out::println);

        // 指定 key 的比较器
        list.stream().sorted(Comparator.comparing(x -> x, Integer::compareTo)).
                forEach(System.out::println);
    }

    /**
     * 等价于
     * int res = 0;
     * for(int now : list){
     * res = res + now;
     * }
     * return res;
     */
    @Test
    public void reduce() {
        // 0 为初始值， lambda 为 BinaryOperator 二元运算符
        System.out.println(list.stream().reduce(0, (res, now) -> res + now));

        //无初值
        System.out.println(list.stream().reduce(Integer::sum));
    }

    /**
     * flatMap: 将多个 stream 合并为 一个 stream
     */
    @Test
    public void flatMap() {
        List<Object> l = Arrays.asList("hello", "world").stream()
                .flatMap(line -> Stream.of(line.split("")))
                .collect(Collectors.toList());
        System.out.println(l);
    }

    /**
     * collect：
     * 1. 收集 stream 中元素 将其转化为 list、map、set等
     * 2. 将集合元素分类
     * 3. 转化为字符串
     */
    @Test
    public void collect() {
        // 生成 list
        List<Integer> l1 = list.stream().filter(i -> i > 5)
                .collect(Collectors.toList());
        // 生成 map
        list.stream().collect(Collectors.toMap(x -> x, y -> y + 1)).
                forEach((x, y) -> System.out.println(String.format("%d - %d", x, y)));

        // 生成指定类型集合
        List<Integer> l2 = list.stream().filter(i -> i > 5)
                .collect(Collectors.toCollection(LinkedList::new));

        // 给集合元素分类
        Map<Integer, List<Integer>> map = list.stream().collect(
                Collectors.groupingBy(i -> i % 2, Collectors.toList()));
        System.out.println(map);

        // 转化为字符串
        System.out.println(list.stream().filter(x -> x > 3)
                .map(String::valueOf)
                .collect(Collectors.joining("-", "[", "]")));
    }


    /**
     * 统计信息 max min sum count
     */
    @Test
    public void stats() {
        IntSummaryStatistics statistics = list.stream().mapToInt(x -> x).summaryStatistics();
        statistics.getMax();
        statistics.getMin();
        statistics.getSum();
        statistics.getCount();
    }

    /**
     * max、min 函数： 为 map() 和 summaryStatistics() 结合
     */
    @Test
    public void minAndMax() {
        System.out.println(list.stream().max(Integer::compareTo).get());
        System.out.println(list.stream().min(Comparator.comparing(i -> i)).get());
    }


    /**
     * 生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例）
     * 新Stream每个元素被消费的时候都会执行给定的消费函数，并且消费函数优先执行
     * 不影响原来的流
     */
    @Test
    public void peek() {
        System.out.println(list.stream().filter(x -> x > 5)
                .map(x -> x * x)
                .peek(System.out::println)
                .collect(Collectors.toList()));
    }
}
