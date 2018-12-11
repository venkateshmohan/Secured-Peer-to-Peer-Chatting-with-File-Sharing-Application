import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.Cipher;
import java.lang.StringBuffer;

//This Thread actively listens for new connections from users
class Listener extends Thread
{
			Integer port;
			public Listener(Integer p)
			{
				port = p;
			}
			public void run()
			{
						ServerSocket serverSocket = null;
						Socket clientSocket = null;
						try
						{
							serverSocket = new ServerSocket(port);
						}
						catch (IOException e)
						{
							System.out.println("Could not listen on port"); System.exit(-1);
						}

						while(true)
						{
									try
									{
										System.out.println("Looking out for new friends...");
										clientSocket = serverSocket.accept();
                    PingListener l=new PingListener(clientSocket,"");
										l.start();

                    //To send details about self
										PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
										out.write("CONNECTED WITH "+Peer.name+" #### "+Peer.savePublicKey(Peer.publicKey)+"\n");
										out.flush();

									}
									catch (IOException e)
									{
										System.out.println("Accept failed"); System.exit(-1);
									}
						}
			}

} //End of Listener class
