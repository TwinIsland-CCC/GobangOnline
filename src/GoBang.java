import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class GoBang {

    private static final Font myFont = new Font("Times New Roman", Font.ITALIC, 16);

    private static final JButton btn0 = new JButton("0");
    private static final JButton btn1 = new JButton("1");
    private static final JButton btn2 = new JButton("2");
    private static final JButton btn3 = new JButton("3");
    private static final JButton Back = new JButton("Back");

    private static final JPanel northBar = new JPanel();//北侧栏，用于显示状态以及备用，并且用于调试
    private static final JPanel stateBar = new JPanel();//南侧栏，用于放置一个状态栏，显示”谁在哪下了棋子“等信息
    private static final JPanel controlBar = new JPanel();//西侧栏，用于放置操作按钮，悔棋、认输、重新开始、退出游戏等
    private static final ContactPanel contactBar = new ContactPanel(new GridLayout(12,1), false);//东侧栏，用于交流和备用

    public static final JLabel state = new JLabel("Mr.CCC ");

    private static final JButton undoBtn = new JButton("悔棋");
    private static final JButton surrenderBtn = new JButton("认输");
    private static final JButton restartBtn = new JButton("重新开始");
    private static final JButton exitBtn = new JButton("退出游戏");

    //调试用按钮
    private static final JButton forceEnd = new JButton("强制结束游戏");
    private static final JLabel test = new JLabel("Mr.CCC");

    //游戏途中产生的信息
    public static final int BLACK = -1;
    public static final int SPACE = 0;
    public static final int WHITE = 1;
    public static int steps = 0;//步数
    public static boolean winState = false;//是否胜利

    public static int gameMode;

    //游戏界面，棋盘以及下棋
    private static final ChessPanel centre = new ChessPanel(null, false);



    private static final Thread musThread = new Thread(new Runnable() {//背景音乐播放
        @Override
        public void run() {
            Music.play("res/bgmusic/MareMaris.wav", 0.25, Music.LOOP);
        }
    });

    private static final Music snd = new Music();

    private static void initial() {


    }

    public static void run(final JFrame f, final int width, final int height, int mode){
        //mode为模式选择
        gameMode = mode;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setTitle("Mr.CCC");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(new Dimension(width, height));
                f.setResizable(false);
                f.setLayout(new BorderLayout(3,3));

                musThread.start();

                initial();
                test.setVisible(true);

                state.setFont(myFont);
                state.setHorizontalAlignment(SwingConstants.RIGHT);

                stateBar.setLayout(new BorderLayout());
                stateBar.add(state);

                //对调试用按钮的设置，实际游戏中将禁用这些按钮
                forceEnd.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        GameOver();
                        musThread.stop();
                    }
                });

                //对controlBar的设置
                controlBar.setLayout(new GridLayout(12,1));

                undoBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (ChessPanel.steps > 0){
                            int res = JOptionPane.showConfirmDialog(new JOptionPane(), "你真的要悔棋吗？ "
                                    , "悔棋？", JOptionPane.YES_NO_OPTION);//TODO 如果是人人对战，询问对方是否同意
                            System.out.println(res);
                            if (res == 0){
                                //TODO 悔棋
                                ChessPanel.steps--;
                                ChessPanel.chessPos[ChessPanel.history.get(ChessPanel.history.size() - 1).indexOfX][ChessPanel.history.get(ChessPanel.history.size() - 1).indexOfY].type = GoBang.SPACE;
                                System.out.println(ChessPanel.history.size());
                                ChessPanel.history.remove(ChessPanel.history.size() - 1);//TODO 越界？
                                centre.repaint();
                                System.out.println("Undo");
                            }
                        }
                        else JOptionPane.showMessageDialog(new JOptionPane(), "什么也没有，你悔个什么劲呢？？？", "？？？", JOptionPane.ERROR_MESSAGE);
                    }
                });

                surrenderBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int res = JOptionPane.showConfirmDialog(new JOptionPane(), "大丈夫，你真的要投降？ "
                                , "投降？", JOptionPane.YES_NO_OPTION);
                        System.out.println(res);
                        if (res == 0){
                            //TODO 如果你选择投降，那么游戏直接结束，对方会收到通知
                            System.out.println("Surrender");
                            centre.GameOver();
                        }
                    }
                });

                restartBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {//TODO 只在人机对战有效
                        int res = JOptionPane.showConfirmDialog(new JOptionPane(), "要重新开始对局吗？ ",
                                "重开？", JOptionPane.YES_NO_OPTION);
                        System.out.println(res);
                        if (res == 0){
                            //TODO 重新开始
                            centre.initial();
                            centre.repaint();
                            System.out.println("Restart");
                        }
                    }
                });

                exitBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int res = JOptionPane.showConfirmDialog(new JOptionPane(), "如果你离开游戏，你的对手将胜利，并且你将返回选择界面。",
                                "离开游戏并返回选关界面？", JOptionPane.YES_NO_OPTION);
                        System.out.println(res);
                        if (res == 0){
                            f.dispatchEvent(new WindowEvent(f,WindowEvent.WINDOW_CLOSING));//TODO 返回，现在是点击按钮关闭
                            HelloWindow.run(new JFrame(), 1024, 768);
                        }
                    }
                });
                //
                controlBar.add(undoBtn);
                controlBar.add(surrenderBtn);
                controlBar.add(restartBtn);
                controlBar.add(exitBtn);

                contactBar.setLayout(new FlowLayout());

                northBar.add(forceEnd);

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
    private static void GameOver() {
        musThread.stop();
    }

    public static void main(String[] args) {
        run(new JFrame(), 955, 638, HelloWindow.GAMEMODE_TEST);//调试时默认使用调试模式
    }

}
