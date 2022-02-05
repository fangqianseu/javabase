package base.loadclass;


import cn.hutool.core.io.FileUtil;

/**
 * @author qianfang, at 2022/2/5, 11:14 AM
 **/
public class MyParentClassLoader extends ClassLoader {

    private String path;

    public MyParentClassLoader(String path) {
        this.path = path;
    }


    // 如果需要遵守双亲加载机制 只需要重写 findClass 即可

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = this.path + "/" + name.replace('.', '/').concat(".class");
        if (path != null) {
            byte[] bytes = FileUtil.readBytes(path);
            return defineClass(name, bytes, 0, bytes.length);
        }
        return null;

    }
}
