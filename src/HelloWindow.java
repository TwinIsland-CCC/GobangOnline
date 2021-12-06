import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HelloWindow {
    private static final JPanel area1 = new JPanel();
    private static final JPanel area2 = new JPanel();
    private static final JPanel area3 = new JPanel();
    private static final JPanel area4 = new JPanel();

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

                JButton Play = new JButton("Play");
                JButton Exit = new JButton("Exit");

                Play.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        f.dispose();//点击按钮关闭窗口
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
