package br.saraceni.ethernetcomm.library;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class MessageReceiveHandler extends Handler {
	
	public static final String TAG = "MessageReceiveHandler";
	public static final int WHAT = 36;
	private MessageReceivedCallback msgReceivedCllbck;
	
	public MessageReceiveHandler(MessageReceivedCallback msgRCBk)
	{
		msgReceivedCllbck = msgRCBk;
	}
	
	@Override
	public void handleMessage(Message msg)
	{
		Log.i(TAG, "Entrou no handleMessage");
		switch(msg.what){
		case WHAT:
			Log.i(TAG, "Entrou no case WHAT:");
			String readMessage = (String) msg.obj;
			msgReceivedCllbck.onReceiveMsg(readMessage);
		}
	}

}
