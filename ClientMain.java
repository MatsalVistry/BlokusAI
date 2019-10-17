import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.*;

/**
 * Run a blokus client
 */
public class ClientMain
{
    // stores the servers address
    public static final String ip = "127.0.0.1";
    // stores the port to use for connecting to the sever
    public static final int port = 8621;

    // stores the time to sleep between moves
    public static int moveSleepTime 		= 0;
    // stores how long to wait after a round finishes
    public static int endSleepTime 			= 0;
    // stores how long to wait after showing who will be playing who
    public static int matchInfoSleepTime 	= 0;
    // list of all the scores
    public static PlayerScores scores=null;


    public static void main(String[] args)
    {
        Scanner keyboard = new Scanner(System.in);

        BlokusBoard board=new BlokusBoard();
        Move m;
        DisplayScreen ds = new DisplayScreen(board);
        int mode = 1;

        // Change this to your AI!!!
        Player myAIasOrange = new NitinAI(BlokusBoard.ORANGE, "Nitin's AI");
        Player myAIasPurple = new NitinAI(BlokusBoard.PURPLE, "Nitin's AI");

        String myAI_Name = myAIasOrange.getName();
        String opponentName = "";
        Player currentlyPlaying = null;

        while(true)
        {

            if(mode == 1)
            {
                do
                {
                    System.out.println("-Test Type-");
                    System.out.println("1. AI vs Testers");
                    System.out.println("2. Exit");
                    System.out.print("Enter selection: ");
                    mode = keyboard.nextInt();
                }while(mode < 1 || mode > 2);
                scores = new PlayerScores();
                try
                {

                    Socket connectionToServer = new Socket(ip,port);

                    ObjectInputStream is = new
                            ObjectInputStream(connectionToServer.getInputStream());

                    ObjectOutputStream os = new
                            ObjectOutputStream(connectionToServer.getOutputStream());

                    board.reset();
                    while(true)
                    {
                        os.writeObject(new CommandToServer(CommandToServer.NEW_MATCH,myAI_Name));
                        os.reset();

                        CommandToClient categoriesFromSever = (CommandToClient)is.readObject();
                        ArrayList<String> categories = (ArrayList<String>)categoriesFromSever.getCommandData();

                        int spot = 0;

                        do
                        {
                            System.out.println("\n-Select an AI Category-");
                            for(int x = 0; x<categories.size();x++)
                            {
                                System.out.println(x+"."+ categories.get(x));
                            }
                            System.out.print("Enter selection:");
                            spot =keyboard.nextInt();
                        }while(spot>=categories.size());

                        os.writeObject(new CommandToServer(CommandToServer.SELECT_CATEGORY,spot));
                        os.reset();

                        CommandToClient aiListFromSever = (CommandToClient)is.readObject();
                        ArrayList<String> aiList = (ArrayList<String>)aiListFromSever.getCommandData();

                        spot = 0;

                        do
                        {
                            System.out.println("\n-Select an AI-");
                            for(int x = 0; x<aiList.size();x++)
                            {
                                System.out.println(x+"."+ aiList.get(x));
                            }
                            System.out.print("Enter selection:");
                            spot = keyboard.nextInt();
                        }while(spot>=aiList.size());

                        os.writeObject(new CommandToServer(CommandToServer.SELECT_AI,spot));
                        os.reset();
                        opponentName = aiList.get(spot);


                        // play full games list
                        while(true)
                        {
                            CommandToClient comFromServer = (CommandToClient)is.readObject();
                            //System.out.println("com from server"+ comFromServer.getCommand());

                            if(comFromServer.getCommand()==CommandToClient.START_PLAYER_FIRST_GAMES)
                            {
                                board.reset();
                                currentlyPlaying = myAIasOrange;
                                System.out.println("\n\n***"+myAIasOrange.getName()+ " is playing as ORANGE vs "+ opponentName+ "*** ");
                                Thread.sleep(matchInfoSleepTime);
                            }
                            else if(comFromServer.getCommand()==CommandToClient.START_PLAYER_SECOND_GAMES)
                            {
                                board.reset();
                                currentlyPlaying = myAIasPurple;
                                System.out.println("\n\n***"+opponentName+ " is playing as ORANGE vs "+myAIasPurple.getName() + "*** ");
                                Thread.sleep(matchInfoSleepTime);
                            }
                            else if(comFromServer.getCommand()==CommandToClient.SUCCESSFUL_MOVE)
                            {
                                m=(Move)comFromServer.getCommandData();
                                board.makeMove(m,currentlyPlaying.getColor());
                                Thread.sleep(matchInfoSleepTime);
                            }
                            else if(comFromServer.getCommand()==CommandToClient.PLAYER_FAILED_TO_MOVE)
                            {
                                System.out.println("Your AI failed to provide a valid move!!!");
                                Thread.sleep(moveSleepTime);
                                if(currentlyPlaying.getColor()==board.ORANGE)
                                    board.orangeSkips();
                                else
                                    board.purpleSkips();
                            }
                            else if(comFromServer.getCommand()==CommandToClient.OPPONENT_FAILED_TO_MOVE)
                            {
                                System.out.println("Your Oppenet failed to provide a valid move!!!");
                                Thread.sleep(moveSleepTime);
                                if(currentlyPlaying.getColor()==board.ORANGE)
                                    board.purpleSkips();
                                else
                                    board.orangeSkips();
                            }
                            else if(comFromServer.getCommand()==CommandToClient.PLAYER_SKIP)
                            {
                                System.out.println("Your AI skipped its turn!!!");
                                Thread.sleep(moveSleepTime);
                                if(currentlyPlaying.getColor()==board.ORANGE)
                                    board.orangeSkips();
                                else
                                    board.purpleSkips();

                            }
                            else if(comFromServer.getCommand()==CommandToClient.OPPONENT_SKIP)
                            {
                                System.out.println("Your Opponet skipped it's turn!!!");
                                Thread.sleep(moveSleepTime);
                                if(currentlyPlaying.getColor()==board.ORANGE)
                                    board.purpleSkips();
                                else
                                    board.orangeSkips();

                            }
                            else if(comFromServer.getCommand()==CommandToClient.OPPONENT_MOVE)
                            {
                                board.makeMove((Move)comFromServer.getCommandData(),getOpponentValue(currentlyPlaying.getColor()));
                                Thread.sleep(moveSleepTime);

                            }
                            else if(comFromServer.getCommand()==CommandToClient.WIN)
                            {
                                System.out.println("\t\t\t"+currentlyPlaying.getName() +" wins!");
                                Thread.sleep(endSleepTime);
                                scores.addWin();
                                board.reset();
                            }
                            else if(comFromServer.getCommand()==CommandToClient.LOSE)
                            {
                                System.out.println("\t\t\t"+opponentName +" wins!");
                                Thread.sleep(endSleepTime);
                                scores.addLoss();
                                board.reset();

                            }
                            else if(comFromServer.getCommand()==CommandToClient.TIE)
                            {
                                System.out.println("\t\t\t"+"\tTie Game");
                                Thread.sleep(endSleepTime);
                                scores.addCat();
                                board.reset();
                            }
                            else if(comFromServer.getCommand()==CommandToClient.MAKE_MOVE)
                            {
                                m = currentlyPlaying.getMove(board);
                                os.reset();
                                os.writeObject(new CommandToServer(CommandToServer.MOVE,m));
                                os.reset();

                            }
                            else if(comFromServer.getCommand()==CommandToClient.MATCHES_COMPLETE)
                            {
                                System.out.println("Your AI's results are: ");
                                System.out.println("Wins: "+scores.getWins());
                                System.out.println("Cats: "+scores.getCats());
                                System.out.println("Loses: "+scores.getLosses());
                                Thread.sleep(endSleepTime);
                                break;
                            }

                        }
                        break;

                    }


                }
                catch(Exception e)
                {
                    System.out.println("Error in main: "+e.getMessage());
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Shutting down...");
                System.exit(0);
            }
        }
    }

    /**
     * Gets the opposite color the give color
     * @param self - color
     * @return returns the opposite color of the provide color (ORANGE /PURPLE)
     */
    public static char getOpponentValue(int self)
    {
        if(self==BlokusBoard.ORANGE)
            return BlokusBoard.PURPLE;
        else
            return BlokusBoard.ORANGE;
    }
}