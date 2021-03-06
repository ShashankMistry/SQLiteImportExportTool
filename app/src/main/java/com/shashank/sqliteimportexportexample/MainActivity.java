package com.shashank.sqliteimportexportexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shashank.sqliteimportexport.DatabaseImportExport;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "Chat.db";
    private static final int EXPORT_CODE = 11;
    private static final int IMPORT_CODE = 12;
    private static final String APP_NAME = "SQLiteExample";
    TextView text;
    DBHelper dbHelper;
    DatabaseImportExport databaseImportExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this,null,1);
        text = findViewById(R.id.text);
        text.setText("");
        dbHelper.fetchData(text);
        Button export = findViewById(R.id.export);
        Button importDb=  findViewById(R.id.importDb);
        Button Direct = findViewById(R.id.direct);
        databaseImportExport = new DatabaseImportExport();

        export.setOnClickListener(v -> databaseImportExport.SQLiteExport(MainActivity.this,DATABASE_NAME,EXPORT_CODE));

        importDb.setOnClickListener(v -> databaseImportExport.SQLiteImport(MainActivity.this,IMPORT_CODE));

        Direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseImportExport.directExportToExternal(MainActivity.this, DATABASE_NAME,APP_NAME);
            }
        });

        databaseImportExport.setExportListener(new DatabaseImportExport.ExportListener() {
            @Override
            public void onExportSuccess(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onExportFailure(Exception exception) {
                exception.printStackTrace();
                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        databaseImportExport.setImportListener(new DatabaseImportExport.ImportListener() {
            @Override
            public void onImportSuccess(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onImportFailure(Exception exception) {
                exception.printStackTrace();
                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        databaseImportExport.setDirectExportListener(new DatabaseImportExport.DirectExportListener() {
            @Override
            public void onDirectExportSuccess(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDirectExportFailure(Exception exception) {
                exception.printStackTrace();
                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case EXPORT_CODE:
                databaseImportExport.writeDataOnExternal(MainActivity.this,data,DATABASE_NAME);
                break;
            case IMPORT_CODE:
                assert data != null;
                databaseImportExport.writeDataOnInternal(MainActivity.this,data,DATABASE_NAME);
                text.setText("");
                dbHelper.fetchData(text);
                break;
        }
    }

}