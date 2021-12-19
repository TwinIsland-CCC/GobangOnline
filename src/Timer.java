import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Timer extends JPanel {

    private final JLabel infLabel = new JLabel("初始化");
    private long maxTime = 10;

    private ScheduledThreadPoolExecutor scheduled;

    public static void main(String[] args) {
        Timer t = new Timer();
        t.startCount();
    }
    long time = 3;//每人下棋的总秒数
    public void startCount() {//开始计时，核心模块
        time = maxTime;
        scheduled = new ScheduledThreadPoolExecutor(2);
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(time);
                if (time <= 0) {
                    timeout();
                    return;
                }
                long hour = time / 3600;
                long minute = (time - hour * 3600) / 60;
                long seconds = time - hour * 3600 - minute * 60;
                String content = "<html>您还有<br>" + minute + "分钟<br> " + seconds + "秒<br>的下棋时间 " +
                        "</html>";
                infLabel.setText(content);
                time--;
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void timeout() {
        stopTimer();
        infLabel.setText("下棋时间耗尽");
        ContactPanel.showArea.append("你的下棋时间耗尽，你输了\n");
        if (Vars.gameMode == Vars.GAMEMODE_TEST){
            GoBang.centre.GameOver(Vars.steps % 2);
        }
        else if (Vars.gameMode == Vars.GAMEMODE_PVE){

        }
        else if (Vars.gameMode == Vars.GAMEMODE_PVP) {
            GoBang.centre.GameOver(Vars.steps % 2);
            Vars.net.sendTimeOut();
        }

    }

    public void stopTimer() {
        if (scheduled != null) {
            scheduled.shutdownNow();
            infLabel.setText("你已经下棋");
            scheduled = null;
        }
    }
    public Timer() {
        //scheduled = new ScheduledThreadPoolExecutor(2);
        setLayout(new FlowLayout());
        infLabel.setText("初始化");
        JButton shutdown = new JButton("强行停止");
        shutdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTimer();
            }
        });
        //add(shutdown);
        JButton open = new JButton("重新开始");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCount();
            }
        });
        //add(open);
        add(infLabel);
    }

}