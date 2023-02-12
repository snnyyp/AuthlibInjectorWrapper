package cn.snnyyp.project.authlibinjectorwrapper;

import com.moandjiezana.toml.Toml;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * 工具类
 *
 * @author snnyyp
 */
public final class Util {

    /**
     * 带换行的System.out.printf()
     *
     * @param format 格式
     * @param args   替换参数
     */
    public static void printlnf(String format, Object... args) {
        System.out.printf(format, args);
        System.out.println();
    }

    /**
     * 初始化配置文件
     * 如果不存在则从jar包中提取默认配置文件到当前目录下
     */
    public static void initConfig() throws IOException {
        File configFile = new File("AuthlibInjectorWrapper.toml");
        // 如果配置文件存在，直接return
        if (configFile.exists()) {
            return;
        }
        // 执行到这里，说明配置文件不存在
        // 终端打印提示
        System.out.println("[AuthlibInjectorWrapper]: Config file not found, extracting default config file");
        // 读取jar包内的默认配置文件
        InputStream defaultConfigFileInputStream = Main.class.getResourceAsStream("/AuthlibInjectorWrapper.toml");
        // 如果默认配置文件不存在，抛出IOException异常
        if (defaultConfigFileInputStream == null) {
            throw new IOException("AuthlibInjectorWrapper.jar is broken");
        }
        // 一切无误，提取
        FileUtils.copyInputStreamToFile(defaultConfigFileInputStream, configFile);
    }

    /**
     * 读取配置文件
     *
     * @return 配置文件Map[String, Object]
     */
    public static ConfigStruct readConfig() throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get("AuthlibInjectorWrapper.toml"))) {
            return new Toml()
                    .read(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .to(ConfigStruct.class);
        }
    }
}
