import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;
import java.security.*;
import javax.crypto.Cipher;
import java.lang.StringBuffer;

//The CommandListener class actively listens to any user input on the command line for instructions and proccesses them accordingly
class CommandListener extends Thread
{
			public CommandListener()
			{}

			public void run()
			{
				while(true)
				{
					Scanner scanner = new Scanner(System.in);
					while(scanner.hasNextLine())
					{
								String command=scanner.nextLine();

						//If the command is 'find friend',
						//the below code finds new users in the chat room that the node is not connected with and helps establish connections with them.
								if(command.equals("find friend"))
										{
                      String line="";
                      try
                      {
                        BufferedReader reader2 = new BufferedReader(new FileReader("peers.csv"));
                        String newPortNumbers=""; String newUserList="";
                        while ((line = reader2.readLine()) != null)
                        {
                          String readname= line.split(",")[0];
                          int flag = Peer.isFriend(readname);
                          if(flag==0) //check if not one of your existing connections
                          {
                            if(!(readname.equals(Peer.name)))
                            {
                              newUserList = newUserList+ readname+",";
                              newPortNumbers= newPortNumbers + readname+":"+line.split(",")[1]+",";
                            }
                          }
                        }
                        if (!newUserList.equals(""))
                        {
                          System.out.println("NEW USERS IN THE CHAT ROOM ARE - "+newUserList);
                          System.out.println("How many of them do you want to connect with?");
                          int n =scanner.nextInt();
                          String catchEmpty = scanner.nextLine();
                          for (int i=0;i<n;i++)
                          {
                            System.out.println("Enter a name you want to connect with");
                            String friend = scanner.nextLine();
                            if(newPortNumbers.contains(friend))
                            {
                              String temp = newPortNumbers.split(friend+":")[1];
                              String friendPort = temp.split(",")[0];
                              Integer connect_port = Integer.parseInt(friendPort);
                              System.out.println("Your friend "+friend+ " is at "+friendPort);
                              Peer.connect(connect_port,Peer.myListenPort);
                            }
                          }
                        }
                        else
                        {
                          System.out.println("No new users");
                        }
                      }
                      catch(IOException e) {}
										}
//******************************************************************************************************************************************
							//Send broadcast messages to all its connected nodes
                    if(command.contains("broadcast"))
                    {
                      String message = command.split("broadcast ")[1];
                      String connectedUsers;
                      connectedUsers = Peer.readHash();
                      //if(connectedUsers.equals(""))
                      String[] friendList = connectedUsers.split(",");
                      for(int i =0;i<friendList.length;i++)
                      {
                        Socket t = Peer.fetchSocket(friendList[i]);
                        try
                        {
                            PrintWriter pOut = new PrintWriter(t.getOutputStream(), true);
                            pOut.write("Broadcast from "+Peer.name+": "+message+"\n");
                            pOut.flush();

                        }
                        catch (NullPointerException e){}
                        catch (IOException e) {}
                      }

                    }
//******************************************************************************************************************************************
								  // Allows user to chat with a specific friend
									if(command.contains("chat"))
                    {
                      String details = command.split("chat ")[1];
                      String meta = details.split("@ ")[1];
                      String friend = meta.split(" ",2)[0];
                      String message = meta.split(" ",2)[1];
                      Socket t = Peer.fetchSocket(friend);

                        try
                        {
                            String encryptedMsg = Peer.encryptMessage(message, Peer.privateKey);
                            PrintWriter pOut = new PrintWriter(t.getOutputStream(), true);
                            pOut.write("Message from "+Peer.name+": "+encryptedMsg+"\n");
                            pOut.flush();

                        }
                        catch (NullPointerException e){}
                        catch (IOException e) {}
                        catch (Exception e) {System.out.println("Error while encrypting");}

                    }
//******************************************************************************************************************************************
								    //Takes care of sending files
                    if (command.contains("send file"))
                    {
                      System.out.println("Whom do you want to send file to?");
                      String friend = scanner.nextLine();
                      Socket s = Peer.fetchSocket(friend);

                      try
                      {
                        File file = new File("send\\apple.txt");
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();
                        while (line != null)
                        {
                          sb.append(line).append("#");
                          line = br.readLine();
                        }
                        String fileAsString = sb.toString();
                        PrintWriter pOut = new PrintWriter(s.getOutputStream(), true);
                        pOut.write(fileAsString+"\n");
                        pOut.flush();

                      }
                      catch(FileNotFoundException e){}
                      catch(IOException e){}

										/* Below is the code we initially implemented for file sending
											try
                      {
                        File file=null;
                        file = Peer.createFile();
                        InputStream is;
                        BufferedOutputStream bos;
                        DataOutputStream dos1;
                        int bSize=s.getSendBufferSize();
                        OutputStream out1 = s.getOutputStream();
                        //dos1= new DataOutputStream(out1);
                        //long length= file.length();
                        //dos1.writeLong(length);
                        //String name= file.getName();
                        //dos1.writeUTF(name);
                        //System.out.println("length is---"+ length+"name is----"+name);
                        byte[] buffer = new byte[bSize];
                        is = new FileInputStream(file);
                        //bos = new BufferedOutputStream(out1,bSize);
                        int count;
                        while ((count = is.read(buffer,0,bSize)) > 0) {
                            out1.write(buffer, 0, count);
                          }
                          //out1.flush();
                          //System.out.println("buffer" + buffer);
                          //is.close();
                          //socket.close();
                          //out1.close();
                          //fis.close();
                          //bis.close();

                      }
                      catch(FileNotFoundException e){}
                      catch(IOException e) {}
											*/

                    }
//******************************************************************************************************************************************
				}
			} // end of while

		} // end of run block
} // end of class
