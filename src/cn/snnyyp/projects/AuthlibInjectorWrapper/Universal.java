package cn.snnyyp.projects.AuthlibInjectorWrapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;


public class Universal {
    public static String listToString(List list){
        // 列表转换String，空格作为分隔符，用作拼接JVM参数
        return StringUtils.join(list, " ");
    }

    public static String input(){
        //用途和Python的相同，带你找回Python的感觉，前缀先算了
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void initConf(){
        //初始化配置文件，如果不存在，则使用默认配置创建
        File configFile = new File("AuthlibInjectorWrapper.json");
        if (!configFile.exists()){
            try{
                System.out.println("[AuthlibInjectorWrapper] Config file not found, going to create a new one using default values.");
                configFile.createNewFile();//没有配置文件则创建一个
            }catch (IOException e){
                e.printStackTrace();
            }
            //创建后将jar内默认的配置文件输出
            InputStream inputStream = Universal.class.getResourceAsStream("/conf/AuthlibInjectorWrapper.json");
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                FileWriter fileWriter = new FileWriter("AuthlibInjectorWrapper.json", true);//中文兼容
                String line;
                while ((line = bufferedReader.readLine()) != null){//遍历每行，保证全部写出
                    fileWriter.write(line);
                    fileWriter.write(Constants.lineSeparator);//由于是readLine，每次写了之后还要写一个换行符
                }

                //关闭文件句柄
                fileWriter.close();//close!
                bufferedReader.close();
                inputStream.close();
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static Map ReadConfig(){
        //读取json格式的配置文件，转换成map后返回
        String line;
        StringBuilder configContent = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream("AuthlibInjectorWrapper.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
            while ((line = bufferedReader.readLine()) != null){
                configContent.append(line);//逐行读取并追加
            }
            return (Map)JSON.parse(configContent.toString());
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return new HashMap();
    }
}
