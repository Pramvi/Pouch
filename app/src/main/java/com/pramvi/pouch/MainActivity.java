package com.pramvi.pouch;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    NfcAdapter nfcAdapter;
    boolean writeMode;
    Context context;
    IntentFilter tagDetected;
    PendingIntent pi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=MainActivity.this;
        String nfcMessage = "Abhishek";

        // When an NFC tag comes into range, call the main activity which handles writing the data to the tag
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        Intent nfcIntent = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        nfcIntent.putExtra("nfcMessage", nfcMessage);
        pi = PendingIntent.getActivity(context, 0, nfcIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        nfcAdapter.enableForegroundDispatch((Activity) context, pi, new IntentFilter[]{tagDetected}, null);

    }

    public NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = payload.getBytes(utfEncoding);
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }
    //@Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pi,  new IntentFilter[]{tagDetected}, null);
    }

    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onNewIntent(Intent intent) {
        // When an NFC tag is being written, call the write tag function when an intent is
        // received that says the tag is within range of the device and ready to be written to
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String nfcMessage = intent.getStringExtra("nfcMessage");

        if(nfcMessage != null) {
            writeTag(this, tag, nfcMessage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static boolean writeTag(Context context, Tag tag, String data) {
        // Record to launch Play Store if app is not installed
        NdefRecord appRecord = NdefRecord.createApplicationRecord(context.getPackageName());

        // Record with actual data we care about
        NdefRecord relayRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                new String("application/" + context.getPackageName())
                        .getBytes(Charset.forName("US-ASCII")),
                null, data.getBytes());

        // Complete NDEF message with both records
        NdefMessage message = new NdefMessage(new NdefRecord[] {relayRecord, appRecord});

        try {
            // If the tag is already formatted, just write the message to it
            Ndef ndef = Ndef.get(tag);
            if(ndef != null) {
                ndef.connect();

                // Make sure the tag is writable
                if(!ndef.isWritable()) {
                    DialogUtils.displayErrorDialog(context, R.string.nfcReadOnlyErrorTitle, R.string.nfcReadOnlyError);
                    return false;
                }


                // Check if there's enough space on the tag for the message
                int size = message.toByteArray().length;
                if (ndef.getMaxSize() <= size) {
                    DialogUtils.displayErrorDialog(context, R.string.nfcBadSpaceErrorTitle, R.string.nfcBadSpaceError);
                    return false;
                }

                try {
                    // Write the data to the tag
                    ndef.writeNdefMessage(message);

                    DialogUtils.displayErrorDialog(context, R.string.nfcWrittenTitle, R.string.nfcWritten);
                    return true;
                } catch (TagLostException tle) {
                    DialogUtils.displayErrorDialog(context, R.string.nfcTagLostErrorTitle, R.string.nfcTagLostError);
                    return false;
                } catch (IOException ioe) {
                    DialogUtils.displayErrorDialog(context, R.string.nfcFormattingErrorTitle, R.string.nfcFormattingError);
                    return false;
                } catch (FormatException fe) {
                    DialogUtils.displayErrorDialog(context, R.string.nfcFormattingErrorTitle, R.string.nfcFormattingError);
                    return false;
                }
                // If the tag is not formatted, format it with the message
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if(format != null) {
                    try {
                        format.connect();
                        format.format(message);

                        DialogUtils.displayErrorDialog(context, R.string.nfcWrittenTitle, R.string.nfcWritten);
                        return true;
                    } catch (TagLostException tle) {
                        DialogUtils.displayErrorDialog(context, R.string.nfcTagLostErrorTitle, R.string.nfcTagLostError);
                        return false;
                    } catch (IOException ioe) {
                        DialogUtils.displayErrorDialog(context, R.string.nfcFormattingErrorTitle, R.string.nfcFormattingError);
                        return false;
                    } catch (FormatException fe) {
                        DialogUtils.displayErrorDialog(context, R.string.nfcFormattingErrorTitle, R.string.nfcFormattingError);
                        return false;
                    }
                } else {
                    DialogUtils.displayErrorDialog(context, R.string.nfcNoNdefErrorTitle, R.string.nfcNoNdefError);
                    return false;
                }
            }
        } catch(Exception e) {
            DialogUtils.displayErrorDialog(context, R.string.nfcUnknownErrorTitle, R.string.nfcUnknownError);
        }

        return false;
    }
}
