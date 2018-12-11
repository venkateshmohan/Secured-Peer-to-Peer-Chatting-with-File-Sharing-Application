import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.*;
import java.util.Random;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.*;
import java.lang.Math.*;
import java.lang.Runnable;

// This file listener thread is not part of the running implementation but is the one we tried before the current set up
class FileListener extends Thread implements Runnable
{
   Socket s;
   InputStream is;
   FileOutputStream fos;
   BufferedInputStream bis;
   int bufferSize;
   DataInputStream dis;
   BufferedOutputStream bos;

   public FileListener(Socket client)
   {
   s=client;
   is = null;
   fos = null;
   bis = null;
   bos=null;
   dis=null;
   bufferSize = 0;
   }

   public void run()
   {
    System.out.println("I am into FileListener");
   while(true) {
       try {
           is = s.getInputStream();
           bufferSize = s.getReceiveBufferSize();
           System.out.println("Buffer size: " + bufferSize);
           //DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
           //bis = new BufferedInputStream(is,bufferSize);
           //dis= new DataInputStream(bis);
           //long fileLength= dis.readLong();
           int count = 0;
           int total = 0;
           //long remaining = fileLength;
           //System.out.println("length----" + fileLength);
           //String filename=dis.readUTF();
           System.out.println("into try");
           //System.out.println("name----" + filename);
           File file=new File("x.txt");
           //OutputStream pw = new FileOutputStream(file);
           //Writer outputStream = new OutputStreamWriter(pw);
           fos = new FileOutputStream(file);
           //DataOutputStream dos = new DataOutputStream(fos);
           //FileWriter fw=new FileWriter(file);
           //PrintWriter pw =new PrintWriter(fw);
           //bos=new BufferedOutputStream(fos);
         try{
           Thread.sleep(2000);
         } catch(InterruptedException e){}
           //for(int j = 0; j < fileLength; j++)
           byte[] bytes = new byte[bufferSize];
           //count = is.read(bytes,0,bufferSize);
           //System.out.println(count);
           while ((count = is.read(bytes,0,bufferSize)) != -1) {
              //System.out.println("xxxx");
               //Peer.getSize(bytes,remaining)
               //total+=count;
               //System.out.println("read"+total+"bytes");
              fos.write(bytes, 0, count);
              fos.flush();
           }
           //pw.write(Peer.readFile(file));
           //outputStream.write(Peer.readFile(file));
           //outputStream.flush();
           //outputStream.close();
           System.out.println("data written successfully");
           /*byte[] bytes = new byte[bufferSize];
           int count;
           while ((count = ds.read(bytes)) >= 0) {
               ds.write(bytes, 0, count);
           }*/
           //bos.close();
           fos.close();
           is.close();
           s.close();
           //dis.close();

       } catch (IOException e) {
           e.printStackTrace();
       }
       catch (NullPointerException e){}
   }


   }
}
