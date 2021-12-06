import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class GoBang {

    private static final JButton btn0 = new JButton("0");
    private static final JButton btn1 = new JButton("1");
    private static final JButton btn2 = new JButton("2");
    private static final JButton btn3 = new JButton("3");
    private static final JButton Back = new JButton("Back");

    private static final JPanel area1 = new JPanel();
    private static final JPanel area2 = new JPanel();
    private static final JPanel area3 = new JPanel();
    private static final JPanel area4 = new JPanel();

    private static final JPanel centre = new JPanel();

    private static final JLabel test = new JLabel("Mr.CCC");

    private static void initial() {


    }

    public static void run(final JFrame f, final int width, final int height){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                f.setTitle("Mr.CCC");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(new Dimension(width, height));
                f.setResizable(false);
                f.setLayout(new BorderLayout(3,3));

                initial();
                test.setVisible(true);

                Back.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int res = JOptionPane.showConfirmDialog(new JOptionPane(), "Back to HelloWindow?",
                                "Are you sure? ", JOptionPane.YES_NO_OPTION);
                        System.out.println(res);
                        if (res == 0){
                            f.dispatchEvent(new WindowEvent(f,WindowEvent.WINDOW_CLOSING));//TODO 返回，现在是点击按钮关闭
                            HelloWindow.run(new JFrame(), 1024, 768);
                        }
                    }
                });

                area4.setLayout(new FlowLayout());

                area1.add(btn0);
                area2.add(btn1);
                area3.add(btn2);
                area4.add(btn3);
                area4.add(Back);
                centre.add(test);


                f.add(area1, BorderLayout.NORTH);
                f.add(area2, BorderLayout.WEST);
                f.add(area3, BorderLayout.EAST);
                f.add(area4, BorderLayout.SOUTH);
                f.add(centre, BorderLayout.CENTER);

                f.setVisible(true);
            }
        });

    }
    public static void main(String[] args) {
        run(new JFrame(), 800, 600);
    }

}
