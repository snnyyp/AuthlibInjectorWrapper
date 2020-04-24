package cn.snnyyp.projects.AuthlibInjectorWrapper;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;


public class SystemInformation {
    public static String getJavaBinaryHome(){
        //获取Java的根目录，java(.exe)还在bin目录下面
        return String.format("%s%sbin%sjava", System.getProperty("java.home"),
                Constants.fileSeparator, Constants.fileSeparator);
    }

    public static String getJvmBit(){
        //获取JVM的位数
        return System.getProperty("sun.arch.data.model");
    }

    public static String getMachineTotalMemory(){
        //获取系统最大内存，单位MB
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return String.valueOf(osmxb.getTotalPhysicalMemorySize() / (1024*1024));
    }

    public static String getMachineFreeMemory(){
        //获取系统空闲内存，单位MB
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return String.valueOf(osmxb.getFreePhysicalMemorySize() / (1024*1024));
    }

    public static List<String> getJvmArgs(){
        //获取JVM的启动参数，例如-Xmx, -Xms
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    public static boolean isWindows(){
        //判断是不是Windows系统，如果是False的话则应当是Linux系统
        return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS");
    }

    public static String getOsName(){
        //返回操作系统的名称
        if (isWindows()){
            return "Windows";
        }else{
            return "Linux";
        }
    }
}
