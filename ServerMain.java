import java.io.*;
import java.net.*;

/**
 * Hosts a blockus game Server
 */
public class ServerMain
{
    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8621);

            while(true)
            {

                Socket connectionToClient = serverSocket.accept();
                ObjectOutputStream os = new
                        ObjectOutputStream(connectionToClient.getOutputStream());
                ObjectInputStream is = new
                        ObjectInputStream(connectionToClient.getInputStream());

                Thread t = new Thread(new ServersListener(os,is));
                t.start();
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
            e.printStackTrace();
        }
    }
}