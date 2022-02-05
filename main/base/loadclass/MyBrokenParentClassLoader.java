package base.loadclass;


import cn.hutool.core.io.FileUtil;

/**
 * @author qianfang, at 2022/2/5, 11:14 AM
 **/
public class MyBrokenParentClassLoader extends ClassLoader {

    private String path;

    public MyBrokenParentClassLoader(String path) {
        this.path = path;
    }

    // 如果需要打破双亲加载机制 只需要重写 loadClass 即可
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        System.out.println("load " + name);
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();

                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    if (name.startsWith("base.loadclass")) {
                        c = findClass(name);
                    } else {
                        c = this.getParent().loadClass(name);
                    }

                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    // 自定义的 findClass 方法也是需要的
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("load" + name);
        String path = this.path + "/" + name.replace('.', '/').concat(".class");
        if (path != null) {
            byte[] bytes = FileUtil.readBytes(path);
            return defineClass(name, bytes, 0, bytes.length);
        }
        return null;

    }
}
