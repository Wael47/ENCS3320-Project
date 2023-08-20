import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.http.HttpHeaders;
import java.util.Date;
import java.util.Scanner;
public class HttpServer {
	

	
	private static int port = 9000;

	public static void main(String[] args) {
		
		System.out.println("Server is running on port: "+port );
		try {
			Server server = new Server(port);
			
			server.start();
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
