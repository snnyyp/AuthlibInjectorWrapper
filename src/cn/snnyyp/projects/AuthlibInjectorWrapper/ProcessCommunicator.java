package cn.snnyyp.projects.AuthlibInjectorWrapper;

import java.io.*;


interface ProcessCommunicator{
    boolean isAlive();//返回程序是否还活着
    String readLine();//从标准输出读取一行
    void writeLine(String data);//向标准输入写入一行
    void destroy();//释放
}


class LinuxProcessCommunicator implements ProcessCommunicator{
    Process process;
    InputStream inputStream;//输入流
    OutputStream outputStream;//输出流
    BufferedReader bufferedReader;//缓存读
    BufferedWriter bufferedWriter;//缓存写

    LinuxProcessCommunicator(String cmd){
        //初始化方法，启动程序
        try{
            Runtime runtime = Runtime.getRuntime();
            this.process = runtime.exec(cmd);

            //输入流 / 输出流
            this.inputStream = this.process.getInputStream();
            this.outputStream = this.process.getOutputStream();

            //缓存读 / 缓存写
            this.bufferedReader = new BufferedReader(
                    new InputStreamReader(this.inputStream, "UTF-8")//Linux用UTF-8编码
            );
            this.bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(this.outputStream)
            );
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean isAlive(){
        return this.process.isAlive();
    }

    public String readLine(){
        try {
            return bufferedReader.readLine();
        }catch (IOException e){
            e.printStackTrace();
            return "Failed to read a line from buffer";//发生异常则提示
        }
    }

    public void writeLine(String data){
        try {
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void destroy(){
        try {
            this.bufferedWriter.close();
            this.bufferedReader.close();
            this.inputStream.close();
            this.outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            this.process.destroy();
        }
    }
}


class WindowsProcessCommunicator extends LinuxProcessCommunicator{
    WindowsProcessCommunicator(String cmd){
        super(cmd);
        try{
            this.bufferedReader = new BufferedReader(
                    new InputStreamReader(this.inputStream, "GBK")//Windows用GBK编码
            );
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
