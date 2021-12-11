public class Position {
    public int type;
    public int x;
    public int y;
    public int indexOfX;
    public int indexOfY;
    Position(){x = 0; y = 0; type = GoBang.SPACE;}
    Position(int x, int y, int type, int inx, int iny){
        this.x = x;
        this.y = y;
        this.type = type;
        indexOfX = inx;
        indexOfY = iny;
    }
    public boolean canPutChess(){
        return type == GoBang.SPACE;
    }
    public void setType(int inType){
        type = inType;
    }
    public boolean isEqual(Position position){
        return this.type == position.type;
    }
}

