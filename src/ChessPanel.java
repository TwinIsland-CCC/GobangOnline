import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Vector;
import java.math.*;

public class ChessPanel extends JPanel {
    //边距：22
    //棋盘格间距：35

    ImageIcon bgImage = new ImageIcon("res/other/chesspanel.png");
    private static final int radius = 17;//检测按下哪个位置的半径
    private static final int[][] chessPos = new int[15][15];
    private static final Point[][] chessPoint = new Point[15][15];
    private static final ImageIcon blackChess = new ImageIcon("res/other/black.png");
    private static final ImageIcon whiteChess = new ImageIcon("res/other/white.png");
    private static final Point thisPoint = new Point(22,22);
    private static int thisX = 22;
    private static int thisY = 22;
    private static int indexOfX = 0;
    private static int indexOfY = 0;
    private static final Vector<Point> history = new Vector<>();

    public ChessPanel(LayoutManager layout, boolean isDoubleBuffered) {

        super(layout, isDoubleBuffered);
        for (int i = 0; i < 15; i++){
            chessPoint[i] = new Point[15];
            for (int j = 0; j < 15; j++){
                chessPoint[i][j] = new Point(i*35+22 - radius, j*35+22 - radius);
                //chessPoint[i][j] = new Point((i+1)*35+22, (j+1)*35+22);
                chessPos[i][j] = GoBang.SPACE;
            }
        }
        System.out.println(Arrays.deepToString(chessPoint));
        setSize(new Dimension(535,535));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("x : " + e.getX() + " y : " + e.getY());
                thisPoint.x = thisX = e.getX();
                thisPoint.y = thisY = e.getY();
                indexOfX = (thisX - 22) / 35;
                indexOfY = (thisY - 22) / 35;
                if ((thisX - 22)%35 > radius) indexOfX++;
                if ((thisY - 22)%35 > radius) indexOfY++;//根据棋盘格式设置位置，一次性代码

                //根据游戏模式，下面的代码会有所不同
                //测试：单机模式

                //TODO 如果不能在这里下，就不paint，出一个错误的音效
                if (GoBang.gameMode == HelloWindow.GAMEMODE_TEST) {
                    paintBlackChess(getGraphics(), chessPoint[indexOfX][indexOfY].getX(), chessPoint[indexOfX][indexOfY].getY());

                }
                else if (GoBang.gameMode == HelloWindow.GAMEMODE_PVE){

                }
                else if (GoBang.gameMode == HelloWindow.GAMEMODE_PVP){

                }

                //TODO 人机对战
                //TODO 联网对战
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

    }




    @Override
    public void paint(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        ImageIcon bg = new ImageIcon("res/bgimage/bg1.png");
        //g2D.scale((float)getWidth()/bg.getIconWidth(), (float)getHeight()/bg.getIconHeight());
        g2D.drawImage(bgImage.getImage(), 0, 0, null);
    }

    public void paintWhiteChess(Graphics g, double x, double y) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(whiteChess.getImage(), (int)x, (int)y, null);

    }
    public void paintBlackChess(Graphics g, double x, double y) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(blackChess.getImage(), (int)x, (int)y, null);
    }

    private double getDistance(Point x, Point y){
        return Math.sqrt(
                Math.pow(Math.abs(x.getX() - y.getX()), 2)
                + Math.pow(Math.abs(x.getX() - y.getY()), 2));
    }
}
