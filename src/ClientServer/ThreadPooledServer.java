package ClientServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* The following class represent the thread pool class. its the server of the application which handle clients
 * requests. the thread pool listens for clients requests on a specific host & port according the file transport.properties.
 **/
public class ThreadPooledServer implements Runnable {
    // Fields:
    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool; // Can Handling 10 clients

    /**
     * Class Constructor.
     * @param port
     * @throws IOException
     */
    public ThreadPooledServer(int port) throws IOException {
        this.serverPort = port;
        String[] transportProps = Utils.PropertiesReaders.getTransportProperties();
        System.out.println("number of parallel clients: " + Integer.parseInt(transportProps[2]));
        threadPool = Executors.newFixedThreadPool(Integer.parseInt(transportProps[2]));
    }

    /**
     * Overrides the function runnable as a part of implementing the interface Runnable.
     */
    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    break;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            System.out.println("Client Request Was Received.");
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }
}
