/* µCOMP:javac -classpath C:\jbuilder8\jdk1.4\temp %sµ */
package simple.net;

import java.net.*;
import java.io.*;
import java.nio.channels.ServerSocketChannel;

import simple.util.StaticDebug;
/**
 * Doesn't accept connections yet.
 * <br>Created: Jun 19, 2005
 * <br>Abandoned
 * @author Kenneth Pierce
 * @version .5
 * @deprecated
 */
public final class Server {
  ServerSocket p = null;
  int port;
  Socket[] cons = new Socket[1];
  public Server(int port) {
    this.port = port;
  }
  public void connect() {
     StaticDebug.debug("simple.net.Server#connect()","pass");
    try {
      p = new ServerSocket(port);
    }
    catch (IOException ex) {
      StaticDebug.error(this.toString(),ex);
    }
  }
  public boolean disconnect() {
    try {
      p.close();
      return true;
    }
    catch (IOException ex) {
      return false;
    }
  }
  public ServerSocketChannel getChannel() {
    return p.getChannel(); //ServerSocketChannel
  }
  public InetAddress getInetAddress() {
    return p.getInetAddress(); //InetAddress
  }
  public int getLocalPort() {
    return p.getLocalPort(); //int
  }
  public SocketAddress getLocalSocketAddress() {
    return p.getLocalSocketAddress(); //SocketAddress
  }
  public int getRecieveBufferSize() {
    try {
      return p.getReceiveBufferSize(); //int
    }
    catch (SocketException ex) {
      return 0;
    }
  }
  public boolean getReuseAddress() {
    try {
      return p.getReuseAddress(); //boolean
    }
    catch (SocketException ex) {
      return false;
    }
  }
  public int getSoTimeout() {
    try {
      return p.getSoTimeout(); //int
    }
    catch (IOException ex) {
      return 0;
    }
  }
  public boolean getIsBound() {
    return p.isBound(); //boolean
  }
  public boolean getIsClosed() {
    return p.isClosed(); //boolean
  }
  public boolean setRecieveBufferSize(int bytes) {
    try {
      p.setReceiveBufferSize(bytes); //int
      return true;
    }
    catch (SocketException ex) {
      return false;
    }
  }
  public boolean setReuseAddress(boolean value) {
    try {
      p.setReuseAddress(value); //boolean
      return true;
    }
    catch (SocketException ex) {
      return false;
    }
  }
  public boolean setSoTimeout(int milli) {
    try {
      p.setSoTimeout(milli); //int
      return true;
    }
    catch (IOException ex) {
      return false;
    }
  }
  public Socket acceptConnection() {
    try {
      Socket temp = p.accept();
      return temp;
    }
    catch (IOException ex) {
      return null;
    }
  }
  public Socket getConnection(int index) {
    return cons[index];
  }
  public Socket[] getConnections() {
    return cons;
  }
  public void closeConnection(int index) throws IOException {
    cons[index].close();
    int i;
    for (i = cons.length-1;cons[i] == null;i--);
    System.arraycopy(cons,index+1,cons,index,index-(i));
    cons[i] = null;
  }
  public void closeConnections() {
    for (int i = 0;i<cons.length;i++) {
      try {
	cons[i].close();
      }
      catch (IOException ex) {
      }
    }
    cons = null;
  }
  public void addConnection(Socket con) {
    int i;
    for (i = 0;(cons[i] != null)||(i<cons.length);i++);
    if (i == (cons.length-1)) {
      Socket[] temp = new Socket[cons.length + 1];
      System.arraycopy(cons, 0, temp, 0, cons.length);
      temp[cons.length] = con;
      cons = new Socket[cons.length + 1];
      System.arraycopy(temp, 0, cons, 0, temp.length);
    } else {
      cons[i] = con;
    }
  }
}