import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class GoBang {

    private static final Font myFont = new Font("宋体", Font.ITALIC, 16);
    private static final Font chineseFont = new Font("宋体", Font.PLAIN, 16);

    private static final JPanel northBar = new JPanel();//北侧栏，用于显示状态以及备用，并且用于调试
    private static final JPanel stateBar = new JPanel();//南侧栏，用于放置一个状态栏，显示”谁在哪下了棋子“等信息
    private static final JPanel controlBar = new JPanel();//西侧栏，用于放置操作按钮，悔棋、认输、重新开始、退出游戏等
    public static final ContactPanel contactBar = new ContactPanel(new GridLayout(12,1), false);//东侧栏，用于交流和备用

    public static final JLabel downState = new JLabel("Mr.CCC ");//TODO 用于显示右下角信息，包含下棋下到哪一步等等
    public static final JLabel upState = new JLabel("Mr.CCC ");//TODO 用于显示左上角信息，显示游戏模式和玩家信息
    public static final JLabel p1Inf = new JLabel(Vars.P1Name);
    public static final JLabel p2Inf = new JLabel(Vars.P2Name);
    public static final JLabel separator1 = new JLabel("  |  ");
    public static final JLabel separator2 = new JLabel("  |  ");

    private static final JButton undoBtn = new JButton("悔棋");
    private static final JButton surrenderBtn = new JButton("认输");
    private static final JButton restartBtn = new JButton("重新开始");
    private static final JButton exitBtn = new JButton("退出游戏");
    public static final JButton replayBtn = new JButton("复盘");
    private static final JButton timeBtn = new JButton("计时模式");

    //调试用按钮
    private static final JButton forceEnd = new JButton("强制结束游戏");
    private static final JButton stepsChg = new JButton("步数增加224");
    private static final JLabel test = new JLabel("Mr.CCC");

    //游戏途中产生的信息
    public static final int BLACK = 0;
    public static final int SPACE = -1;
    public static final int WHITE = 1;
/*    public static int steps = 0;//步数
    public static boolean winState = false;//是否胜利*/

    //游戏界面，棋盘以及下棋
    public static final ChessPanel centre = new ChessPanel(null, false);


    //音乐线程
    private static final Thread musThread = new Thread(new Runnable() {//背景音乐播放
        @Override
        public void run() {
            Music.play("res/bgmusic/MareMaris.wav", 0.25, Music.LOOP);
        }
    });
    //复盘线程

    private static final Music snd = new Music();

    public static void run(final JFrame f, final int width, final int height, int mode){
        //mode为模式选择
        Vars.gameMode = mode;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setTitle("Mr.CCC");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(new Dimension(width, height));
                f.setResizable(false);
                //musThread.start();
                test.setVisible(true);
                setDownState();
                setGame(f);
                setUpState(mode);
            }
        });

    }

    public static void setUpState(int mode) {
        if (mode == Vars.GAMEMODE_TEST) {
            Vars.gameType = "单机模式";
            Vars.p1GameInf += "玩家1: " + Vars.P1Name + "  胜场: " + Vars.P1WinNum;
            Vars.p2GameInf += "玩家2: " + Vars.P2Name + "  胜场: " + Vars.P2WinNum;
        }
        else if (mode == Vars.GAMEMODE_PVE) {
            Vars.gameType = "人机对战";
        }
        //人人对战则调用
        System.out.println(Vars.P2Name);

        upState.setText(Vars.gameType);
        p1Inf.setText(Vars.p1GameInf);
        p2Inf.setText(Vars.p2GameInf);

    }

    private static void setDownState() {
        downState.setFont(myFont);
        downState.setHorizontalAlignment(SwingConstants.RIGHT);

        stateBar.setLayout(new BorderLayout());
        stateBar.add(downState);
    }

    private static void setGame(JFrame f) {
        f.setLayout(new BorderLayout(3,3));

        //对调试用按钮的设置，实际游戏中将禁用这些按钮
        forceEnd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameOver();
                //musThread.stop();
            }
        });

        stepsChg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vars.steps+=224;
                System.out.println("成功，现在的步数为："+Vars.steps);
            }
        });

        //对controlBar的设置
        controlBar.setLayout(new GridLayout(2,1));
        //悔棋
        undoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Vars.gameIsOver) {
                    if (Vars.gameMode == Vars.GAMEMODE_TEST){
                        if (Vars.steps > 0) {
                            String type;
                            if (Vars.steps % 2 == 1) type = "黑棋";
                            else type = "白棋";
                            int res = JOptionPane.showConfirmDialog(new JOptionPane(), "你是" + type + "，你真的要悔棋吗？ "
                                    , "悔棋？", JOptionPane.YES_NO_OPTION);
                            System.out.println(res);
                            if (res == 0) {
                                undo();
                            }
                        }
                    }
                    else if (Vars.gameMode == Vars.GAMEMODE_PVE){

                    }
                    else if (Vars.gameMode == Vars.GAMEMODE_PVP){
                        Vars.net.sendUndo();
                        ContactPanel.showArea.append("悔棋请求已发送，等待回应...\n");
                    }
                    else JOptionPane.showMessageDialog(new JOptionPane(), "什么也没有，你悔个什么劲呢？？？", "？？？", JOptionPane.ERROR_MESSAGE);
                }
                else JOptionPane.showMessageDialog(new JOptionPane(), "游戏已经结束，请开始下一局游戏", "异常", JOptionPane.ERROR_MESSAGE);
            }
        });
        //投降
        surrenderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Vars.gameIsOver) {
                    if (Vars.gameMode == Vars.GAMEMODE_TEST){
                        int res = JOptionPane.showConfirmDialog(new JOptionPane(), "大丈夫，你真的要投降？ "
                                , "投降？", JOptionPane.YES_NO_OPTION);
                        System.out.println(res);
                        if (res == 0){
                            System.out.println("Surrender");
                            centre.GameOver(Vars.steps%2);
                        }
                    }
                    else if (Vars.gameMode == Vars.GAMEMODE_PVE){

                    }
                    else if (Vars.gameMode == Vars.GAMEMODE_PVP){
                        int res = JOptionPane.showConfirmDialog(new JOptionPane(), "大丈夫，你真的要投降？ "
                                , "投降？", JOptionPane.YES_NO_OPTION);
                        System.out.println(res);
                        if (res == 0){
                            System.out.println("Surrender");
                            centre.GameOver(Vars.steps%2);
                            Vars.net.sendSurrender();
                            ContactPanel.showArea.append("你已投降\n");
                        }
                    }
                }else JOptionPane.showMessageDialog(new JOptionPane(), "游戏已经结束，请开始下一局游戏", "异常", JOptionPane.ERROR_MESSAGE);
            }
        });
        //重开
        restartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Vars.gameMode == Vars.GAMEMODE_TEST){
                    int res = JOptionPane.showConfirmDialog(new JOptionPane(), "要重新开始对局吗？ ",
                            "重开？", JOptionPane.YES_NO_OPTION);
                    System.out.println(res);
                    if (res == 0){
                        restart();
                    }
                }
                else if (Vars.gameMode == Vars.GAMEMODE_PVE){

                }
                else if (Vars.gameMode == Vars.GAMEMODE_PVP){
                    Vars.net.sendRestart();
                    ContactPanel.showArea.append("重开请求已发送，等待回应...\n");
                }
            }
        });
        //退出
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Vars.gameMode == Vars.GAMEMODE_TEST){
                    int res = JOptionPane.showConfirmDialog(new JOptionPane(), "如果你离开游戏，你的对手将胜利，并且你将返回选择界面。",
                            "离开游戏并返回选关界面？", JOptionPane.YES_NO_OPTION);
                    System.out.println(res);
                    if (res == 0){
                        f.dispatchEvent(new WindowEvent(f,WindowEvent.WINDOW_CLOSING));//TODO 返回，现在是点击按钮关闭
                        HelloWindow.run(new HelloWindow(), 1024, 768);
                    }
                }
                else if (Vars.gameMode == Vars.GAMEMODE_PVE){

                }
                else if (Vars.gameMode == Vars.GAMEMODE_PVP){
                    int res = JOptionPane.showConfirmDialog(new JOptionPane(), "如果你离开游戏，你的对手将胜利，并且你将返回选择界面。",
                            "离开游戏并返回选关界面？", JOptionPane.YES_NO_OPTION);
                    System.out.println(res);
                    if (res == 0){
                        Vars.net.sendEnemyExit();
                        f.dispatchEvent(new WindowEvent(f,WindowEvent.WINDOW_CLOSING));//TODO 返回，现在是点击按钮关闭
                        HelloWindow.run(new HelloWindow(), 1024, 768);
                    }
                }
            }
        });
        //复盘
        replayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int res = JOptionPane.showConfirmDialog(new JOptionPane(), "复盘可以看到你上一盘游戏中所有行动。"
                        , "是否进行复盘？", JOptionPane.YES_NO_OPTION);
                System.out.println(res);
                if (res == 0){
                    //如果你选择投降，那么游戏直接结束，对方会收到通知
                    System.out.println("replay");
                    replayBtn.setVisible(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            centre.replay();
                        }
                    }).start();
                }
            }
        });

        JPanel gridPanel = new JPanel(new GridLayout(5,1,3,3));

        gridPanel.add(undoBtn);
        gridPanel.add(surrenderBtn);
        gridPanel.add(restartBtn);
        gridPanel.add(exitBtn);
        gridPanel.add(replayBtn);
        controlBar.add(gridPanel);

        JPanel timerPanel = new JPanel(new BorderLayout());

        timeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Vars.gameMode == Vars.GAMEMODE_TEST){
                    int res = JOptionPane.showConfirmDialog(new JOptionPane(), "如果开启计时模式，你们的每一步棋都将限制时间，时间耗尽则直接失败。 ",
                            "开启计时模式？", JOptionPane.YES_NO_OPTION);
                    System.out.println(res);
                    if (res == 0){
                        Vars.timeModeFlag = true;
                    }
                }
                else if (Vars.gameMode == Vars.GAMEMODE_PVE){

                }
                else if (Vars.gameMode == Vars.GAMEMODE_PVP) {
                    Vars.net.sendTimeModeRequest();
                    ContactPanel.showArea.append("计时模式开启请求已发送，等待回应...\n");
                }
            }
        });

        timerPanel.add(timeBtn, BorderLayout.NORTH);
        timerPanel.add(Vars.myTimer, BorderLayout.CENTER);
        controlBar.add(timerPanel);

        replayBtn.setVisible(false);

        //contactBar.setLayout(new GridLayout(5,1));

        northBar.setLayout(new FlowLayout());
        upState.setFont(chineseFont);
        upState.setHorizontalTextPosition(JLabel.LEFT);
        p1Inf.setFont(chineseFont);
        p1Inf.setHorizontalTextPosition(JLabel.LEFT);
        p2Inf.setFont(chineseFont);
        p2Inf.setHorizontalTextPosition(JLabel.LEFT);
        separator1.setFont(chineseFont);
        separator1.setHorizontalTextPosition(JLabel.LEFT);
        separator2.setFont(chineseFont);
        separator2.setHorizontalTextPosition(JLabel.LEFT);

        northBar.add(upState);
        northBar.add(separator1);
        northBar.add(p1Inf);
        northBar.add(separator2);
        northBar.add(p2Inf);
        //northBar.add(forceEnd);
        //northBar.add(stepsChg);

        northBar.setPreferredSize(new Dimension(800,30));
        controlBar.setPreferredSize(new Dimension(100,600));
        //contactBar.setPreferredSize(new Dimension(100,600));
        stateBar.setPreferredSize(new Dimension(800,30));

        f.add(northBar, BorderLayout.NORTH);
        f.add(stateBar, BorderLayout.SOUTH);
        f.add(controlBar, BorderLayout.WEST);
        f.add(contactBar, BorderLayout.EAST);
        f.add(centre, BorderLayout.CENTER);



        f.setVisible(true);
    }

    private static void setTimer(){

    }

    public static void undo() {
        if (Vars.steps != 0){
            Vars.steps--;
            Vars.chessPos[Vars.history.get(Vars.history.size() - 1).indexOfX][Vars.history.get(Vars.history.size() - 1).indexOfY].type = GoBang.SPACE;
            System.out.println(Vars.history.size());
            Vars.history.remove(Vars.history.size() - 1);//TODO 越界？
            centre.repaint();
            System.out.println("Undo");
        }
    }

    public static void requestUndo(){
        if (Vars.steps != 0){//如果棋子是对方颜色，自己只撤一步，对方也一步
            if (Vars.steps % 2 == Vars.CHESS_TYPE){
                Vars.steps -= 2;
                Vars.chessPos[Vars.history.get(Vars.history.size() - 1).indexOfX][Vars.history.get(Vars.history.size() - 1).indexOfY].type = GoBang.SPACE;
                Vars.history.remove(Vars.history.size() - 1);
                Vars.chessPos[Vars.history.get(Vars.history.size() - 1).indexOfX][Vars.history.get(Vars.history.size() - 1).indexOfY].type = GoBang.SPACE;
                Vars.history.remove(Vars.history.size() - 1);
                centre.repaint();
            }
            else {//如果当前棋子为自己颜色，自己一下撤两步，对方也两步
                Vars.steps--;
                Vars.chessPos[Vars.history.get(Vars.history.size() - 1).indexOfX][Vars.history.get(Vars.history.size() - 1).indexOfY].type = GoBang.SPACE;
                System.out.println(Vars.history.size());
                Vars.history.remove(Vars.history.size() - 1);
                centre.repaint();
            }
            System.out.println("Undo");
        }
    }

    public static void listenUndo(){
        if (Vars.steps != 0){//如果棋子是对方颜色，自己只撤一步，对方也一步
            if (Vars.steps % 2 == Vars.CHESS_TYPE){
                Vars.steps--;
                Vars.chessPos[Vars.history.get(Vars.history.size() - 1).indexOfX][Vars.history.get(Vars.history.size() - 1).indexOfY].type = GoBang.SPACE;
                Vars.history.remove(Vars.history.size() - 1);
                System.out.println(Vars.history.size());
                centre.repaint();
            }
            else {//如果当前棋子为自己颜色，自己一下撤两步，对方也两步
                Vars.steps -= 2;
                Vars.chessPos[Vars.history.get(Vars.history.size() - 1).indexOfX][Vars.history.get(Vars.history.size() - 1).indexOfY].type = GoBang.SPACE;
                Vars.history.remove(Vars.history.size() - 1);
                Vars.chessPos[Vars.history.get(Vars.history.size() - 1).indexOfX][Vars.history.get(Vars.history.size() - 1).indexOfY].type = GoBang.SPACE;
                Vars.history.remove(Vars.history.size() - 1);
                centre.repaint();
            }
            System.out.println("Undo");
        }
    }

    public static void restart() {
        Vars.gameIsOver = false;
        replayBtn.setVisible(false);
        centre.initial();
        centre.repaint();
        System.out.println("Restart");
    }

    //游戏结束
    public static void GameOver() {
        //musThread.stop();
        replayBtn.setVisible(true);
        Vars.gameIsOver = true;
    }


    public static void run(int mode){
        run(new JFrame(), 955, 638, mode);//调试时默认使用调试模式
    }


    public static void main(String[] args) {
        run(new JFrame(), 955, 638, Vars.GAMEMODE_TEST);//调试时默认使用调试模式
    }

    public static void timeModeStart() {
        Vars.timeModeFlag = true;
        restart();
    }
}
