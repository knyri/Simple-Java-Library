/* µCOMP:javac -classpath C:\jbuilder8\jdk1.4\temp %sµ */
package simple.net;

import java.io.*;
import java.net.*;
/**
 * Synchronized class for connecting and sending to network nodes.<br>
 * Methods are fully functional, but missing methods on the read side.<br>
 * Abandoned<br>
 * Created: 2005
 * @author Kenneth Pierce
 * @version .5
 */
public final class Connection extends Thread {
  private DataInputStream in = null;
  private DataOutputStream out = null;
  private Socket talk = null;
  private String server = "127.0.0.1";
  private int port = 80;
  private boolean keepalive = true;
  public void run() {
    try {
      if (talk == null) {
	  talk = new Socket(server, port);
      }
      in = new DataInputStream(talk.getInputStream());
      out = new DataOutputStream(talk.getOutputStream());
      talk.setKeepAlive(keepalive);
    }
    catch (IOException ex) {
    }
  };
  public Connection() {}
  public Connection(String server, int port) {
    this.server = server;
    this.port = port;
  }
  public Connection(String server, int port, boolean keepalive) {
    this.server = server;
    this.port = port;
    this.keepalive = keepalive;
  }
  public Connection(Socket socket) {
    this.talk = socket;
  }
  public void connect() {
    this.start();
  }
  public void connect(String server, int port) {
    this.server = server;
    this.port = port;
    this.start();
  }
  public void connect(String server, int port, boolean keepalive) {
    this.server = server;
    this.port = port;
    this.keepalive = keepalive;
    this.start();
  }
  public boolean disconnect() {
    try {
      talk.close();
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public boolean isConnected() {
    if (talk == null) {
      return false;
    }
    if (talk.isConnected()) {
      return true;
    } else {
      return false;
    }
  }
//Send Functions: byte&[], string, object, int, float, double, short, long, char&[]
  public synchronized boolean send(byte[] data) {
    try {
      out.write(data);
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(byte data) {
    try {
      out.write(data);
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(String data) {
    try {
      out.write(data.getBytes());
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(int data) {
    try {
      out.writeInt(data);
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(Object data) {
    byte[] b = data.toString().getBytes();
    try {
      out.write(b);
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(float data) {
    try {
      out.writeFloat(data);
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(double data) {
    try {
      out.writeDouble(data);
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(long data) {
    try {
      out.writeLong(data);
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(short data) {
    try {
      out.writeShort(data);
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(char data) {
    try {
      out.writeChar(data);
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
  public synchronized boolean send(char[] data) {
    try {
      out.writeChars(new String(data));
    }
    catch (IOException ex) {
      return false;
    }
    return true;
  }
//--------------end send functions--------------------
  public synchronized String get() {
    try {
      byte b;
	  b = in.readByte();
	  byte c[] = new byte[in.available() + 1];
	  in.read(c, 0, c.length - 1);
	  for (int i = c.length - 1; i > 0; i--) {
	    c[i] = c[i - 1];
	  }
	  c[0] = b;
	  return new String(c);
      //b = new byte[in.available()];
      //in.read(b,0,in.available());
    }
    catch (IOException ex) {
      return null;
    }
  }
  public synchronized String getIP() {
    return talk.getInetAddress().getHostAddress();
  }
}