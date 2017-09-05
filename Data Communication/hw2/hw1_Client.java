import java.io.*;
import java.net.*;



public class hw1_Client {
	public static void main(String args[]) {
		Socket socket = null;
		
		try {
			socket = new Socket("127.0.0.1", 3500);
		
			BufferedReader socket_br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader input_br = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			while(true) {
				String usr_message = input_br.readLine();
				
				if(usr_message.equals("Q")) { 
					socket.close();
					return; 
				}
				// String Server_message = socket_br.readLine();
				
				//System.out.println(Server_message);
				
				bw.write(usr_message + '\n');
				bw.flush();
				
				String server_message = socket_br.readLine();
				System.out.println("I get message from server : " + server_message);
			}
		
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
