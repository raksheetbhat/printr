package com.example.raksheet.printer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Raksheet on 08-12-2015.
 */
public class SelectFileFragment extends Activity {

    private View mFileView;
    private TextView filePath;
    private File selectedFile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_file);

        filePath = (TextView) findViewById(R.id.file_path);
        Intent intent = new Intent(this,FilePicker.class);
        startActivityForResult(intent,1);

    }

}
