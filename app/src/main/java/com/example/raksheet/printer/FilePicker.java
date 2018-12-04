package com.example.raksheet.printer;

import android.app.ListActivity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Raksheet on 08-12-2015.
 */
public class FilePicker extends ListActivity {

    public final static String EXTRA_FILE_PATH = "file_path";
    public final static String EXTRA_SHOW_HIDDEN_FILES = "show_hidden_files";
    public final static String EXTRA_ACCEPTED_FILE_EXTENSIONS = "accepted_file_extensions";
    public final static String DEFAULT_INITIAL_DIRECTORY = "/";

    protected File Directory;
    protected ArrayList<File> Files;
    protected FilePickerListAdapter Adapter;
    protected boolean ShowHiddenFiles = false;
    protected String[] acceptedFileExtensions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View emptyView = inflater.inflate(R.layout.empty_view,null);
        ((ViewGroup) getListView().getParent()).addView(emptyView);

        Directory = new File(DEFAULT_INITIAL_DIRECTORY);

        Files = new ArrayList<File>();

        Adapter = new FilePickerListAdapter(this,Files);
        setListAdapter(Adapter);

        acceptedFileExtensions = new String[] {};

        if(getIntent().hasExtra(EXTRA_FILE_PATH)) Directory = new File(getIntent().getStringExtra(EXTRA_FILE_PATH));

        if(getIntent().hasExtra(EXTRA_SHOW_HIDDEN_FILES)) ShowHiddenFiles = getIntent().getBooleanExtra(EXTRA_SHOW_HIDDEN_FILES,false);

        if(getIntent().hasExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS)){
            ArrayList<String> collection = getIntent().getStringArrayListExtra(EXTRA_ACCEPTED_FILE_EXTENSIONS);
            acceptedFileExtensions = (String[]) collection.toArray(new String[collection.size()]);
        }
    }

    @Override
    protected void onResume() {
        refreshFilesList();
        super.onResume();
    }

    private void refreshFilesList() {
        Files.clear();
        //ExtensionFilenameFilter filter = new ExtensionFilenameFilter(acceptedFileExtensions);

        /*File[] files = Directory.listFiles(filter);

        if(files!=null && files.length>0){
            for(File f : files){
                if(f.isHidden() && !ShowHiddenFiles){
                    continue;
                }

                Files.add(f);
            }
            Collections.sort(Files, new FileComparator());
        }*/

        Adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(Directory.getParentFile() != null){
            Directory = Directory.getParentFile();
            refreshFilesList();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        File newFile = (File) l.getItemAtPosition(position);

        if(newFile.isFile()){
            Intent extra = new Intent();
            extra.putExtra(EXTRA_FILE_PATH,newFile.getAbsolutePath());
            setResult(RESULT_OK,extra);
            finish();
        }else{
            Directory = newFile;
            refreshFilesList();
        }

        super.onListItemClick(l, v, position, id);
    }
}

class FilePickerListAdapter extends ArrayAdapter<File>{

    private List<File> mObjects;

    public FilePickerListAdapter(Context context, List<File> objects) {
        super(context, R.layout.select_list,android.R.id.text1,objects);
        mObjects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.select_list,parent,false);
        }else{
            row = convertView;
        }

        File object = mObjects.get(position);

        ImageView imageView = (ImageView) row.findViewById(R.id.file_picker_image);
        TextView textView = (TextView) row.findViewById(R.id.file_picker_text);
        textView.setSingleLine(true);
        textView.setText(object.getName());

        if(object.isFile()){
            //imageView.setImageResource(R.dra);
        }
        return row;
    }
}
