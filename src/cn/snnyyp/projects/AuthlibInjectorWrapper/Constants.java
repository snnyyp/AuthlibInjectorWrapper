package cn.snnyyp.projects.AuthlibInjectorWrapper;

import java.io.File;

public class Constants {
    //存储一些常量
    public static double currentVersion = 1.1;//当前版本号
    public static int publishVersion = 2;//发行版本，用于检查更新
    public static String author = "Github@MRdeveloper123";//作者
    public static String authorEmail = "mrdeveloper123@outlook.com";//作者的邮箱
    public static String lineSeparator = System.getProperty("line.separator");//当前系统环境下的换行符
    public static String fileSeparator = File.separator;
    public static String welcomeTitle = "" +
            "| . | _ _ _| |_ | |_ | |<_>| |_ | |._ _ <_> ___  ___ _| |_ ___  _ _ \n" +
            "|   || | | | |  | . || || || . \\| || ' || |/ ._>/ | ' | | / . \\| '_>\n" +
            "|_|_|`___| |_|  |_|_||_||_||___/|_||_|_|| |\\___.\\_|_. |_| \\___/|_|  \n" +
            "                                       <__'                         \n" +
            " _ _ _                               \n" +
            "| | | | _ _  ___  ___  ___  ___  _ _ \n" +
            "| | | || '_><_> || . \\| . \\/ ._>| '_>\n" +
            "|__/_/ |_|  <___||  _/|  _/\\___.|_|  \n" +
            "                 |_|  |_|            ";
}
