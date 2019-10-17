import java.io.Serializable;

/**
 * Stores commands taht the server will send to the client
 */
public class CommandToClient implements Serializable
{
    // the type of command being sent
    private int command = 0;
    // the data corrisponds to the command
    private Object commandData = null;

    // categories of commands
    public static final int CATEGORY_SELECTION 			= 2;
    public static final int AI_SELECTION 				= 3;
    public static final int START_PLAYER_FIRST_GAMES 	= 4;
    public static final int START_PLAYER_SECOND_GAMES 	= 5;
    public static final int MAKE_MOVE 					= 6;
    public static final int WIN 						= 7;
    public static final int LOSE 						= 8;
    public static final int TIE 						= 9;
    public static final int MATCHES_COMPLETE			= 10;
    public static final int SUCCESSFUL_MOVE				= 11;
    public static final int FAILED_MOVE					= 12;
    public static final int OPPONENT_MOVE				= 13;
    public static final int OPPONENT_FAILED_TO_MOVE		= 14;
    public static final int OPPONENT_SKIP       		= 15;
    public static final int PLAYER_SKIP         		= 16;
    public static final int PLAYER_FAILED_TO_MOVE		= 17;

    /**
     * Creates a type only command and sets the data to null
     * @param command - the type of command
     */
    public CommandToClient(int command)
    {
        this.command	= command;
    }

    /**
     * Creates a command with command data
     * @param command - the command to be performed
     * @param commandData - the data needed to perform the command
     */
    public CommandToClient(int command, Object commandData)
    {
        this.command	 = command;
        this.commandData = commandData;
    }

    /**
     * Returns the command type
     * @return - the command type
     */
    public int getCommand()
    {	return command;	}

    /**
     * Returns the data needed to perform the command
     * @return data for the command, null when not needed
     */
    public Object getCommandData()
    {	return commandData;	}
}