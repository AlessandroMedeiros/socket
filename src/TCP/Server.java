package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

  public final static int SOCKET_PORT = 5000;
  private static final String sourceFilePath = "files/file 10000.txt"; // 1500 ou 10000
  private static final String destinationPath = "recebidas/";

  public static void main(String[] args) throws IOException {
    File f = new File(destinationPath);
    if (!f.exists()) {
      f.mkdirs();
    }

    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    ServerSocket servsock = null;
    Socket sock = null;
    try {
      servsock = new ServerSocket(SOCKET_PORT);
      while (true) {
        System.out.println("Waiting...");
        try {
          sock = servsock.accept();
          System.out.println("Accepted connection : " + sock);
          File myFile = new File(sourceFilePath);
          byte[] mybytearray = new byte[(int) myFile.length()];
          fis = new FileInputStream(myFile);
          bis = new BufferedInputStream(fis);
          bis.read(mybytearray, 0, mybytearray.length);
          os = sock.getOutputStream();
          System.out.println("Sending " + sourceFilePath + "(" + mybytearray.length + " bytes)");
          os.write(mybytearray, 0, mybytearray.length);
          os.flush();
          System.out.println("Done.");
        } finally {
          if (bis != null) bis.close();
          if (os != null) os.close();
          if (sock != null) sock.close();
        }
      }
    } finally {
      if (servsock != null) servsock.close();
    }
  }
}