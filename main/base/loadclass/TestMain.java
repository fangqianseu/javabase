package base.loadclass;

import cn.hutool.core.util.ReflectUtil;

/**
 * @author qianfang, at 2022/2/5, 11:46 AM
 **/
public class TestMain {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException {
        doParentLoad();
//        doBrokenParentLoad();
    }

    private static void doBrokenParentLoad() throws ClassNotFoundException, NoSuchMethodException {
        String path = "/Users/qianfang/workshop/javaProject/javabase/target/classes";
        MyBrokenParentClassLoader c1 = new MyBrokenParentClassLoader(path);
        Class<?> aClass = c1.loadClass("base.loadclass.Hello", false);
        System.out.println(aClass.hashCode());
        Object o = ReflectUtil.newInstance(aClass);
        ReflectUtil.invoke(o, aClass.getMethod("sayHello", null));
    }

    private static void doParentLoad() throws ClassNotFoundException, NoSuchMethodException {
        String path = "/Users/qianfang/workshop/javaProject/javabase/target/classes";
        MyParentClassLoader cl1 = new MyParentClassLoader(path);
        Class<?> c1 = cl1.loadClass("base.loadclass.Hello");
        System.out.println(c1.hashCode());
        Object o = ReflectUtil.newInstance(c1);
        ReflectUtil.invoke(o, c1.getMethod("sayHello", null));

        MyParentClassLoader cl2 = new MyParentClassLoader(path);
        Class<?> c2 = cl2.loadClass("base.loadclass.Hello");
        System.out.println(c2.hashCode());

        Class<?> c3 = cl1.loadClass("base.loadclass.Hello");
        System.out.println("c1 == c2? " + c1.equals(c2));
        System.out.println("c1 == c3? " + c1.equals(c3));
    }
}
