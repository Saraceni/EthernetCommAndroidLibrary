package br.saraceni.ethernetcomm.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class EthernetClient {
	
	public static final String TAG = "EthernetClient";
	private Socket socket;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;
	private InetAddress ip_address;
	private int port;
	private ReaderThread rt;
	private MessageReceiveHandler msgReceivedHandler;
	
	public EthernetClient(InetAddress ip_address, int port, MessageReceiveHandler msgReceiveHandler){
		this.ip_address = ip_address;
		this.port = port;
		this.msgReceivedHandler = msgReceiveHandler;
	}
	
	public void conectar(){
		Log.i(TAG, "entrou no conectar do cliente");
		if(socket != null && socket.isConnected()){
			Log.i(TAG, "entrou no socket.isConnected");
			return;
		}
		try{
			Log.i(TAG, "vai tentar criar o socket");
			Log.i(TAG, "endereco ip " + ip_address.toString());
			Log.i(TAG, "porta: " + String.valueOf(port));
			socket = new Socket(ip_address, port);
			Log.i(TAG, "criou o socket");
			InputStreamReader ir = new InputStreamReader(socket.getInputStream());
			bufferedReader = new BufferedReader(ir);
			printWriter = new PrintWriter(socket.getOutputStream());
			rt = new ReaderThread(bufferedReader, msgReceivedHandler);
			Thread t = new Thread(rt);
			t.start();
			Log.i(TAG, "terminou de conectar");
			
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
				
            //Log.i("ethernet_client", exc.getCause().toString());
            //Log.i("ethernet_client", exc.getClass().toString());
			Log.i("ethernet_client", exc.toString());
            exc.printStackTrace();
		}
	}
	
	public void enviaDados(String str){
		if(socket != null && socket.isConnected()){
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
		if(socket == null)
			return;
        try{
        	rt.isWorking = false;
            socket.close();
            bufferedReader.close();
            printWriter.close();
            rt.isWorking = false;
            rt = null;
            socket = null;
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
		if(socket != null){
			return socket.isConnected();
		}
		else
		{
			return false;
		}
	}		

}
