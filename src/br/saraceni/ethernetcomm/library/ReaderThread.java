package br.saraceni.ethernetcomm.library;

import java.io.IOException;
import java.io.BufferedReader;

import android.util.Log;

public class ReaderThread implements Runnable {
	
	public static final String TAG = "ReaderThread";
	public volatile boolean isWorking = true;
	private BufferedReader bufferedReader;
	private MessageReceiveHandler messageReceiveHandler;
	
	public ReaderThread(BufferedReader bufferedReader,  MessageReceiveHandler messageReceiveHandler)
	{
		this.bufferedReader = bufferedReader;
		this.messageReceiveHandler = messageReceiveHandler;
	}

	public void run() {
		while(isWorking){
			String message;
			try{
				while((message = bufferedReader.readLine()) != null){
					Log.i(TAG, message);
					messageReceiveHandler.obtainMessage(MessageReceiveHandler.WHAT, message).sendToTarget();
				}
			}
			catch(IOException exc){
				exc.printStackTrace();
			}
		}
		
	}

}
