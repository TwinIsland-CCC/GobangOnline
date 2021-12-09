import javax.swing.*;
import java.awt.*;

public class ContactPanel extends JPanel {
    private static final JTextArea showArea = new JTextArea("Test");
    private static final JTextArea textArea = new JTextArea("Test");
    ImageIcon bgImage = new ImageIcon("res/other/chesspanel.png");
    public ContactPanel(LayoutManager layout, boolean isDoubleBuffered) {

        super(layout, isDoubleBuffered);
        setPreferredSize(new Dimension(300,600));
        showArea.setPreferredSize(new Dimension(300,200));
        textArea.setPreferredSize(new Dimension(300,20));
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
