package com.services.smslistenerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();

    public  SmsReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null){
                final  Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i =0; i < pdusObj.length; i++){
                    SmsMessage currentMassage = getIncomingMessage(pdusObj[i], bundle);
                    String phoneNumber = currentMassage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMassage.getDisplayMessageBody();
                    Log.i("SmsReceiver", "senderNum: "+senderNum+ "; message  : "+senderNum );

                    Intent showSmsIntent = new Intent(context, SmsReceiverActivity.class);
                    showSmsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, phoneNumber);
                    showSmsIntent.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE, message);
                        context.startActivity(showSmsIntent);
                }
            }
        }catch (Exception e){
            Log.e("SmsReceiver", "Exceptionm SmsReceiver" + e);
        }
    }

    private SmsMessage getIncomingMessage(Object aObject, Bundle bundle) {
        SmsMessage currentSms;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String format = bundle.getString("format");
            currentSms = SmsMessage.createFromPdu((byte[]) aObject, format);
        } else {
            currentSms = SmsMessage.createFromPdu((byte[]) aObject);
        }
        return currentSms;
    }
}
