import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Connectionworker extends Thread{
	
	private java.net.Socket socket ;
	
	public Connectionworker(java.net.Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//TODO R
			String Requests = "";
			for (int i = 0; i < 5; i++) {
				Requests += in.readLine() +"\n";
			}
			String[] Words = Requests.split(" ");
			System.out.println(Requests);
			//TODO W
			if ((new File("."+Words[1])).exists() == true) {
				if (Words[1].equals("/") || Words[1].equals("/en") || Words[1].equals("/en/")
						|| Words[1].equals("/en/main_en.html")) {
					outputStream.write(text$html("./en/main_en.html").getBytes());
				} else if (Words[1].equals("/ar") || Words[1].equals("/ar/") || Words[1].equals("/ar/main_ar.html")) {
					outputStream.write(text$html("./ar/main_ar.html").getBytes());
				}
				
				if (Words[1].contains(".css")) {
					outputStream.write(text$CSS(Words[1]).getBytes());
				}
				
				if (Words[1].contains("images")) {
//					outputStream.write(text$CSS(Words[1]).getBytes());
					outputStream.write(image$(Words[1]).getBytes());
				}
			} 
			else {
				if (Words[1].contains("/go")) {
					outputStream.write((Temporary_Redirect("https://www.google.com/")).getBytes());
				}else if (Words[1].contains("/cn")) {
					outputStream.write((Temporary_Redirect("https://edition.cnn.com/")).getBytes());
					
				}else if (Words[1].contains("/bzu")) {
					outputStream.write((Temporary_Redirect("https://www.birzeit.edu/")).getBytes());
				}else {
					outputStream.write(Error404().getBytes());	
				}
				
				
			}

			outputStream.flush();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {}
			}
			if (socket != null) {
				try {
					
					socket.close();
				} catch (IOException e) {}
			}
			System.out.println("...................................");
		}
	}
	private static String ReadFile(String PATH) {
		try {
			File file = new File(PATH);

		    Scanner sc = new Scanner(file);
		    StringBuilder stringBuilder = new StringBuilder();
		    while (sc.hasNextLine()) {
		    	stringBuilder.append(sc.nextLine());
			}
		    String File = stringBuilder.toString();
		    sc.close();
			return File;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	private String text$html(String path) {
		String HTML = ReadFile(path);
		final String CRLF = "\n\r";
		String Response = 
				"HTTP/1.1 200 OK" + CRLF +
				"Date: " + new Date() + CRLF +
				"Server: Java simple HTTP Server from wael ziada 1191085" + CRLF +
				"Content-Length" + HTML.getBytes().length + CRLF + 
				"Content-Type: text/html" + CRLF + 
				CRLF +
				HTML +
				CRLF + CRLF
				
				;
		
		System.out.println("Response" + CRLF +Response);
		return Response;
	}
	private String text$CSS(String path) {
		String CSS = ReadFile("."+path);
		final String CRLF = "\n\r";
		String Response = 
				"HTTP/1.1 200 OK" + CRLF +
				"Date: " + new Date() + CRLF +
				"Server: Java simple HTTP Server from wael ziada 1191085" + CRLF +
				"Content-Length: " + CSS.getBytes().length + CRLF + 
				"Content-Type: text/CSS" + CRLF + 
				CRLF +
				CSS +
				CRLF + CRLF
				
				;
		
		System.out.println("Response" + CRLF +Response);
		return Response;
	}
	
	private String image$(String path) throws IOException {

		File file = new File("."+path);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		String data = Base64.getEncoder().encodeToString(fileContent);
		String Content_Type = "";
		if (path.contains(".png")) {
			Content_Type = "png";
		}else if (path.contains(".jpg")){
			Content_Type = "jpg";
		}
		
		final String CRLF = "\r\n";
		
		String Response = 
				"HTTP/1.1 200 OK" + CRLF +
				"Date: " + new Date() + CRLF + 
				"Server: Java simple HTTP Server from wael ziada 1191085" + CRLF +
				"Content-Length: "+ data.getBytes().length + CRLF +
				"Content-Type: image/"+Content_Type + CRLF + 
				CRLF +
				data +
				CRLF + CRLF 
				;
//		System.out.println("Response" + CRLF +Response);
        return Response;

	}
	private String Error404() {
		String html = ReadFile("./error/404.html");
		final String CRLF = "\n\r";
		String Response = 
				"HTTP/1.1 404 NotFound" + CRLF +
				"Date: " + new Date() + CRLF +
				"Server: Java simple HTTP Server from wael ziada 1191085" + CRLF +
				"Content-Length: " + html.getBytes().length + CRLF + 
				"IP: "+this.socket.getInetAddress()+",port number: " +this.socket.getLocalPort() + CRLF +
				"Content-Type: text/html" + CRLF + 
				CRLF +
				html+
				CRLF + CRLF
				
				;
		System.out.println("Response" + CRLF +Response);
		return Response;
	}
	private String Temporary_Redirect(String URL) {
		final String CRLF = "\n\r";
		String Response = 
				"HTTP/1.1 307 Temporary Redirect" + CRLF +
				"Date: " + new Date() + CRLF +
				"Server: Java simple HTTP Server from wael ziada 1191085" + CRLF +
				"Refresh: 0; url="+ URL + CRLF 
				;
		System.out.println("Response" + CRLF +Response);
		return Response;
	}
}

/*
 * String googleString =
				"HTTP/1.1 200 OK"+ CRLF+
				"Refresh: 0; url=http://www.google.com";
*/

