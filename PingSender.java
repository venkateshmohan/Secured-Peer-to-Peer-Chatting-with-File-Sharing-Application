import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.Cipher;
import java.lang.StringBuffer;

// This class takes care of sending out periodic ping messages to all connected nodes
class PingSender extends Thread
{
			public void run()
			{
				while(true)
				{
						try
						{
							Thread.sleep(15000);
							int pingDone=Peer.pingStatus();
						}
						catch(InterruptedException e)
		        {
		               System.err.println("IOException " + e);
		        }
				}
			}
}
