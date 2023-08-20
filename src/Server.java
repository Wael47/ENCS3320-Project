import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;

public class Server extends Thread{
	
	private int port ;
	private ServerSocket serverSocket ;
	
	
	
	public Server(int port) throws IOException {
		this.port = port;
		this.serverSocket = new ServerSocket(port);
	}

	public int getPort() {
		return port;
	}

	@Override
	public void run() {
		
		try {
			
			
			while (serverSocket.isBound() && !serverSocket.isClosed()) {
				
				java.net.Socket socket = serverSocket.accept();
				Connectionworker connectionworker = new Connectionworker(socket);
				connectionworker.start();
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {}
			}
		}
	}
}
