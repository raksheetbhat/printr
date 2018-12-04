package com.example.raksheet.printer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.Fragment;

/**
 * Created by Raksheet on 06-12-2015.
 */
public class PrintActivity extends Fragment{
    public PrintActivity(){

    }
    public void doPrint(){
        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);

        String jobName = getActivity().getString(R.string.app_name) + " Document";

        printManager.print(jobName, new MyPrintDocumentAdapter(getActivity()) ,null);
    }

}
