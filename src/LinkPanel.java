import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LinkPanel extends JPanel {
    private static final JLabel ipHint = new JLabel("ip地址：");
    public static final JTextField ipInput = new JTextField("localhost");
    public static final JTextArea infHint = new JTextArea("等待操作...\n");
    private static final JButton listenBtn = new JButton("接收");
    private static final JButton connectBtn = new JButton("连接");
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

                infHint.setEditable(false);
                infHint.setPreferredSize(new Dimension(250, 100));

                f.add(ipHint);
                f.add(ipInput);
                f.add(listenBtn);
                f.add(connectBtn);
                f.add(infHint);
                f.setVisible(true);
            }
        });



    }
    public static void main(String[] args) {
        run(new JFrame(), 300, 400);
    }
}
