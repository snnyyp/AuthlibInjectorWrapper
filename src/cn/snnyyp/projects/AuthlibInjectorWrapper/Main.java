package cn.snnyyp.projects.AuthlibInjectorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        Universal.initConf();//初始化配置文件，如果不存在则用默认配置创建一个
        Map config = Universal.ReadConfig();//读取配置文件

        //配置文件中设置为true，打印欢迎语
        if ((boolean) config.get("PrintWelcomeTitle")) {
            System.out.println(Constants.welcomeTitle);
        }

        //检查版本更新
        //System.out.println("Checking updates...");
        //Universal.checkUpdate();
        //System.out.println("------");

        //打印一些系统信息
        if ((boolean) config.get("PrintSystemDetail")) {
            System.out.println("System details:");
            System.out.println("Operating System: " + SystemInformation.getOsName());
            System.out.println("Java binary home: " + SystemInformation.getJavaBinaryHome());
            System.out.println("JVM bit: " + SystemInformation.getJvmBit() + "Bit");
            System.out.println("Machine free memory: " + SystemInformation.getMachineFreeMemory() + "MB");
            System.out.println("Machine total memory: " + SystemInformation.getMachineTotalMemory() + "MB");
            System.out.println("Java virtual machine arguments: " + SystemInformation.getJvmArgs());
        }

        //构造命令字符串ServerJarArguments
        //String cmd = "{JavaBinaryHome} {JvmArguments} -javaagent:{AuthlibInjectorPath}={YggdrasilUrl} -jar {ServerJar} {ServerJarArguments}";
        List<String> cmd = new ArrayList<String>();

        //配置JavaBinaryHome
        if (((String) config.get("JavaBinaryHome")).trim().equals("")) {//连Java都不配置，想啥
            System.out.println("Missing Java binary home");
            System.exit(0);
        } else {
            cmd.add(
                    (String) config.get("JavaBinaryHome")
            );
        }

        //配置Java虚拟机参数
        if (((String) config.get("JvmArguments")).trim().equals("")) {//如果配置文件的Jvm参数为空，则使用面板默认传入的参数
            cmd.add(
                    Universal.listToString(SystemInformation.getJvmArgs())
            );
        } else {//如果配置文件的Jvm的参数不为空，则使用配置文件中的参数
            cmd.add(
                    (String) config.get("JvmArguments")
            );
        }

        //配置AuthlibInjector
        if (!config.get("AuthlibInjectorPath").equals("")) {//如果AuthlibInjector的路径为空，则不设置该参数，反之设置改参数
            if (((String) config.get("YggdrasilUrl")).trim().equals("")) {//既然有配置了AuthlibInjector路径了，怎么能不配置Yggdrasil的Url呢
                System.out.println("You have to configure the YggdrasilUrl if you set the AuthlibInjectorPath");
                System.exit(0);
            } else {
                cmd.add(
                        String.format("-javaagent:%s=%s", config.get("AuthlibInjectorPath"), config.get("YggdrasilUrl"))
                );
            }
        }


        if (((String) config.get("ServerJar")).trim().equals("")) {
            //配置服务端核心
            if (((String) config.get("ServerJar")).trim().equals("")) {//怎么能不配置服务端核心路径呢
                System.out.println("You have to configure the ServerJar");
                System.exit(0);
            } else {
                cmd.add(
                        String.format("-jar %s %s", config.get("ServerJar"), config.get("ServerJarArguments"))
                );
            }
        } else {
            //1.18.2 启动脚本
            //@user_jvm_args.txt @libraries/net/minecraftforge/forge/1.18.2-%FORGE_VERSION%/win_args.txt
            String system_type;
            if (SystemInformation.isWindows()) {
                system_type = "win_args";
            } else {
                system_type = "unix_args";
            }
            cmd.add(
                    String.format("@user_jvm_args.txt @libraries/net/minecraftforge/forge/1.18.2-%s/%s.txt %s", config.get("FORGE_VERSION"), system_type, config.get("ServerJarArguments"))
            );
        }

        //打印启动命令
        System.out.println("Startup command: " + Universal.listToString(cmd));

        //准备完一切工作后休息5秒，如果用户发现不对劲可以及时停止
        System.out.println("Server will start after 5 seconds...");
        Thread.sleep(5000);

        //构造ProcessCommunicator
        ProcessCommunicator processCommunicator;
        if (SystemInformation.isWindows()) {
            processCommunicator = new WindowsProcessCommunicator(Universal.listToString(cmd));
        } else {
            processCommunicator = new LinuxProcessCommunicator(Universal.listToString(cmd));
        }

        //启动标准输出线程
        StdoutWatchDog stdoutWatchDog = new StdoutWatchDog(processCommunicator);
        stdoutWatchDog.start();

        //启动监视服务端是否已经关闭线程
        ExitWatchDog exitWatchDog = new ExitWatchDog(processCommunicator);
        exitWatchDog.start();

        while (processCommunicator.isAlive()) {
            String i = Universal.input();
            processCommunicator.writeLine(i);
        }
    }
}
