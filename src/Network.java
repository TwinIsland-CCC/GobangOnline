import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Network {
    private static final String CMD_RESTART = "111";
    private static final String CMD_UNDO = "222";
    private static final String CMD_DO = "&&&";
    private static final String CMD_SURRENDER = "333";
    private static final String CMD_ENEMY_EXIT = "444";
    public static int PORT = 9999;
    private Socket s = null;
    protected PrintWriter out = null;
    protected BufferedReader in = null;
    public static final String CMD_PUT_CHESS = "@@@";
    public static final String CMD_CONTACT = "###";
    private static final String CMD_INITIAL = "$$$";
    private static final String CMD_CONTROL = "%%%";
    public static final int CMD_LENGTH = 3;
    public static int CHESS_TYPE = GoBang.BLACK;//默认黑棋
    public void listen(final String ip){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LinkPanel.infHint.append("已连接到" + LinkPanel.ipInput.getText()+"，正在等待玩家连接......\n");
                    ServerSocket ss = new ServerSocket(PORT);
                    s = ss.accept();
                    out = new PrintWriter(s.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    startReadThread();
                    LinkPanel.linkFlag = true;
                    LinkPanel.infHint.append("连接成功"+"\n你是黑棋，请点击开始游戏，开始对局~");
                    //如果自己是接收端，则是庄家，自己是白棋
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
                    LinkPanel.infHint.append("连接成功"+"\n你是白棋，请点击开始游戏，开始对局~");
                    startReadThread();
                    LinkPanel.linkFlag = true;
                    CHESS_TYPE = GoBang.WHITE;//如果是用户端则是白棋
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

            //GoBang.centre.putChess(row, col);
            GoBang.centre.pvpPutChess(row, col, CHESS_TYPE);

        }
        else if (line.startsWith(CMD_CONTACT)){
            //聊天指令
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String time = df.format(new Date());
            line = line.substring(CMD_LENGTH);
            String output = Vars.P2Name + "[对手]: " + time + "\n" + line;
            GoBang.contactBar.putMessage(output);
        }
        else if(line.startsWith(CMD_INITIAL)){
            line = line.substring(CMD_LENGTH);
            Vars.gameType = "联机对战";
            Vars.P2Name = line;
            System.out.println("成功，P2Name = " + Vars.P2Name);
            String chessType = null;
            if (CHESS_TYPE == GoBang.BLACK) chessType = "黑棋";
            else if (CHESS_TYPE == GoBang.WHITE) chessType = "白棋";
            Vars.gameType += "   我(" + chessType + "): " + Vars.P1Name;
            if (CHESS_TYPE == GoBang.BLACK) chessType = "白棋";
            else if (CHESS_TYPE == GoBang.WHITE) chessType = "黑棋";
            Vars.gameType +="  |  对手(" + chessType + "): "+ Vars.P2Name;
            GoBang.setUpState(Vars.GAMEMODE_PVP);
        }
        else if(line.startsWith(CMD_CONTROL)){
            if (line.substring(CMD_LENGTH).equals(CMD_RESTART)) {
                int res = JOptionPane.showConfirmDialog(new JOptionPane(), "对方请求重新开始，要重新开始对局吗？ ",
                        "重开？", JOptionPane.YES_NO_OPTION);
                System.out.println(res);
                if (res == 0){
                    //重新开始
                    GoBang.restart();
                    sendDoRestart();
                }
            }
            else if (line.substring(CMD_LENGTH).equals(CMD_UNDO)){
                if (Vars.steps > 0) {
                    int res = JOptionPane.showConfirmDialog(new JOptionPane(), "对方请求悔棋，是否同意？"
                            , "悔棋？", JOptionPane.YES_NO_OPTION);
                    System.out.println(res);
                    if (res == 0) {
                        GoBang.undo();
                        sendDoUndo();
                    }
                }
            }
            else if (line.substring(CMD_LENGTH).equals(CMD_SURRENDER)){
                JOptionPane.showMessageDialog(new JOptionPane(), "对方投降，你获胜了");
                GoBang.centre.GameOver(Vars.steps%2);
            }
            else if (line.substring(CMD_LENGTH).equals(CMD_ENEMY_EXIT)){
                JOptionPane.showMessageDialog(new JOptionPane(), "对方离开了游戏，你获胜了");
                GoBang.centre.GameOver(Vars.steps%2);
            }
        }
        else if(line.startsWith(CMD_DO)){
            if (line.substring(CMD_LENGTH).equals(CMD_RESTART)){
                ContactPanel.showArea.append("对方已接受重开申请，游戏即将重新开始...\n");
                GoBang.restart();
            }
            else if (line.substring(CMD_LENGTH).equals(CMD_UNDO)){
                ContactPanel.showArea.append("对方已接受悔棋申请，你已撤回一步\n");
                GoBang.undo();

            }
        }
        else {
            System.out.println("无效的指令，请检查程序");
        }
    }

    public void sendChess(int row, int col){
        out.println(CMD_PUT_CHESS+row+','+col);
    }
    public void sendMessage(String message){
        out.println(CMD_CONTACT+message);
    }

    public void initial(){//用于对局开始时为对方传输我方信息
        out.println(CMD_INITIAL+Vars.P1Name);//传输名字
        //其他可选：段位，胜率，对局数等
    }
    public void sendRestart(){
        out.println(CMD_CONTROL+CMD_RESTART);
    }
    public void sendUndo(){
        out.println(CMD_CONTROL+CMD_UNDO);
    }
    public void sendDoRestart(){
        out.println(CMD_DO+CMD_RESTART);
    }
    public void sendDoUndo(){
        out.println(CMD_DO+CMD_UNDO);
    }

    public void sendSurrender() {
        out.println(CMD_CONTROL+CMD_SURRENDER);
    }

    public void sendEnemyExit() {
        out.println(CMD_CONTROL+ CMD_ENEMY_EXIT);
    }
}
