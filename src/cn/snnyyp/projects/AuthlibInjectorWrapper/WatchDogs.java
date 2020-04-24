package cn.snnyyp.projects.AuthlibInjectorWrapper;


class StdoutWatchDog extends Thread{
    private ProcessCommunicator processCommunicator;

    StdoutWatchDog(ProcessCommunicator processCommunicator){
        this.processCommunicator = processCommunicator;
        this.setDaemon(true);//线程随程序的退出而结束
    }

    @Override
    public void run() {
        while (this.processCommunicator.isAlive()){
            String content = this.processCommunicator.readLine();
            if (!(content == null)){//如果读到的行为空，则不输出
                System.out.println(content);
            }
        }
    }
}

class ExitWatchDog extends Thread{
    private ProcessCommunicator processCommunicator;

    ExitWatchDog(ProcessCommunicator processCommunicator){
        this.processCommunicator = processCommunicator;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true){
            if (!this.processCommunicator.isAlive()){
                break;
            }
            try{
                Thread.sleep(500);//不必时刻检查，咱可以每隔0.5秒检查一次，没什么太大影响
            }catch (InterruptedException e){//应该是Ctrl+C终止了，不过面板服应该不存在，只不过会发signal强制结束进程
                e.printStackTrace();
                break;
            }
        }
        this.processCommunicator.destroy();
        System.exit(0);
    }
}
