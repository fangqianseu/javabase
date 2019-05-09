/*
Date: 05/09,2019, 08:42
*/
package zookeeper.base;

import org.apache.zookeeper.data.Stat;

public class StatUtils {
    public static String printStat(Stat stat) {
        if (null == stat) {
            return "";
        }
        return "Stat [czxid=" + stat.getCzxid() + ", mzxid=" + stat.getMzxid() + ", length=" + stat.getDataLength() + ", version=" + stat.getVersion() + "]";
    }
}
