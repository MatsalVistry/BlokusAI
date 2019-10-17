import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class BigMoverAI extends Player
{

    public BigMoverAI(int color,String name)
    {
        super(color, name);
    }


    public Move getMove(BlokusBoard board)
    {

        ArrayList<IntPoint> avaiableMoves = board.moveLocations(getColor());
        Collections.shuffle(avaiableMoves);
        //System.out.println("available move locations "+avaiableMoves);
        ArrayList<Integer> usableFives = new ArrayList<>();
        ArrayList<Integer> usableFours = new ArrayList<>();
        ArrayList<Integer> usableThrees = new ArrayList<>();
        ArrayList<Integer> usableTwos = new ArrayList<>();
        ArrayList<Integer> usableOnes = new ArrayList<>();
        boolean[] used = (getColor()==BlokusBoard.ORANGE)?board.getOrangeUsedShapes():board.getPurpleUsedShapes();
        for(int x=0; x<used.length; x++)
            if(!used[x])
            {
                if(board.getShapes().get(x).getSquareCount()==1)
                    usableOnes.add(x);
                else if(board.getShapes().get(x).getSquareCount()==2)
                    usableTwos.add(x);
                else if(board.getShapes().get(x).getSquareCount()==3)
                    usableThrees.add(x);
                else if(board.getShapes().get(x).getSquareCount()==4)
                    usableFours.add(x);
                else
                    usableFives.add(x);
            }


        Collections.shuffle(usableFives);
        Collections.shuffle(usableFours);
        Collections.shuffle(usableThrees);
        Collections.shuffle(usableTwos);
        Collections.shuffle(usableOnes);

        Move fiveMove = moveFromList(board,avaiableMoves,usableFives);
        if(fiveMove!=null) {

            return fiveMove;
        }
        Move fourMove = moveFromList(board,avaiableMoves,usableFours);
        if(fourMove!=null){

            return fourMove;
        }

        Move threeMove = moveFromList(board,avaiableMoves,usableThrees);
        if(threeMove!=null)
        {

            return threeMove;
        }

        Move twoMove = moveFromList(board,avaiableMoves,usableTwos);
        if(twoMove!=null)
        {
            return twoMove;
        }

        Move oneMove = moveFromList(board,avaiableMoves,usableOnes);
        if(oneMove!=null)
        {

            return oneMove;
        }

        return null;
    }


    private Move moveFromList(BlokusBoard board,ArrayList<IntPoint> avaialbeMoves, ArrayList<Integer> shapeLocs)
    {
        Move move = null;
        for(Integer position: shapeLocs)
            for(IntPoint movLoc: avaialbeMoves)
            {
                for(int i=0; i<8;i++) {
                    boolean flip = i >= 3;
                    int rotation = i % 4;
                    boolean[][] shape = board.getShapes().get(position).manipulatedShape(flip, rotation);
                    for (int r = -shape.length+1; r <shape.length;  r++)
                        for (int c = -shape[0].length+1; c < shape[0].length; c++)
                        {
                            IntPoint topLeft = new IntPoint(movLoc.getX()+c,movLoc.getY()+r);
                            Move test = new Move(position,flip,rotation,topLeft);
                            if(board.isValidMove(test,getColor()))
                                return test;
                        }
                }
            }
        return null;
    }

    /**
     * Returns a clone of the player
     * @return a clone of this player
     */
    public Player freshCopy()
    {
        return new BigMoverAI(getColor(),getName());
    }
}
