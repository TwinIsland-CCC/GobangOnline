import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ResultWindow {
    private static final Thread musThread = new Thread(new Runnable() {//背景音乐播放
        @Override
        public void run() {
            Music.playMusic("res/bgmusic/Credits.wav", 0.25);
        }
    });

    private static final JButton restartBtn = new JButton("重新开始");
    private static final JButton replayBtn = new JButton("复盘");
    private static final JButton exitBtn = new JButton("退出游戏");

    private static void initial() {

    }

    public static void run(final JFrame f, final int width, final int height, int mode){
        //mode为模式选择
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setTitle("Mr.CCC");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(new Dimension(width, height));
                f.setResizable(false);
                f.setLayout(new BorderLayout(3,3));

                musThread.start();

                f.setVisible(true);
            }
        });

    }
    public static void main(String[] args) {
        run(new JFrame(), 800, 600, HelloWindow.PVE);
    }

}
