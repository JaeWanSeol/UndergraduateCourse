import java.net.*;
import java.io.*;



public class hw1_Server {
	public static void main(String args[]) {
		try {
			ServerSocket s_socket = new ServerSocket(3500);
		
			while(true) {
				try {
					Socket socket = s_socket.accept();
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					while(true) {
						
						String message = br.readLine(); // delete \n
						if(message != null && !message.equals("null")) {
						
						
						System.out.println("I got message from client : " + message);
						
						bw.write(message + '\n');
						bw.flush();
						
						}
						
						else {
							break;
						}
					}
				
				}
			
				catch (IOException e) {
					e.printStackTrace();
					s_socket.close();
					
				}
			}
			
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
