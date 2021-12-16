import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class GoBang {

    private static final Font myFont = new Font("宋体", Font.ITALIC, 16);
    private static final Font chineseFont = new Font("宋体", Font.PLAIN, 16);


    private static final JButton btn0 = new JButton("0");
    private static final JButton btn1 = new JButton("1");
    private static final JButton btn2 = new JButton("2");
    private static final JButton btn3 = new JButton("3");
    private static final JButton Back = new JButton("Back");

    private static final JPanel northBar = new JPanel();//北侧栏，用于显示状态以及备用，并且用于调试
    private static final JPanel stateBar = new JPanel();//南侧栏，用于放置一个状态栏，显示”谁在哪下了棋子“等信息
    private static final JPanel controlBar = new JPanel();//西侧栏，用于放置操作按钮，悔棋、认输、重新开始、退出游戏等
    public static final ContactPanel contactBar = new ContactPanel(new GridLayout(12,1), false);//东侧栏，用于交流和备用

    public static final JLabel downState = new JLabel("Mr.CCC ");//TODO 用于显示右下角信息，包含下棋下到哪一步等等
    public static final JLabel upState = new JLabel("Mr.CCC ");//TODO 用于显示左上角信息，显示游戏模式和玩家信息

    private static final JButton undoBtn = new JButton("悔棋");
    private static final JButton surrenderBtn = new JButton("认输");
    private static final JButton restartBtn = new JButton("重新开始");
    private static final JButton exitBtn = new JButton("退出游戏");
    public static final JButton replayBtn = new JButton("复盘");

    //调试用按钮
    private static final JButton forceEnd = new JButton("强制结束游戏");
    private static final JButton stepsChg = new JButton("步数增加224");
    private static final JLabel test = new JLabel("Mr.CCC");

    //游戏途中产生的信息
    public static final int BLACK = -1;
    public static final int SPACE = 0;
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

    private static void initial() {


    }

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
                f.setLayout(new BorderLayout(3,3));

                //musThread.start();

                initial();
                test.setVisible(true);

                downState.setFont(myFont);
                downState.setHorizontalAlignment(SwingConstants.RIGHT);

                stateBar.setLayout(new BorderLayout());
                stateBar.add(downState);

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
                controlBar.setLayout(new GridLayout(12,1));
                //悔棋
                undoBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!Vars.gameIsOver) {
                            if (Vars.steps > 0) {
                                String type;
                                if (Vars.steps % 2 == 1) type = "黑棋";
                                else type = "白棋";
                                int res = JOptionPane.showConfirmDialog(new JOptionPane(), "你是" + type + "，你真的要悔棋吗？ "
                                        , "悔棋？", JOptionPane.YES_NO_OPTION);//TODO 如果是人人对战，询问对方是否同意
                                System.out.println(res);
                                if (res == 0) {
                                    //TODO 悔棋
                                    Vars.steps--;
                                    Vars.chessPos[Vars.history.get(Vars.history.size() - 1).indexOfX][Vars.history.get(Vars.history.size() - 1).indexOfY].type = GoBang.SPACE;
                                    System.out.println(Vars.history.size());
                                    Vars.history.remove(Vars.history.size() - 1);//TODO 越界？
                                    centre.repaint();
                                    System.out.println("Undo");

                                }
                            } else
                                JOptionPane.showMessageDialog(new JOptionPane(), "什么也没有，你悔个什么劲呢？？？", "？？？", JOptionPane.ERROR_MESSAGE);
                        }else JOptionPane.showMessageDialog(new JOptionPane(), "游戏已经结束，请开始下一局游戏", "异常", JOptionPane.ERROR_MESSAGE);
                    }
                });
                //投降
                surrenderBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!Vars.gameIsOver) {
                            int res = JOptionPane.showConfirmDialog(new JOptionPane(), "大丈夫，你真的要投降？ "
                                    , "投降？", JOptionPane.YES_NO_OPTION);
                            System.out.println(res);
                            if (res == 0){
                                //TODO 如果你选择投降，那么游戏直接结束，对方会收到通知
                                System.out.println("Surrender");
                                centre.GameOver(Vars.steps%2);
                            }
                        }else JOptionPane.showMessageDialog(new JOptionPane(), "游戏已经结束，请开始下一局游戏", "异常", JOptionPane.ERROR_MESSAGE);
                    }
                });
                //重开
                restartBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {//TODO 只在人机对战有效
                        int res = JOptionPane.showConfirmDialog(new JOptionPane(), "要重新开始对局吗？ ",
                                "重开？", JOptionPane.YES_NO_OPTION);
                        System.out.println(res);
                        if (res == 0){
                            //TODO 重新开始
                            Vars.gameIsOver = false;
                            replayBtn.setVisible(false);
                            centre.initial();
                            centre.repaint();
                            System.out.println("Restart");
                        }
                    }
                });
                //退出
                exitBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int res = JOptionPane.showConfirmDialog(new JOptionPane(), "如果你离开游戏，你的对手将胜利，并且你将返回选择界面。",
                                "离开游戏并返回选关界面？", JOptionPane.YES_NO_OPTION);
                        System.out.println(res);
                        if (res == 0){
                            f.dispatchEvent(new WindowEvent(f,WindowEvent.WINDOW_CLOSING));//TODO 返回，现在是点击按钮关闭
                            HelloWindow.run(new HelloWindow(), 1024, 768);
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
                            //TODO 如果你选择投降，那么游戏直接结束，对方会收到通知
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

                controlBar.add(undoBtn);
                controlBar.add(surrenderBtn);
                controlBar.add(restartBtn);
                controlBar.add(exitBtn);
                controlBar.add(replayBtn);

                replayBtn.setVisible(false);

                //contactBar.setLayout(new GridLayout(5,1));

                northBar.setLayout(new FlowLayout());
                upState.setFont(chineseFont);
                upState.setHorizontalTextPosition(JLabel.LEFT);

                String gameType = "";
                if (mode == Vars.GAMEMODE_TEST) gameType = "单机模式";
                else if (mode == Vars.GAMEMODE_PVE) gameType = "人机对战";
                else if (mode == Vars.GAMEMODE_PVP) gameType = "联机对战";

                System.out.println(Vars.P2Name);

                gameType += "   玩家1：" + Vars.P1Name + "  |  玩家2：" + Vars.P2Name;

                upState.setText(gameType);

                northBar.add(upState);
                northBar.add(forceEnd);
                northBar.add(stepsChg);

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
        });

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

}
