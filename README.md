# SQLiteImportExportTool
Easy to use library to import and export SQLite database of application

## Why this library?

Google recently changed their privacy policy regarding write external storage permission in Android 11. So, now we have to use other methods for import export of our databases.
This library does not use write external storage permission, it writes backup on external storage with intent ACTION_CREATE_DOCUMENT.

## How to use?

### Go to Gradle(Module: Project) and add
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

### Go to Gradle(Module: app) and add
```
dependencies {
	        implementation 'com.github.ShashankMistry:SQLiteImportExportTool:1.0.3'
	}
```


### To export database
```
DatabaseImportExport databaseImportExport = new DatabaseImportExport();
databaseImportExport.SQLiteExport(MainActivity.this,DATABASE_NAME,EXPORT_CODE);
```
### To import database
```
databaseImportExport.SQLiteImport(MainActivity.this,IMPORT_CODE));
```

### Write data to exported or imported file in onActivityResult
```
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
                break;
        }
    }
```

### Export file directly to external storage
```
databaseImportExport.directExportToExternal(MainActivity.this, DATABASE_NAME,APP_NAME);
```

### import, export success or failure event listeners
```
 	databaseImportExport.setExportListener(new DatabaseImportExport.ExportListener() {
            @Override
            public void onExportSuccess(String message) {
                Toast.makeText(MainActivity.this, "Exported", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "imported", Toast.LENGTH_SHORT).show();
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
```
