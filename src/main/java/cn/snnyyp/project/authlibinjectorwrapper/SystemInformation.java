package cn.snnyyp.project.authlibinjectorwrapper;

import com.sun.management.OperatingSystemMXBean;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.List;


/**
 * 获取系统信息
 *
 * @author snnyyp
 */
public final class SystemInformation {

    /**
     * 获取正在运行的JVM的二进制文件路径
     * java(.exe)还在 根目录/bin 下面
     *
     * @return Java二进制文件的路径
     */
    public static String getCurrentJvmBinaryPath() {
        return String.format("%1$s%2$sbin%2$sjava", System.getProperty("java.home"), File.separator);
    }

    /**
     * 获取JVM的位数
     *
     * @return JVM的位数
     */
    public static String getJvmBit() {
        return System.getProperty("sun.arch.data.model");
    }

    /**
     * 获取系统最大内存，单位MB
     *
     * @return 系统最大内存
     */
    public static String getMachineTotalMemory() {
        return String.valueOf(
                ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean())
                        .getTotalPhysicalMemorySize() / (1024 * 1024)
        );
    }

    /**
     * 获取系统空闲内存，单位MB
     *
     * @return 系统空闲内存
     */
    public static String getMachineFreeMemory() {
        return String.valueOf(
                ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean())
                        .getFreePhysicalMemorySize() / (1024 * 1024)
        );
    }

    /**
     * 获取JVM的启动参数，例如-Xmx, -Xms
     *
     * @return JVM的启动参数
     */
    public static List<String> getJvmArgument() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    /**
     * 判断是不是Windows系统
     *
     * @return 是不是Windows系统
     */
    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS");
    }

    /**
     * 获取操作系统的名称
     *
     * @return 操作系统的名称
     */
    public static String getOsName() {
        return isWindows() ? "Windows" : "Non-Windows";
    }
}
