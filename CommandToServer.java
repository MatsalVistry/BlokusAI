import java.io.Serializable;

/**
 * Stores commands taht the server will send to the client
 */
public class CommandToServer implements Serializable
{
    // the type of command being sent
    private int command 					= 0;
    // the data corrisponds to the command
    private Object commandData				= null;

    // categories of commands
    public static final int MOVE 			= 1;
    public static final int NEW_MATCH 		= 2;
    public static final int SELECT_CATEGORY = 3;
    public static final int SELECT_AI 		= 4;


    /**
     * Creates a type only command and sets the data to null
     * @param command - the type of command
     */
    public CommandToServer(int command)
    {
        this.command	= command;
    }

    /**
     * Creates a command with command data
     * @param command - the command to be performed
     * @param commandData - the data needed to perform the command
     */
    public CommandToServer(int command, Object commandData)
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