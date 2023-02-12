package cn.snnyyp.project.authlibinjectorwrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
        ConfigStruct config = null;
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
        if (config.print_welcome_title) {
            System.out.println("===========================================");
            System.out.println("| Thanks for using AuthlibInjectorWrapper |");
            System.out.println("===========================================");
        }
        // 打印系统信息
        if (config.print_system_detail) {
            System.out.println("System details:");
            Util.printlnf("    Operating system: %s", SystemInformation.getOsName());
            Util.printlnf("    Current directory: %s", System.getProperty("user.dir"));
            Util.printlnf("    Current JVM binary path: %s", SystemInformation.getCurrentJvmBinaryPath());
            Util.printlnf("    JVM bit: %sBit", SystemInformation.getJvmBit());
            Util.printlnf("    Machine free memory: %sMB", SystemInformation.getMachineFreeMemory());
            Util.printlnf("    Machine total memory: %sMB", SystemInformation.getMachineTotalMemory());
            Util.printlnf("    Current JVM argument: %s", SystemInformation.getJvmArgument());
        }
        // 初始化参数列表
        List<String> processBuilderCommand = new ArrayList<>();
        // 根据重载类型进行参数构造
        switch (config.override_type) {
            case command:
                // command重载
                // 直接全部加入
                processBuilderCommand.addAll(config.override_launch_command);
                break;
            case script:
                // script重载
                // Windows加入cmd /c
                // 其它系统就加入bash
                processBuilderCommand.addAll(
                        SystemInformation.isWindows()
                                ? Arrays.asList("cmd", "/c")
                                : Collections.singletonList("bash")
                );
                // 如果配置文件的override_launch_script_path是空的
                // 提示并退出，返回码3
                if (config.override_launch_script_path.isEmpty()) {
                    System.out.println("[AuthlibInjectorWrapper]: An severe error occurred, the program will exit immediately");
                    System.out.println("[AuthlibInjectorWrapper]: 'override_launch_script_path' cannot be empty");
                    System.exit(3);
                }
                // 如果override_launch_script_path指向的文件不存在
                // 提示并退出，返回码4
                if (!new File(config.override_launch_script_path).exists()) {
                    System.out.println("[AuthlibInjectorWrapper]: An severe error occurred, the program will exit immediately");
                    System.out.println("[AuthlibInjectorWrapper]: Override launch script does not exist");
                    System.exit(4);
                }
                // script存在，加入参数
                processBuilderCommand.add(config.override_launch_script_path);
                break;
            default:
                // 两个都不是，非法参数
                // 提示并退出，返回码5
                System.out.println("[AuthlibInjectorWrapper]: An severe error occurred, the program will exit immediately");
                System.out.println("[AuthlibInjectorWrapper]: 'override_type' can only be 'command' or 'script'");
                System.exit(5);
        }
        // 构造ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(processBuilderCommand).inheritIO();
        // 打印重载参数
        Util.printlnf("[AuthlibInjectorWrapper]: Override type: %s", config.override_type);
        Util.printlnf(
                "[AuthlibInjectorWrapper]: Override argument: %s",
                config.override_type == ConfigStruct.OverrideType.command
                        ? config.override_launch_command
                        : config.override_launch_script_path
        );
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
