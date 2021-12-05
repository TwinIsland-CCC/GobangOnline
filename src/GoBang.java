import javax.swing.*;
import java.awt.*;

public class GoBang {

    private static final JButton btn0 = new JButton("0");
    private static final JButton btn1 = new JButton("1");
    private static final JButton btn2 = new JButton("2");
    private static final JButton btn3 = new JButton("3");

    private static final JPanel area1 = new JPanel();
    private static final JPanel area2 = new JPanel();
    private static final JPanel area3 = new JPanel();
    private static final JPanel area4 = new JPanel();

    private static final JPanel centre = new JPanel();

    private static final JLabel test = new JLabel("Mr.CCC");

    private static void initial() {
        test.setVisible(true);

        area1.add(btn0);
        area2.add(btn1);
        area3.add(btn2);
        area4.add(btn3);
        centre.add(test);

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
