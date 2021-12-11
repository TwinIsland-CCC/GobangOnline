import javax.swing.*;
import java.awt.*;

public class ContactPanel extends JPanel {

    private static final Font myFont = new Font("", Font.BOLD, 30);
    public static final JTextArea infArea = new JTextArea("Test");//显示游戏中信息
    private static final JTextArea showArea = new JTextArea("Test");
    private static final JTextArea textArea = new JTextArea("Test");
    ImageIcon bgImage = new ImageIcon("res/other/chesspanel.png");
    public ContactPanel(LayoutManager layout, boolean isDoubleBuffered) {

        super(layout, isDoubleBuffered);
        setPreferredSize(new Dimension(300,600));
        infArea.setPreferredSize(new Dimension(300,50));
        showArea.setPreferredSize(new Dimension(300,400));
        textArea.setPreferredSize(new Dimension(300,20));
        infArea.setEditable(false);
        infArea.setFont(myFont);

        showArea.setEditable(false);


        add(infArea);
        add(showArea);
        add(textArea);
    }
/*    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        ImageIcon bg = new ImageIcon("res/bgimage/bg1.png");
        //g2D.scale((float)getWidth()/bg.getIconWidth(), (float)getHeight()/bg.getIconHeight());
        g2D.drawImage(bgImage.getImage(), 0, 0, null);
    }*/

}
