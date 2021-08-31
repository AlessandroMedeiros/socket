package UDP;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
  private DatagramSocket socket = null;
  private FileEvent event = null;
  private static final String sourceFilePath = "files/file 10000.txt"; // 1500 ou 10000
  private static final String destinationPath = "recebidas/";
  private String hostName = "localHost";

  public Client() {

  }

  public void createConnection() {
    try {

      socket = new DatagramSocket();
      InetAddress IPAddress = InetAddress.getByName(hostName);
      byte[] incomingData = new byte[999999999];
      event = getFileEvent();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(outputStream);
      os.writeObject(event);
      byte[] data = outputStream.toByteArray();
      DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 9876);
      socket.send(sendPacket);
      System.out.println("File sent from client");
      System.exit(0);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public FileEvent getFileEvent() {
    FileEvent fileEvent = new FileEvent();
    String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("/") + 1);
    String path = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("/") + 1);
    fileEvent.setDestinationDirectory(destinationPath);
    fileEvent.setFilename(fileName);
    fileEvent.setSourceDirectory(sourceFilePath);
    File file = new File(sourceFilePath);
    if (file.isFile()) {
      try {
        DataInputStream diStream = new DataInputStream(new FileInputStream(file));
        long len = (int) file.length();
        byte[] fileBytes = new byte[(int) len];
        int read = 0;
        int numRead = 0;
        while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
          read = read + numRead;
        }
        fileEvent.setFileSize(len);
        fileEvent.setFileData(fileBytes);
        fileEvent.setStatus("Success");
      } catch (Exception e) {
        e.printStackTrace();
        fileEvent.setStatus("Error");
      }
    }
    return fileEvent;
  }

  public static void main(String[] args) {
    File f = new File(destinationPath);
    if (!f.exists()) {
      f.mkdirs();
    }
    Client client = new Client();
    client.createConnection();
  }
}