/*
Date: 05/09,2019, 16:49
简单的回调函数
*/
package util;

import java.util.ArrayList;
import java.util.List;

public class Callback {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();

        new caller(new callback<List>() {
            @Override
            public void func(List list) {
                list.add("hahahahah...");
            }
        }).run(list);

        System.out.println(list);
    }

    /**
     * 具体的回调函数借口
     *
     * @param <T>
     */
    interface callback<T> {
        public void func(T t);
    }

    /**
     * 具体的 执行回调函数的 caller
     *
     * @param <T>
     */
    static class caller<T> {
        private callback<T> callback;

        caller(callback<T> callback) {
            this.callback = callback;
        }

        public void run(T t) {
            System.out.println("begin...");

            callback.func(t);

            System.out.println("end...");
        }
    }
}
