package guava;

import com.google.common.base.Optional;

/**
 * @author qianfang, at 2019-11-11, 23:29
 **/
public class guavaTest {
    public static void main(String[] args) {
        Optional<Integer> one = Optional.of(2);
        System.out.println(one.get());
    }
}
