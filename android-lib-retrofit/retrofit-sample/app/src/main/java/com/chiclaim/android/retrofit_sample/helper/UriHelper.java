package com.chiclaim.android.retrofit_sample.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class UriHelper {

    public static File getFileFromUri(Context context, Uri uri) {
        if (uri == null || uri.getScheme() == null) {
            return null;
        }
        switch (uri.getScheme()) {
            case "content":
                return getFileFromContentUri(context, uri);
            case "file":
                if (uri.getPath() != null) return new File(uri.getPath());
            default:
                return null;
        }
    }

    private static File getFileFromContentUri(Context context, Uri contentUri) {
        if (contentUri == null) {
            return null;
        }

        File file = null;
        String filePath = null;
        String fileName = null;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME};
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(contentUri, filePathColumn, null,
                null, null);

        if (cursor == null) return null;

        try {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            fileName = cursor.getString(cursor.getColumnIndex(filePathColumn[1]));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        if (!TextUtils.isEmpty(filePath)) {
            file = new File(filePath);
        }

        if (file == null || !file.exists() || file.length() <= 0) {
            String path = getPathFromInputStreamUri(context, contentUri, fileName);
            if (path != null) file = new File(path);
        }
        return file;
    }

    private static String getPathFromInputStreamUri(Context context, Uri uri, String fileName) {
        if (uri.getAuthority() != null) {
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                File file = createTempFileFrom(context, inputStream, fileName);
                if (file != null) return file.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static File createTempFileFrom(Context context, InputStream inputStream, String fileName) {

        if (inputStream == null) return null;

        File targetFile = new File(context.getCacheDir(), fileName);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        try (OutputStream outputStream = new FileOutputStream(targetFile)) {
            byte[] buffer = new byte[8 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return targetFile;
    }

}
