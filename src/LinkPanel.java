import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class LinkPanel extends JPanel {
    private static final JLabel ipHint = new JLabel("ip地址：");
    public static final JTextField ipInput = new JTextField("localhost");
    public static final JTextArea infHint = new JTextArea("等待操作...\n");
    private static final JButton listenBtn = new JButton("接收");
    private static final JButton connectBtn = new JButton("连接");
    private static final JButton startGame = new JButton("开始游戏！");
    private static final JTextField nameChg = new JTextField("Mr.CCC");
    private static final JButton setName = new JButton("改名");
    public static volatile Boolean linkFlag = false;//volatile关键字可以指定本变量修改时立即存入主存，从而实现更好的并行操作
    public static void run(final JFrame f, final int width, final int height){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setTitle("选择网络连接");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(new Dimension(width, height));
                f.setResizable(false);
                f.setLayout(new FlowLayout());
                f.repaint();
                f.setLocation(new Point(600, 200));

                ipInput.setPreferredSize(new Dimension(250, 100));

                listenBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Vars.net.listen(ipInput.getText());
                        System.out.println("已连接到" + ipInput.getText()+"，正在等待玩家连接......");
                    }
                });
                connectBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("寻找主机...");
                        Vars.net.connect(ipInput.getText());
                        System.out.println("连接成功");

                    }
                });

                startGame.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Vars.gameMode = Vars.GAMEMODE_PVP;
                        Vars.net.initial();
                        GoBang.run(Vars.GAMEMODE_PVP);
                        f.dispose();
                    }
                });

                JPanel nameChgPane = new JPanel(new FlowLayout());

                setName.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Vars.P1Name = nameChg.getText();
                        System.out.println("成功，现在的名字是："+Vars.P1Name);

                    }
                });

                nameChgPane.add(nameChg);
                nameChgPane.add(setName);

                nameChg.setPreferredSize(new Dimension(170, 20));
                nameChgPane.setBounds(100,100,200,60);
                nameChgPane.setVisible(true);

                infHint.setEditable(false);
                infHint.setPreferredSize(new Dimension(250, 100));

                startGame.setVisible(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("检测连接线程开始");
                        while (!linkFlag) {
                            Thread.onSpinWait();
                        }
                        System.out.println("连接成功，将显示开始游戏按钮");
                        startGame.setVisible(true);
                    }
                }).start();

                f.add(nameChgPane);
                f.add(ipHint);
                f.add(ipInput);
                f.add(listenBtn);
                f.add(connectBtn);
                f.add(infHint);
                f.add(startGame);

                f.setVisible(true);
            }
        });



    }
    public static void run(){
        run(new JFrame(), 300, 400);
    }


    public static void main(String[] args) {
        run(new JFrame(), 300, 400);
    }
}
