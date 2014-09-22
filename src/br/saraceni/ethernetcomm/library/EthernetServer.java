package br.saraceni.ethernetcomm.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class EthernetServer {
	
	public static final String TAG = "EthernetServer";
	private Socket clientSocket;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;
	private int port;
	private ServerSocket serverSocket;
	private MessageReceiveHandler messageReceiveHandler;
	private ReaderThread rt;
	
	public EthernetServer(int port, MessageReceiveHandler messageReceiveHandler)
	{
		this.port = port;
		this.messageReceiveHandler = messageReceiveHandler;
	}
	
	
	public void conectar()
	{
		Log.i(TAG, "entrou no conectar do servidor");
		if(clientSocket != null && clientSocket.isConnected()){
			Log.i(TAG, "entrou no socket.isConnected");
			return;
		}
		try
		{
			Log.i(TAG, "Server port = " + String.valueOf(port));
			Log.i(TAG, "Vai tentar cirar o Server");
			serverSocket = new ServerSocket(port);
			Thread t = new Thread(new ListenForClients());
			t.start();
			Log.i(TAG, "terminou de criar o Server");
		}
		catch(UnknownHostException UHE){
            Log.i("ethernet_client","Nao foi encontrado nenhum IP correspondente!!!", UHE);
            UHE.printStackTrace();
        }
        catch(SecurityException SE){
            Log.i("ethernet_client", "Ha um gerenciador de seguranca que nao permite a conexao!!!");
            SE.printStackTrace();
        }
        catch(NumberFormatException NFE){
            Log.i("ethernet_client","Digite um numero valido para a porta da conexao!");
            NFE.printStackTrace();
        }
        catch(IOException IOE){
            Log.i("ethernet_client","Ocorreu uma IO exception!!!");
            Log.i("ethernet_client", IOE.getMessage());
            IOE.printStackTrace();
        }
		catch(Exception exc){
			Log.i("ethernet_client","Ocorreu uma Excecao!!!");
			if(exc.getMessage() != null)
				Log.i("ethernet_client", exc.getMessage());
			else
				Log.i("ethernet_client", "exc.getMessage() == null");
			Log.i("ethernet_client", exc.toString());
            exc.printStackTrace();
		}
	}
	
	
	public void enviaDados(String str){
		if(clientSocket != null && clientSocket.isConnected()){
			try{
				Log.i("ethernet_client", "vai tentar enviar dado! " + str);
				printWriter.println(str);
				printWriter.flush();
			}
			catch(Exception exc){
				exc.printStackTrace();
			}
		}
	}
	
	public void desconectar(){
		if(serverSocket == null)
			return;
        try{
        	if(rt != null)
        	    rt.isWorking = false;
        	rt = null;
        	if(clientSocket != null)
        		clientSocket.close();
        	serverSocket.close();
        }
        catch(SecurityException SE)
        {
            System.out.println("A Thread nao pode ser interrompida!!!");
            SE.printStackTrace();
        }
        catch(IOException IOE){
            System.out.println("IOException =/");
            IOE.printStackTrace();
        }
    }
	
	public boolean isConnected(){
		if(clientSocket != null){
			return clientSocket.isConnected();
		}
		else
		{
			return false;
		}
	}	
	
	private class ListenForClients implements Runnable {

		@Override
		public void run() {
			try{
				clientSocket = serverSocket.accept();
				InputStreamReader ir = new InputStreamReader(clientSocket.getInputStream());
				bufferedReader = new BufferedReader(ir);
				printWriter = new PrintWriter(clientSocket.getOutputStream());
				rt = new ReaderThread(bufferedReader, messageReceiveHandler);
				Thread t = new Thread(rt);
				t.start();
			}
			catch(Exception exc)
			{
				Log.i(TAG, "Erro ao tentar conectar ao client!");
				Log.i(TAG, exc.toString());
				Log.i(TAG, exc.getMessage());
			}
		}
		
		/*@Override
		public void run()
		{
			while(true)
			{
				try
				{
					clientSocket = serverSocket.accept();
				}
			}
		}*/
		
	}
	
}



















