import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Vector;

public class ChessPanel extends JPanel {
    //边距：22
    //棋盘格间距：35
    private static final Sound success = new Sound("res/audio/success.wav");
    private static final Sound fail = new Sound("res/audio/fail.wav");
    private static final Sound gameover = new Sound("res/audio/gameover.wav");

    private static final Thread sndThread = new Thread(new Runnable() {//音效播放
        @Override
        public void run() {
            success.play(Sound.NOT_LOOP);//危险，有崩溃的风险
            sndThread.interrupt();
        }
    });
    ImageIcon bgImage = new ImageIcon("res/other/chesspanel.png");
    private static final int radius = 17;//检测按下哪个位置的半径
    private static final Position[][] chessPos = new Position[19][19];//四个哨兵
    private static final Point[][] chessPoint = new Point[15][15];
    private static final ImageIcon blackChess = new ImageIcon("res/other/black.png");
    private static final ImageIcon whiteChess = new ImageIcon("res/other/white.png");
    private static final Point thisPoint = new Point(22,22);
    private static int thisX = 22;
    private static int thisY = 22;
    private static int indexOfX = 0;
    private static int indexOfY = 0;
    private static int steps = 0;
    private static final Vector<Position> history = new Vector<>();

    public ChessPanel(LayoutManager layout, boolean isDoubleBuffered) {

        super(layout, isDoubleBuffered);
        for (int i = 0; i < 15; i++){//初始化
            chessPoint[i] = new Point[15];
            for (int j = 0; j < 15; j++){
                chessPoint[i][j] = new Point(i*35+22 - radius, j*35+22 - radius);
                //chessPoint[i][j] = new Point((i+1)*35+22, (j+1)*35+22);
            }
        }
        for (int i = 0; i < 19; i++){//初始化
            chessPos[i] = new Position[19];
            for (int j = 0; j < 19; j++){
                chessPos[i][j] = new Position();
                chessPos[i][j].setType(GoBang.SPACE);
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
                    if (chessPos[indexOfX][indexOfY].canPutChess()){
                        if (steps%2 == 0){//黑棋
                            paintBlackChess(getGraphics(), chessPoint[indexOfX][indexOfY].getX(), chessPoint[indexOfX][indexOfY].getY());
                            chessPos[indexOfX][indexOfY].setType(GoBang.BLACK);
                        }
                        else {//白棋
                            paintWhiteChess(getGraphics(), chessPoint[indexOfX][indexOfY].getX(), chessPoint[indexOfX][indexOfY].getY());
                            chessPos[indexOfX][indexOfY].setType(GoBang.WHITE);
                        }
                        //sndThread.start();
                        success.play(Sound.NOT_LOOP);//TODO 考虑开一个新的线程存按键音？
                        steps++;
                        System.out.println(indexOfX + " " + indexOfY);
                        if (isWin(indexOfX, indexOfY)){
                            gameover.play(Sound.NOT_LOOP);
                            GameOver();
                        }

                    }
                    else {
                        fail.play(Sound.NOT_LOOP);
                    }

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

    private void GameOver() {


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

    public boolean isWin(int row, int col){//思路：每下完一个棋子，扫描以它为中心的八个方向是否有另外四个同类型棋子与它组成5个连子。
        int currentType = chessPos[row][col].type;
        int startOfSearchRow = row - 4;
        int startOfSearchCol = col - 4;
        if (startOfSearchRow < 0) startOfSearchRow = 1;
        else if (startOfSearchRow > 6) startOfSearchRow = 6;
        if (startOfSearchCol < 0) startOfSearchCol = 1;
        else if (startOfSearchCol > 6) startOfSearchCol = 6;
        for (int i = startOfSearchRow; i < startOfSearchRow + 5; i++){
            for (int j = startOfSearchCol; j < startOfSearchCol + 5; j++){
                boolean b1 = currentType == chessPos[i][j+1].type && currentType == chessPos[i][j+2].type
                        && currentType == chessPos[i][j+3].type && currentType == chessPos[i][j+4].type && currentType == chessPos[i][j].type;
                boolean b2 = currentType == chessPos[i+1][j].type && currentType == chessPos[i+2][j].type
                        && currentType == chessPos[i+3][j].type && currentType == chessPos[i+4][j].type && currentType == chessPos[i][j].type;
                boolean b3 = currentType == chessPos[i+1][j+1].type && currentType == chessPos[i+2][j+2].type
                        && currentType == chessPos[i+3][j+3].type && currentType == chessPos[i+4][j+4].type && currentType == chessPos[i][j].type;
                boolean b4 = currentType == chessPos[i+4][j].type && currentType == chessPos[i+3][j+1].type
                        && currentType == chessPos[i+2][j+2].type && currentType == chessPos[i+1][j+3].type && currentType == chessPos[i][j+4].type;
                boolean b5 = currentType == chessPos[i][j+5].type && currentType == chessPos[i][j+6].type
                        && currentType == chessPos[i][j+7].type && currentType == chessPos[i][j+8].type && currentType == chessPos[i][j+4].type;
                boolean b6 = currentType == chessPos[i+5][j].type && currentType == chessPos[i+6][j].type
                        && currentType == chessPos[i+7][j].type && currentType == chessPos[i+8][j].type && currentType == chessPos[i+4][j].type;
                boolean b7 = currentType == chessPos[i+4][j+1].type && currentType == chessPos[i+4][j+2].type
                        && currentType == chessPos[i+4][j+3].type && currentType == chessPos[i+4][j+4].type && currentType == chessPos[i+4][j].type;
                boolean b8 = currentType == chessPos[i+1][j+4].type && currentType == chessPos[i+2][j+4].type
                        && currentType == chessPos[i+3][j+4].type && currentType == chessPos[i+4][j+4].type && currentType == chessPos[i][j+4].type;
                if (b1||b2||b3||b4||b5||b6||b7||b8){
                    return true;
                }
            }
        }
        return false;
    }

}
