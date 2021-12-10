public class Position {
    public int type;
    Position(){type = 0;}
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

