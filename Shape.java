import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores all the data for a single shape
 */
public class Shape
{
    // stores all the forms of the shape
    private ArrayList<boolean[][]> forms = new ArrayList<>();

    // stores the different possible rotations of the shapes
    public static final int ZERO=0;
    public static final int NINTY=1;
    public static final int ONE_EIGHTY=2;
    public static final int TWO_SEVENTY=3;

    private int squareCount = 0;

    public Shape(boolean[][] initial)
    {
        forms.add(initial);
        forms.add(rotate(initial,1));
        forms.add(rotate(initial,2));
        forms.add(rotate(initial,3));
        forms.add(flip(initial));
        forms.add(rotate(flip(initial),1));
        forms.add(rotate(flip(initial),2));
        forms.add(rotate(flip(initial),3));

        for(int r=0; r<initial.length; r++)
            for(int c=0; c<initial[0].length;c++)
                if(initial[r][c])
                    squareCount++;
    }

    /**
     * returns the non-rotated / non-flipped version form of the shape
     * @return 2d array of the shape before any flipping or rotation
     */
    public boolean[][] original()
    {
        return forms.get(0);
    }

    /**
     * Returns a rotated and/or flipped version of the shape
     * @param flipped - if the shape should be flipped
     * @param rotation - the number of times to rotate the shape
     * @return - 2d rotated form of the shape
     */
    public boolean[][] manipulatedShape(boolean flipped, int rotation)
    {
        int index = ((flipped)?4:0)+rotation;
        return forms.get(index);
    }

    /**
     * Returns a text version of the shape
     * (includes all flips/rotations)
     * @return text version of all the forms of the shape
     */
    public String toString()
    {
        String s = "Original Shape\n" + arrayToText(forms.get(0)) + "\n";
        s += "Rotated 1 time:\n" + arrayToText(forms.get(1));
        s += "Rotated 2 times:\n" + arrayToText(forms.get(2));
        s += "Rotated 3 times:\n" + arrayToText(forms.get(3));
        s += "Flipped:\n" + arrayToText(forms.get(4));
        s += "Flipped and Rotated 1 time:\n" + arrayToText(forms.get(5));
        s += "Flipped and Rotated 2 times:\n" + arrayToText(forms.get(6));
        s += "Flipped and Rotated 3 times:\n" + arrayToText(forms.get(7));
        return s;
    }

    /**
     * Compares a shape to another shape
     * @param o - the other shape to compare to
     * @return true if the shapes are equals, false if they are not equal or the received value is not a shape
     */
    public boolean equals(Object o)
    {
        if(o instanceof Shape)
        {
            Shape other = (Shape)o;
            if(other.original().length==original().length && other.original()[0].length==original()[0].length)
            {
                for(int r=0; r<original().length; r++)
                    for(int c=0; c<original()[0].length; c++)
                    {
                        if(original()[r][c] != other.original()[r][c])
                            return false;
                    }
                return true;
            }
            else
                return false;
        }
        else
        {
            return false;
        }
    }

    private boolean[][] rotate(boolean[][] data,int times)
    {
        boolean[][] clone = new boolean [data.length][data[0].length];
        for(int r=0; r<data.length; r++)
            for(int c=0; c<data[0].length; c++)
                clone[r][c] = data[r][c];

        for(int r=0; r<times; r++)
            clone = rotateShapeHelper(clone);

        return clone;
    }

    /**
     * Returns a horizonal flip of the provided 2d array
     * @param data - the array to be flipped
     * @return a horizontally flipped version of the 2d array
     */
    private boolean[][] flip(boolean[][] data)
    {
        boolean[][] flippedData = new boolean[data.length][data[0].length];
        for(int r=0; r<data.length; r++)
            for(int c=0; c<data[0].length; c++)
            {
                flippedData[r][c] = data[r][data[0].length-c-1];
            }
        return flippedData;
    }

    /**
     * Returns a 90 degree rotation of the provided 2d array
     * @param shape - the array to be rotated
     * @return a rotated version of the 2d array
     */
    private boolean[][] rotateShapeHelper(boolean[][] shape)
    {
        int newRows=shape[0].length;
        int newCols=shape.length;

        boolean[][] newShape = new boolean[newRows][newCols];

        for(int r=0; r<newShape.length; r++)
        {
            for(int c = 0; c<newShape[0].length; c++)
            {
                newShape[r][c] = shape[newShape[0].length-c-1][r];
            }
        }

        return newShape;
    }

    /**
     * Returns the number of used possitions in the shape
     */
    public int getSquareCount()
    {
        return squareCount;
    }

    /**
     * Text version an array in grid format
     * true prints as *
     * false prints as -
     * @param data - the boolean array to be be printed
     * @return a text version of a 2d boolean array. (true:* / false:-)
     */
    private String arrayToText(boolean[][] data)
    {
        String s = "";
        for(int r=0; r<data.length; r++) {
            for (int c = 0; c < data[0].length; c++) {
                s += (data[r][c]) ? "*" : "-";
            }
            s+="\n";
        }
        return s;
    }
}
