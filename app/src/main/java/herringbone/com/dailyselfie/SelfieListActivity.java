package herringbone.com.dailyselfie;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SelfieListActivity extends ListActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String EXTRA_MESSAGE = "Extra_Message";

    private SelfieAdapter mAdapter;
    String mCurrentPhotoPath;
    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent;
    private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
    private static final long ALARM_INTERVAL = 2 * 60 * 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ListView placesListView = getListView();
        // Get the AlarmManager Service
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(SelfieListActivity.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                SelfieListActivity.this, 0, mNotificationReceiverIntent, 0);

        mAdapter = new SelfieAdapter(getApplicationContext());
        setListAdapter(mAdapter);

        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                ALARM_INTERVAL, mNotificationReceiverPendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + "/Selfie");
        if(storageDir.exists()) {
            //Get all files
            File[] files = storageDir.listFiles();
            for (File file : files) {
                SelfieRecord record = new SelfieRecord();
                record.setmRecordLocation(file.getAbsolutePath());
                mAdapter.add(record);
            }
        }
    }

    @Override
    protected void onListItemClick (ListView l, View v, int position, long id) {
        SelfieRecord clickedRecord = (SelfieRecord)getListAdapter().getItem(position);
        Intent intent = new Intent(this, SelfieDetailActivity.class);
        String location = clickedRecord.getmRecordLocation();
        intent.putExtra(EXTRA_MESSAGE, location);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.custom_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            dispatchTakePictureIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //create the adapter record
            SelfieRecord record = new SelfieRecord();
            record.setmRecordLocation(mCurrentPhotoPath);
            addNewSelfie(record);
        }
    }

    private void addNewSelfie(SelfieRecord record) {
        mAdapter.add(record);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + "/Selfie");
        if(!storageDir.exists()) {
            if(storageDir.mkdirs()) {
                Log.i("Daily Selfie", "Directory Created...");
            }else{
                Log.i("DailySelfie", "Directory Not Created ..........:");
            }
        }else{
            Log.i("DailySelfie", "Directory Exists ..........:");
        }
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
