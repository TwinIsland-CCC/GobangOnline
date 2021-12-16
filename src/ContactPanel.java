import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author CCC
 * 用于网络聊天以及显示部分信息
 */


public class ContactPanel extends JPanel {

    private static final Font myFont = new Font("", Font.BOLD, 30);
    public static final JTextArea infArea = new JTextArea("Test");//显示游戏中信息
    private static final JTextArea showArea = new JTextArea("游戏开始！\n");
    private static final JTextArea textArea = new JTextArea("测试测试测试测试");
    private static final JScrollPane showScrollPane = new JScrollPane(showArea);
    private static final JScrollPane textScrollPane = new JScrollPane(textArea);
    private static final JButton send = new JButton("发送");
    ImageIcon bgImage = new ImageIcon("res/other/chesspanel.png");
    public ContactPanel(LayoutManager layout, boolean isDoubleBuffered) {

        super(layout, isDoubleBuffered);
        setLayout(new GridLayout());
        setPreferredSize(new Dimension(300,600));
        showScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        showScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        infArea.setPreferredSize(new Dimension(300,50));
        //showArea.setPreferredSize(new Dimension(200,200));
        //textArea.setPreferredSize(new Dimension(300,20));
        showScrollPane.setPreferredSize(new Dimension(300,400));
        textScrollPane.setPreferredSize(new Dimension(300,20));

        JPanel panel = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new BorderLayout());
        panel.add(showScrollPane, BorderLayout.CENTER);
        panel.add(textScrollPane, BorderLayout.SOUTH);

        infArea.setEditable(false);
        infArea.setFont(myFont);

        showArea.setEditable(false);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Vars.gameMode == Vars.GAMEMODE_TEST)
                    putMessage(Vars.P1Name + ": " + textArea.getText());
                else if(Vars.gameMode == Vars.GAMEMODE_PVP) {
                    Vars.net.sendMessage(textArea.getText());
                }
            }
        });
        panel2.add(infArea, BorderLayout.NORTH);
        panel2.add(panel,BorderLayout.CENTER);
        panel2.add(send, BorderLayout.SOUTH);
        add(panel2);
    }

    public void putMessage(String output) {
        showArea.append(output+"\n");
    }
/*    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        ImageIcon bg = new ImageIcon("res/bgimage/bg1.png");
        //g2D.scale((float)getWidth()/bg.getIconWidth(), (float)getHeight()/bg.getIconHeight());
        g2D.drawImage(bgImage.getImage(), 0, 0, null);
    }*/

}
