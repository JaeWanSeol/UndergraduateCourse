import java.net.*;
import java.io.*;
import java.util.*;


public class hw2_Server {
	public static void main(String args[]) {
		try {
			ServerSocket s_socket = new ServerSocket(3500);
			Queue<String> q1 = new LinkedList<String>();
	
			while(true) {
				try {
					Socket socket = s_socket.accept();
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					while(true) {
						
						String message_from_client = br.readLine(); // delete \n
						
						if(message_from_client == null || message_from_client.equals("null")) {
							System.out.println("Connection Terminated");
							break;
						}

						else if(message_from_client.equals("SEND")) {
							while(true) {
								message_from_client = br.readLine();
								if(message_from_client != null && !message_from_client.equals("null")) {
									q1.offer(message_from_client);
									System.out.println(message_from_client + " is cumulating");
								}
								else {
									break;
								}

							}
						}

						else if(message_from_client.equals("RECV")) {
							while(!q1.isEmpty()) {
								bw.write(q1.poll() + '\n');
								bw.flush();
							}
							System.out.println("End Flushing");
							bw.write("LAST_MSG\n");
							bw.flush();
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
