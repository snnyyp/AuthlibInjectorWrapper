package cn.snnyyp.project.authlibinjectorwrapper;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 程序入口
 *
 * @author snnyyp
 */
public final class Main {

    public static void main(String[] args) throws InterruptedException {
        // 初始化配置文件，如果不存在则导出默认配置文件
        System.out.println("[AuthlibInjectorWrapper]: Initializing config file");
        try {
            Util.initConfig();
        } catch (IOException e) {
            // 初始化出现异常，直接退出程序，返回码1
            System.out.println("[AuthlibInjectorWrapper]: An severe error occurred, the program will exit immediately");
            e.printStackTrace();
            System.exit(1);
        }
        // 初始化无误，读取配置文件
        Map<String, Object> config = new HashMap<>(8);
        try {
            config = Util.readConfig();
        } catch (IOException e) {
            // 读取配置文件出现异常，直接退出程序，返回码2
            System.out.println("[AuthlibInjectorWrapper]: An severe error occurred, the program will exit immediately");
            e.printStackTrace();
            System.exit(2);
        }
        // 读取配置文件无误，程序正式开始
        //
        // 打印欢迎语
        if ((boolean) config.get("print_welcome_title")) {
            System.out.println("===========================================");
            System.out.println("| Thanks for using AuthlibInjectorWrapper |");
            System.out.println("===========================================");
        }
        // 打印系统信息
        if ((boolean) config.get("print_system_detail")) {
            System.out.println("System details:");
            Util.printlnf("\tOperating system: %s", SystemInformation.getOsName());
            Util.printlnf("\tCurrent JVM binary path: %s", SystemInformation.getCurrentJvmBinaryPath());
            Util.printlnf("\tJVM bit: %sBit", SystemInformation.getJvmBit());
            Util.printlnf("\tMachine free memory: %sMB", SystemInformation.getMachineFreeMemory());
            Util.printlnf("\tMachine total memory: %sMB", SystemInformation.getMachineTotalMemory());
            Util.printlnf("\tJVM arguments: %s", SystemInformation.getJvmArgs());
        }
        // 最终的启动命令
        // {java_binary_path} {jvm_argument} -javaagent:{authlib_injector_path}={yggdrasil_url} -jar {server_jar} {server_jar_argument}
        List<String> commandStringList = new ArrayList<>();
        // 配置java_binary_path
        String configJavaBinaryPath = ((String) config.get("java_binary_path")).trim();
        if (configJavaBinaryPath.isEmpty()) {
            // 如果java_binary_path未被配置，则使用当前正在运行本jar的JVM的路径
            commandStringList.add(SystemInformation.getCurrentJvmBinaryPath());
        } else {
            // 如果java_binary_path被配置了，则使用配置文件里的
            commandStringList.add(configJavaBinaryPath);
        }
        // 配置Java虚拟机参数
        String configJvmArgument = ((String) config.get("jvm_argument")).trim();
        if ("%default%".equals(configJvmArgument)) {
            // 如果配置文件的jvm_argument是%default%，则使用面板默认传入的参数
            commandStringList.add(StringUtils.join(SystemInformation.getJvmArgs(), StringUtils.SPACE));
        } else {
            // 反之，则使用配置文件中的参数
            commandStringList.add(configJvmArgument);
        }
        // 配置AuthlibInjector
        String configAuthlibInjectorPath = ((String) config.get("authlib_injector_path")).trim();
        if (!configAuthlibInjectorPath.isEmpty()) {
            // 如果authlib_injector_path非空，则设置该参数，反之则不设置
            String configYggdrasilUrl = ((String) config.get("yggdrasil_url")).trim();
            if (configYggdrasilUrl.isEmpty()) {
                // 设置了authlib_injector_path，但是没设置yggdrasil_url
                // 这是不行的
                // 直接退出程序，返回码3
                System.out.println("[AuthlibInjectorWrapper]: An severe error occurred, the program will exit immediately");
                System.out.println("[AuthlibInjectorWrapper]: You have to set 'yggdrasil_url' if you set 'authlib_injector_path'");
                System.exit(3);
            } else {
                // 设置了authlib_injector_path，且设置了yggdrasil_url
                commandStringList.add(
                        String.format("-javaagent:%s=%s", configAuthlibInjectorPath, configYggdrasilUrl)
                );
            }
        }
        // 配置服务端核心
        String configServerJar = ((String) config.get("server_jar")).trim();
        if (configServerJar.isEmpty()) {
            // 未配置server_jar
            // 这是不行的
            // 直接退出程序，返回码4
            System.out.println("[AuthlibInjectorWrapper]: An severe error occurred, the program will exit immediately");
            System.out.println("[AuthlibInjectorWrapper]: You have to set 'server_jar'");
            System.exit(4);
        } else {
            // 配置了server_jar
            // 和server_jar_argument一起加入启动命令
            String configServerJarArgument = ((String) config.get("server_jar_argument")).trim();
            commandStringList.add("-jar");
            commandStringList.add(configServerJar);
            commandStringList.add(configServerJarArgument);
        }
        // 移除列表中的空格和空字符串
        commandStringList.removeIf(StringUtils.SPACE::equals);
        commandStringList.removeIf(StringUtils.EMPTY::equals);
        // 构造ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(commandStringList).inheritIO();
        // 打印启动命令
        Util.printlnf("[AuthlibInjectorWrapper]: Startup command: %s", processBuilder.command());
        // 准备完一切工作后sleep 5秒，如果用户发现不对劲可以及时停止
        System.out.println("[AuthlibInjectorWrapper]: Server will start in 5 seconds...");
        Thread.sleep(5 * 1000);
        // 启动程序
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            // 启动程序失败
            // 直接退出程序，返回码5
            System.out.println("[AuthlibInjectorWrapper]: An severe error occurred, the program will exit immediately");
            System.out.println("[AuthlibInjectorWrapper]: Failed to start the process");
            e.printStackTrace();
            System.exit(5);
        }
        // 打印程序状态码
        Util.printlnf(
                "[AuthlibInjectorWrapper]: Process exited with status code %s",
                process.waitFor()
        );
    }
}
