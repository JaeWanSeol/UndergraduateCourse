import java.io.*;
import java.net.*;



public class hw2_Client {
	public static void main(String args[]) {
		Socket socket = null;
		
		try {
			socket = new Socket("127.0.0.1", 3500);
		
			BufferedReader socket_br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader input_br = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			String usr_message = input_br.readLine();
			
			if(usr_message.equals("SEND")) {
				bw.write("SEND\n");
				bw.flush();
				
				System.out.println("Start Sending");
				while(true) {
					usr_message = input_br.readLine();
					if(usr_message.equals("Q")) {
						System.out.println("End Sending");
						socket.close();
						break;
					}
					else {
						bw.write(usr_message + '\n');
						bw.flush();
					}
				}
			}
			
			else if(usr_message.equals("RECV")) {
				String message_from_server;
				System.out.println("Start Receiving");
				bw.write("RECV\n");
				bw.flush();
				while(true) {
					message_from_server = socket_br.readLine();
					System.out.println(message_from_server);
					if(message_from_server.equals("LAST_MSG")) {
						System.out.println("End Receiving");
						socket.close();
						break;
					}
				}
			}
			
			else {
				socket.close();
			}
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
