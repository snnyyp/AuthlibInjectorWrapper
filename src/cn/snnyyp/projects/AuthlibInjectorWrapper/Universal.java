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

    public static void checkUpdate(){
        //检查版本更新
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String latestVersion = String.valueOf(Constants.publishVersion);// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL("https://api.snnyyp.cn/updateCheck/AuthlibInjectorWrapper/Version.html");
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                }
                latestVersion = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        //如果有更新，则提示用户
        if (Integer.parseInt(latestVersion) > Constants.publishVersion){
            System.out.println("The current version of AuthlibInjector Wrapper is outdated");
            System.out.println("Please visit https://github.com/MRdeveloper123/AuthlibInjectorWrapper/releases to download the latest build");
        }else{
            System.out.println("Congratulations, you are using the latest version of AuthlibInjector Wrapper");
        }
    }
}
