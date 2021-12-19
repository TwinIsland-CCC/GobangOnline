import java.util.Vector;

public class Vars {
    //本类用来存放全局都需要使用到的变量
    public static final int GAMEMODE_PVE = 1;
    public static final int GAMEMODE_PVP = 0;
    public static final int GAMEMODE_TEST = 2;

    public static int CHESS_TYPE = GoBang.BLACK;//默认黑棋

    public static final Network net = new Network();

    public static int gameMode;
    public static boolean gameIsOver = false;

    public static String P1Name = "CCC";
    public static String P2Name = "Bot";
    public static int P1Type = 1;
    public static int P2Type = 0;
    public static int P1WinNum = 0;
    public static int P2WinNum = 0;
    public static String gameType = null;

    public static boolean timeModeFlag = false;

    public static String p1GameInf = "";
    public static String p2GameInf = "";

    public static Timer myTimer = new Timer();

    public static int steps = 0;
    public static final Position[][] chessPos = new Position[19][19];//四个哨兵
    public static final Vector<Position> history = new Vector<>();//TODO 复盘和悔棋

}
