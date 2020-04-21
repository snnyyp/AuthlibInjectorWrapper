package cn.snnyyp.projects.AuthlibInjectorWrapper;

import java.lang.management.ManagementFactory;
import java.util.List;

public class SystemInformation {
    public static String getJavaBinaryHome(){
        //获取Java的根目录，java(.exe)还在bin目录下面
        return String.format("%s/bin/java", System.getProperty("java.home"));
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
