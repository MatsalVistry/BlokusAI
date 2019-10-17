import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class ServersListener implements Runnable
{
    // Streams
    private ObjectInputStream is;
    private ObjectOutputStream os;

    // board
    private BlokusBoard board= new BlokusBoard();;

    // selected opponent
    private Player opponentAsOrange;
    private Player opponentAsPurple;

    // Orange AIs by category
    private ArrayList<Player> testingAIsAsOrange = new ArrayList<Player>();
    private ArrayList<Player> fourthPeriodAIsAsOrange = new ArrayList<Player>();
    private ArrayList<Player> seventhPeriodAIsAsOrange = new ArrayList<Player>();

    // Purple AIs by category
    private ArrayList<Player> testingAIsAsPurple = new ArrayList<Player>();
    private ArrayList<Player> fourthPeriodAIsAsPurple = new ArrayList<Player>();
    private ArrayList<Player> seventhPeriodAIsAsPurple = new ArrayList<Player>();

    // Category Names
    private ArrayList<String> categories = new ArrayList<String>();
    private int numberOfGameToPlay = 10;
    private CommandToClient commandFromSerever = null;

    // name of the connected AI
    private String playerName;

    /**
     * Constructs a server lister for running games
     * @param os - output steam
     * @param is - input steam
     */
    public ServersListener(ObjectOutputStream os, ObjectInputStream is)
    {
        this.is			= is;
        this.os			= os;

        // Adds testing AIs
        testingAIsAsOrange.add(new BigMoverAI(board.ORANGE, "Servers Random AI"));
        testingAIsAsPurple.add(new BigMoverAI(board.PURPLE, "Servers Random AI"));
        // Adds fourth Period AIs

        // Adds Seventh Period AIs

        // Load Categories
        categories.add("Test AIs");
        //categories.add("Fourth Period");
        //categories.add("Seventh Period");
    }

    /**
     * Receives and processes game commands
     */
    public void run()
    {
        try
        {
            //System.out.println("AI Connection");
            while(true)
            {
                CommandToServer bigCommand = (CommandToServer)is.readObject();
                if(bigCommand.getCommand() == CommandToServer.NEW_MATCH)
                {
                    playerName = (String) bigCommand.getCommandData();
                    //System.out.println("AI Name is" + playerName);

                    commandFromSerever = new CommandToClient(CommandToClient.CATEGORY_SELECTION,categories);
                    os.writeObject(commandFromSerever);
                    os.reset();


                    CommandToServer categoryCommand = (CommandToServer)is.readObject();
                    int categoryIndex =(Integer) categoryCommand.getCommandData();
                    ArrayList<String> names = new ArrayList<String>();
                    //System.out.println("Category Number" +categoryIndex);

                    if(categoryIndex == 0)
                    {
                        for(Player p: testingAIsAsOrange)
                        {
                            names.add(p.getName());
                        }

                        commandFromSerever = new CommandToClient(CommandToClient.AI_SELECTION,names);
                        os.writeObject(commandFromSerever);
                        os.reset();

                        CommandToServer pickedAI = (CommandToServer)is.readObject();
                        int ai_Index =(Integer) pickedAI.getCommandData();
                        //System.out.println("ai Number " +ai_Index);

                        if(ai_Index >= testingAIsAsOrange.size())
                        {
                            System.out.println("Bad AI Index");
                            break;
                        }
                        else
                        {
                            opponentAsOrange = testingAIsAsOrange.get(ai_Index).freshCopy();
                            opponentAsPurple = testingAIsAsPurple.get(ai_Index).freshCopy();
                        }
                    }
                    else if(categoryIndex == 1)
                    {
                        //System.out.println("Category not implemented");
                        break;

                    }
                    else if(categoryIndex == 2)
                    {
                        //System.out.println("Category not implemented");
                        break;

                    }
                    else
                    {
                        //System.out.println("Bad Category");
                        break;
                    }

                    CommandToClient a = new CommandToClient(CommandToClient.START_PLAYER_FIRST_GAMES,opponentAsPurple.getName());
                    os.writeObject(a);
                    os.reset();

                    for(int x = 0; x<numberOfGameToPlay; x++)
                        playGame(true);

                    commandFromSerever = new CommandToClient(CommandToClient.START_PLAYER_SECOND_GAMES,opponentAsOrange.getName());
                    os.writeObject(commandFromSerever);
                    os.reset();

                    for(int x = 0; x<numberOfGameToPlay; x++)
                        playGame(false);

                    commandFromSerever = new CommandToClient(CommandToClient.MATCHES_COMPLETE,opponentAsPurple.getName());
                    os.writeObject(commandFromSerever);
                    os.reset();

                    break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error in Server's Listener: "+ e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Plays a single blockus game
     * @param playerFirst - if the connected player is player is playing as orange
     */
    public void playGame(boolean playerFirst)
    {
        //System.out.println("In play game");
        try
        {
            board.reset();
            //Location l;
            boolean firstPlayersTurn = true;

            if(playerFirst)
            {
                while(true)
                {
                    if(board.status()==BlokusBoard.PLAYING)
                    {
                        if(firstPlayersTurn)
                        {
                            commandFromSerever = new CommandToClient(CommandToClient.MAKE_MOVE);
                            os.writeObject(commandFromSerever);
                            os.reset();

                            CommandToServer categoryCommand = (CommandToServer)is.readObject();
                            Move m =(Move) categoryCommand.getCommandData();
                            //System.out.println("Visitor moved to  "+l);

                            if(m==null)
                            {
                                System.out.println("ORANGE Connected AI skips");
                                //Thread.sleep(500);
                                board.orangeSkips();
                                commandFromSerever = new CommandToClient(CommandToClient.PLAYER_SKIP);
                                os.writeObject(commandFromSerever);
                                os.reset();

                            }
                            else if(board.isValidMove(m,BlokusBoard.ORANGE))
                            {
                                board.makeMove(m,BlokusBoard.ORANGE);
                                commandFromSerever = new CommandToClient(CommandToClient.SUCCESSFUL_MOVE,m);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }
                            else
                            {
                                System.out.println("ORANGE Connected AI made an invalid move "+m);
                                //System.out.println(Arrays.deepToString(board.getShapes().get(m.getPieceNumber()).manipulatedShape(m.isFlip(), m.getRotation())));
                                board.orangeSkips();
                                //Thread.sleep(500);
                                commandFromSerever = new CommandToClient(CommandToClient.FAILED_MOVE);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }

                            firstPlayersTurn = false;
                        }
                        else
                        {
                            Move m=opponentAsPurple.getMove(new BlokusBoard((board)));

                            if(m==null)
                            {
                                System.out.println("PURPLE Server AI skipps");
                                //Thread.sleep(500);
                                board.purpleSkips();
                                commandFromSerever = new CommandToClient(CommandToClient.OPPONENT_SKIP);
                                os.writeObject(commandFromSerever);
                                os.reset();
                                //Thread.sleep(100000);
                            }
                            else if(board.isValidMove(m,BlokusBoard.PURPLE))
                            {
                                board.makeMove(m,BlokusBoard.PURPLE);
                                commandFromSerever = new CommandToClient(CommandToClient.OPPONENT_MOVE,m);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }
                            else
                            {
                                System.out.println("PURPLE Server AI failed to move"+m);
                                //Thread.sleep(500);
                                //System.out.println(Arrays.deepToString(board.getShapes().get(m.getPieceNumber()).manipulatedShape(m.isFlip(), m.getRotation())));
                                board.purpleSkips();;
                                commandFromSerever = new CommandToClient(CommandToClient.OPPONENT_FAILED_TO_MOVE);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }

                            firstPlayersTurn = true;
                        }

                    }
                    if(board.status()==BlokusBoard.ORANGE_WINS)
                    {
                        //System.out.println("X Wins");
                        commandFromSerever = new CommandToClient(CommandToClient.WIN);
                        os.writeObject(commandFromSerever);
                        os.reset();
                        return;
                    }
                    else if(board.status()==BlokusBoard.PURPLE_WINS)
                    {
                        //System.out.println("O Wins");
                        commandFromSerever = new CommandToClient(CommandToClient.LOSE);
                        os.writeObject(commandFromSerever);
                        os.reset();
                        return;
                    }
                    else if(board.status()==BlokusBoard.TIE)
                    {
                        //System.out.println("Cat");
                        commandFromSerever = new CommandToClient(CommandToClient.TIE);
                        os.writeObject(commandFromSerever);
                        os.reset();
                        return;
                    }
                }
            }
            else
            {
                while(true)
                {
                    if(board.status()==BlokusBoard.PLAYING)
                    {
                        if(firstPlayersTurn)
                        {
                            Move m=opponentAsOrange.getMove(new BlokusBoard(board));
                            if(m==null)
                            {
                                System.out.println("ORANGE Sever AI skips");
                                //Thread.sleep(500);
                                board.orangeSkips();
                                commandFromSerever = new CommandToClient(CommandToClient.OPPONENT_SKIP);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }
                            else if(board.isValidMove(m,BlokusBoard.ORANGE))
                            {
                                //System.out.println("ORANGE Sever AI moves"+m);
                                //System.out.println(Arrays.deepToString(board.getShapes().get(m.getPieceNumber()).manipulatedShape(m.isFlip(), m.getRotation())));
                                board.makeMove(m,BlokusBoard.ORANGE);
                                commandFromSerever = new CommandToClient(CommandToClient.OPPONENT_MOVE,m);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }
                            else
                            {
                                System.out.println("ORANGE Sever AI failed to move"+m);
                                //System.out.println(Arrays.deepToString(board.getShapes().get(m.getPieceNumber()).manipulatedShape(m.isFlip(), m.getRotation())));
                                //Thread.sleep(500);
                                board.orangeSkips();
                                commandFromSerever = new CommandToClient(CommandToClient.OPPONENT_FAILED_TO_MOVE);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }

                            firstPlayersTurn = false;
                        }
                        else
                        {
                            commandFromSerever = new CommandToClient(CommandToClient.MAKE_MOVE);
                            os.writeObject(commandFromSerever);
                            os.reset();

                            CommandToServer categoryCommand = (CommandToServer)is.readObject();
                            Move m =(Move) categoryCommand.getCommandData();

                            if(m==null)
                            {
                                System.out.println("PURPLE Connected AI skips");
                                //Thread.sleep(500);
                                board.purpleSkips();
                                commandFromSerever = new CommandToClient(CommandToClient.PLAYER_SKIP);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }
                            else if(board.isValidMove(m,BlokusBoard.PURPLE))
                            {
                                board.makeMove(m,BlokusBoard.PURPLE);
                                commandFromSerever = new CommandToClient(CommandToClient.SUCCESSFUL_MOVE,m);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }
                            else
                            {
                                System.out.println("PURPLE Connected AI fails to move"+m);
                                //System.out.println(Arrays.deepToString(board.getShapes().get(m.getPieceNumber()).manipulatedShape(m.isFlip(), m.getRotation())));
                                //Thread.sleep(500);
                                board.purpleSkips();
                                commandFromSerever = new CommandToClient(CommandToClient.FAILED_MOVE);
                                os.writeObject(commandFromSerever);
                                os.reset();
                            }

                            firstPlayersTurn = true;
                        }

                    }
                    if(board.status()==BlokusBoard.ORANGE_WINS)
                    {
                        //System.out.println("X Wins");
                        commandFromSerever = new CommandToClient(CommandToClient.LOSE);
                        os.writeObject(commandFromSerever);
                        os.reset();
                        return;
                    }
                    else if(board.status()==BlokusBoard.PURPLE_WINS)
                    {
                        //System.out.println("O Wins");
                        commandFromSerever = new CommandToClient(CommandToClient.WIN);
                        os.writeObject(commandFromSerever);
                        os.reset();
                        return;
                    }
                    else if(board.status()==BlokusBoard.TIE)
                    {
                        //System.out.println("Cat");
                        commandFromSerever = new CommandToClient(CommandToClient.TIE);
                        os.writeObject(commandFromSerever);
                        os.reset();
                        return;
                    }
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Crashed While Playing a Game");
            e.printStackTrace();
        }
    }
}


