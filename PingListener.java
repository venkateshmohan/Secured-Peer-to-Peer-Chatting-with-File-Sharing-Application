import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.Cipher;
import java.lang.StringBuffer;

// This class listens for messages, hellos and hello acks from other Peers and also receives files
class PingListener extends Thread
{
				Socket socket;
        String friend;

				public PingListener(Socket s,String username)
				{
					socket = s;
          friend = username;
				}

				public void run()
				{
								while(true)
								{
									try
									{
										BufferedReader pIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    InputStream is = socket.getInputStream();

										String ping=pIn.readLine();
										if (ping!= null && !ping.isEmpty())
												{
													//when a newly connected friend send connection details, they are stored in the hash table
                            if(ping.contains("CONNECTED WITH "))
                            {
                                //Fetches details about a new node that connects with it and hashes the information
                                String meta = ping.split("CONNECTED WITH ")[1];
                                friend = meta.split(" #### ")[0];
                                System.out.println(ping.split(" #### ")[0]);
                                String recdkey=ping.split(" #### ")[1];
                                PeerDetails p = new PeerDetails();
                                p.socket = socket;
                                p.pubKey = Peer.retrievePublicKey(recdkey);
                                Peer.myHash(friend,p);
                            }

														//Hello messages are responded to with HelloAck messages
														if(ping.contains("Hello from"))
															{
                                System.out.println(ping);
																PrintWriter pong = new PrintWriter(socket.getOutputStream(), true);
																pong.write("HelloAck from "+Peer.name+"\n");
																pong.flush();
															}

													 //Hello Ack messages and Broadcast messages are displayed out
                            if(ping.contains("Broadcast from ")||ping.contains("HelloAck from"))
                            {
                                System.out.println(ping);
                            }

														//Received messages are decrypted and displayed
                            if(ping.contains("Message from"))
                            {
                                String encryptedMsg = (ping.split(":")[1]).replaceAll(" ","");
                                try
                                {
                                  PublicKey pkey = Peer.fetchPublicKey(friend);
                                  String decryptMessage = Peer.decryptMessage(encryptedMsg, pkey);
                                  System.out.println(friend+":"+decryptMessage);
                                }
                                catch(Exception e)
                              	{
                                System.out.println("Error while decrypting");
                                e.printStackTrace();
                              	}

                            }

														//Files are received and stored under the recv folder
                            if(ping.contains("$"))
                            {
                                String directoryName = "recv/"+Peer.name;
                                File directory = new File(directoryName);
                                if (! directory.exists()){
                                  directory.mkdir();
                                }
                                PrintWriter writer = new PrintWriter("recv\\"+Peer.name+"\\"+friend+".txt", "UTF-8");
                                String[] fileContents =ping.split("#");
                                for(int i =0;i<fileContents.length;i++)
                                {
                                  writer.println(fileContents[i]);
                                }
                                writer.close();
                            }

														/*	 Init code we tried for receiving files
														else if(ping.contains("HelloAck"))
															{
															System.out.println(ping);
              								String receiveUser= ping.split(" ")[2];
              								Socket s = Peer.fetchSocket(receiveUser);
              								Runnable fl= new FileListener(s);
              								new Thread(fl).start();
              								//l.start();
														}*/
												}
									}//End of Try
									catch (Exception e)
									{
										//Indicates that the connected node has crashed
										System.out.println(friend+ " has Disappeared");
										Peer.hashtable.remove(friend);

										try
										{
											Thread.sleep(Long.MAX_VALUE);
										}
										catch(InterruptedException q)
										{
											System.out.println("Cdnt Sleep "+q);

										}

									}

								} // end of while block

				} // end of run block
} // end of class
