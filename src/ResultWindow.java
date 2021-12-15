import javax.swing.*;
import java.awt.*;

public class ResultWindow {
    private static final Thread musThread = new Thread(new Runnable() {//背景音乐播放
        @Override
        public void run() {
            Music.play("res/bgmusic/Credits.wav", 0.25, Music.LOOP);
        }
    });

    private static final JTextArea text = new JTextArea();
    private static final JPanel operateBar = new JPanel();
    private static final JButton restartBtn = new JButton("重新开始");
    private static final JButton replayBtn = new JButton("复盘");
    private static final JButton exitBtn = new JButton("退出游戏");


    private static void initial() {

    }

    public static void run(final JFrame f, final int width, final int height, int mode, int winner){
        //mode为模式选择
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setTitle("Mr.CCC");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//调试完毕后要删掉这一行
                f.setSize(new Dimension(width, height));
                f.setResizable(false);
                f.setLayout(new BorderLayout(3,3));

                musThread.start();
                f.add(text, BorderLayout.CENTER);

                operateBar.setLayout(new GridLayout(1,3));

                operateBar.add(restartBtn);
                operateBar.add(replayBtn);
                operateBar.add(exitBtn);

                f.add(operateBar, BorderLayout.SOUTH);
                f.setVisible(true);
            }
        });

    }
    public static void main(String[] args) {
        run(new JFrame(), 300, 400, Vars.GAMEMODE_TEST, GoBang.BLACK);
    }

}
