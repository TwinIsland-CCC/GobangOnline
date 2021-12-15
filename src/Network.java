import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Network {
    public static int PORT = 9999;
    private Socket s = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;
    public static final String CMD_PUT_CHESS = "@@@";
    public static final String CMD_CONTACT = "###";
    public static final int CMD_LENGTH = 3;
    public void listen(final String ip){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket ss = new ServerSocket(PORT);
                    s = ss.accept();
                    out = new PrintWriter(s.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    LinkPanel.infHint.append("已连接到" + LinkPanel.ipInput.getText()+"，正在等待玩家连接......\n");
                    startReadThread();
                } catch (IOException e) {
                    e.printStackTrace();
                    LinkPanel.infHint.append("连接异常\n");
                }

            }
        }).start();
    }
    public void connect(final String ip){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    s = new Socket(ip, PORT);
                    out = new PrintWriter(s.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    LinkPanel.infHint.append("连接成功\n");
                    startReadThread();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startReadThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        String line = in.readLine();
                        if (line == null){
                            //可以处理读取不到的情况
                            System.out.println("读取失败");
                        }
                        parseMessage(line);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseMessage(String line) {
        System.out.println(line);
        if (line.startsWith(CMD_PUT_CHESS)){
            //下棋指令
            line = line.substring(CMD_LENGTH);
            String[] cmd = line.split(",");
            int row = Integer.parseInt(cmd[0]);
            int col = Integer.parseInt(cmd[1]);

            GoBang.centre.putChess(row, col);

        }
        else if (line.startsWith(CMD_CONTACT)){
            line = line.substring(CMD_LENGTH);
            String output = Vars.P1Name +": "+line;
            GoBang.contactBar.putMessage(output);
        }
    }

    public void sendChess(int row, int col){
        out.println(CMD_PUT_CHESS+row+','+col);
    }
    public void sendMessage(String message){
        out.println(CMD_CONTACT+message);
    }
}
