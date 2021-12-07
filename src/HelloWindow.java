import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static java.applet.Applet.newAudioClip;

public class HelloWindow {
    private static final JPanel area1 = new JPanel();
    private static final JPanel area2 = new JPanel();
    private static final JPanel area3 = new JPanel();
    private static final JPanel area4 = new JPanel();
    private static final Thread musThread = new Thread(new Runnable() {//背景音乐播放
        @Override
        public void run() {
            Music.playMusic("res/bgmusic/Start.wav");
        }
    });

    private static AudioClip bgm;

    private static Clip kick;

    private static final JButton Play = new JButton("Play");
    private static final JButton Exit = new JButton("Exit");

    private static final JPanel centre = new JPanel();

    private static final JLabel test = new JLabel("Mr.CCC");

    private static void initial() {
        test.setVisible(true);

        centre.add(test);

    }
    public static void run(final JFrame f, final int width, final int height){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setTitle("Hello!");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(new Dimension(width, height));
                f.setResizable(false);
                f.setLayout(null);

                musThread.start();

                JPanel bgShow = new JPanel() {
                    @Override
                    public void paint(Graphics g) {
                        Graphics2D g2D = (Graphics2D) g;
                        ImageIcon bg = new ImageIcon("res/bgimage/bg1.png");
                        g2D.scale((float)width/bg.getIconWidth(), (float)height/bg.getIconHeight());
                        g2D.drawImage(bg.getImage(), 0, 0, null);
                    }
                };
                bgShow.setLayout(null);
                bgShow.setBounds(0,0,width,height);
                f.setContentPane(bgShow);

                class MyMouseListener implements MouseListener {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println(e.getPoint());
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                }

                f.addMouseListener(new MyMouseListener());

                Play.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        f.dispose();//点击按钮关闭窗口
                        musThread.stop();//本方法已被弃用，不安全，但是这里能使
                        GoBang.run(new JFrame(), 800, 600);
                    }
                });

                Exit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        f.dispatchEvent(new WindowEvent(f,WindowEvent.WINDOW_CLOSING));//点击按钮关闭窗口
                    }
                });

                Play.setBounds(600,400,200,60);
                Exit.setBounds(600,520,200,60);

                f.add(Play);
                f.add(Exit);

                initial();

                f.setVisible(true);
            }
        });

    }
    public static void main(String[] args) {
        run(new JFrame(), 1024, 768);
    }
}

