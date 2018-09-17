package com.nazran.filtering;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    public static final String MY_ACTION = "MY_ACTION";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new MyAsyncTask().execute();
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getContactList();
            return null;
        }
    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.e(TAG, "Name: " + name);
                        Log.e(TAG, "Phone Number: " + phoneNo);

                        Intent intent = new Intent(MY_ACTION);
                        intent.putExtra("name", name);
                        intent.putExtra("phone", phoneNo);
                        sendBroadcast(intent);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
    }
}
