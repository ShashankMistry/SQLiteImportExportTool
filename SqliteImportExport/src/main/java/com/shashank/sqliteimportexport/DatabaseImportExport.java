package com.shashank.sqliteimportexport;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseImportExport {
     ExportListener exportListener;
     ImportListener importListener;
     DirectExportListener directExportListener;

    public void SQLiteExport(Activity activity,String DATABASE_NAME, int REQUEST_CODE){
        Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        i.setType("application/octet-stream"); // application/octet-stream for handling database files
        i.putExtra(Intent.EXTRA_TITLE, DATABASE_NAME);
        activity.startActivityForResult(i, REQUEST_CODE);
    }

    public void writeDataOnExternal(Activity activity,Intent data,String DATABASE_NAME){
        try {
            assert data != null;
            FileOutputStream stream = (FileOutputStream) activity.getContentResolver().openOutputStream(data.getData());
            Files.copy(Paths.get(activity.getDatabasePath(DATABASE_NAME).getPath()), stream);
            exportListener.onExportSuccess("exported");
            stream.close(); ///very important
        } catch (Exception e) {
            e.printStackTrace();
            exportListener.onExportFailure(e);
        }
    }

    public void SQLiteImport(Activity activity, int REQUEST_CODE){
        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
        intent1.setType("application/octet-stream");
        activity.startActivityForResult(intent1, REQUEST_CODE);
    }
    public void writeDataOnInternal(Activity activity,Intent data,String DATABASE_NAME){
        try {
            File file = new File(activity.getDatabasePath(DATABASE_NAME).getPath());
            assert data != null;
            FileInputStream stream = (FileInputStream) activity.getContentResolver().openInputStream(data.getData());
            FileChannel channel = stream.getChannel();
            FileChannel channel1 = new FileOutputStream(file).getChannel();
            channel1.transferFrom(channel, 0, channel.size());
            importListener.onImportSuccess("imported");
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            importListener.onImportFailure(e);
        }
    }

    public void directExportToExternal(Context context, String DATABASE_NAME, String Application_name)  {
        ContentValues contentValues = new ContentValues();
        ContentResolver resolver = context.getContentResolver();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,DATABASE_NAME);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"application/octet-stream");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS+"/"+Application_name+"_Backup");
        Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);
        try {
            FileOutputStream outputStream = (FileOutputStream) resolver.openOutputStream(uri);
            Files.copy(context.getDatabasePath(DATABASE_NAME).toPath(),outputStream);///
            directExportListener.onDirectExportSuccess("Exported to Documents/"+Application_name+"_Backup");
        } catch (IOException e){
            directExportListener.onDirectExportFailure(e);
        }

    }


    public interface ExportListener {
        void onExportSuccess(String message);
        void onExportFailure(Exception exception);
    }

    public interface DirectExportListener {
        void onDirectExportSuccess(String message);
        void onDirectExportFailure(Exception exception);
    }
    public interface ImportListener {
        void onImportSuccess(String message);
        void onImportFailure(Exception exception);
    }

    public void setImportListener(ImportListener importListener){
        this.importListener = importListener;
    }

    public void setExportListener(ExportListener exportListener){
        this.exportListener = exportListener;
    }

    public void setDirectExportListener(DirectExportListener directExportListener){
        this.directExportListener = directExportListener;
    }

}
