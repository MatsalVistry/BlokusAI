import java.io.Serializable;

public class IntPoint implements Serializable
{
    private int x;
    private int y;

    public IntPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString()
    {
        return "("+x+", "+y+")";
    }

    public boolean equals(Object o)
    {
        if(o instanceof IntPoint == false)
            return false;
        else
        {
            IntPoint p = (IntPoint) o;
            return p.getX()==x && p.getY()==y;
        }
    }
}
