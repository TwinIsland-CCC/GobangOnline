import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HelloWindow extends JFrame {
    private static final Thread musThread = new Thread(new Runnable() {//背景音乐播放
        @Override
        public void run() {
            Music.play("res/bgmusic/Start.wav", 0.25, Music.LOOP);//音量要求在0-2之间
        }
    });

    private static final JButton offlineMode = new JButton("OfflineMode");
    private static final JButton PVEMode = new JButton("PVEMode");
    private static final JButton PVPMode = new JButton("PVPMode");
    private static final JButton Exit = new JButton("Exit");

    public static final int GAMEMODE_PVE = 1;
    public static final int GAMEMODE_PVP = 0;
    public static final int GAMEMODE_TEST = 2;

    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        ImageIcon bg = new ImageIcon("res/bgimage/bg1.png");
        g2D.scale((float)1024/bg.getIconWidth(), (float)768/bg.getIconHeight());
        g2D.drawImage(bg.getImage(), 0, 0, null);
    }

    private static void initial() {

    }
    public static void run(final HelloWindow f, final int width, final int height){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setTitle("Hello!");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(new Dimension(width, height));
                f.setResizable(false);
                f.setLayout(null);
                f.repaint();
                musThread.start();

                class MyMouseListener implements MouseListener {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println(e.getPoint());
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e) {}
                }

                f.addMouseListener(new MyMouseListener());

                offlineMode.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        f.dispose();//点击按钮关闭窗口
                        musThread.stop();//本方法已被弃用，不安全，但是这里能使
                        GoBang.run(new JFrame(), 955, 638, GAMEMODE_TEST);
                    }
                });

                PVEMode.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        /*f.dispose();//点击按钮关闭窗口
                        musThread.stop();//本方法已被弃用，不安全，但是这里能使
                        GoBang.run(new JFrame(), 955, 638, GAMEMODE_PVE);
                        */
                        JOptionPane.showMessageDialog(new JOptionPane(), "暂未开放！",
                                "噢！", JOptionPane.PLAIN_MESSAGE);
                    }
                });

                PVPMode.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        f.dispose();//点击按钮关闭窗口
                        musThread.stop();//本方法已被弃用，不安全，但是这里能使
                        GoBang.run(new JFrame(), 955, 638, GAMEMODE_PVP);
                    }
                });

                Exit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        f.dispatchEvent(new WindowEvent(f,WindowEvent.WINDOW_CLOSING));//点击按钮关闭窗口
                    }
                });

                offlineMode.setBounds(600, 160, 200, 60);
                PVEMode.setBounds(600,280,200,60);
                PVPMode.setBounds(600,400,200,60);
                Exit.setBounds(600,520,200,60);

                offlineMode.setVisible(true);
                PVEMode.setVisible(true);
                PVPMode.setVisible(true);
                Exit.setVisible(true);

                f.add(offlineMode);
                f.add(PVEMode);
                f.add(PVPMode);
                f.add(Exit);

                initial();

                f.setVisible(true);
            }
        });



    }
    public static void main(String[] args) {
        run(new HelloWindow(), 1024, 768);
    }
}

