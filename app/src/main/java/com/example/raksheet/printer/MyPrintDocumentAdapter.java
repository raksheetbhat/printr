package com.example.raksheet.printer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Raksheet on 06-12-2015.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class MyPrintDocumentAdapter extends PrintDocumentAdapter {
    private Context context;
    private PrintedPdfDocument mPdfDocument;
    private int totalPages;
    private StringBuffer writtenPagesArray;
    private AssetFileDescriptor destination;
    private int pageWidth;
    private int pageHeight;

    public MyPrintDocumentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1,
                         CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {

        mPdfDocument = new PrintedPdfDocument(context,printAttributes1);

        pageHeight = printAttributes1.getMediaSize().getHeightMils()/1000 * 72;
        pageWidth = printAttributes1.getMediaSize().getWidthMils()/1000 * 72;

        if(cancellationSignal.isCanceled()){
            layoutResultCallback.onLayoutCancelled();
            return;
        }

        //int pages = computePageCount(printAttributes1);
        int pages = 4;
        if(pages>0){
            PrintDocumentInfo info = new PrintDocumentInfo.Builder("print_output.pdf").setContentType(
                    PrintDocumentInfo.CONTENT_TYPE_DOCUMENT
            ).setPageCount(pages).build();

            layoutResultCallback.onLayoutFinished(info,true);
        }else{
            layoutResultCallback.onLayoutFailed("Page count calculation failed");
        }
    }

    private int computePageCount(PrintAttributes printAttributes) {
        int itemsPerPage = 4;

        PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
        if(!pageSize.isPortrait()){
            itemsPerPage = 6;
        }

        int printItemCount = getPrintItemCount();

        return (int) Math.ceil(printItemCount/itemsPerPage);
    }

    private int getPrintItemCount() {
        return 0;
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor,
                        CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {

        for(int i=0;i<totalPages;i++){
            if(containsPage(pageRanges,i)){
                //writtenPagesArray.append(writtenPagesArray.size(),i);
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,i).create();
                PdfDocument.Page page = mPdfDocument.startPage(newPage);

                if(cancellationSignal.isCanceled()){
                    writeResultCallback.onWriteCancelled();
                    mPdfDocument.close();
                    mPdfDocument = null;
                    return;
                }

                drawPage(page);

                mPdfDocument.finishPage(page);
            }

            try{
                mPdfDocument.writeTo(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
            }catch(IOException e){
                writeResultCallback.onWriteFailed(e.toString());
                return;
            }finally{
                mPdfDocument.close();
                mPdfDocument = null;
            }

            //PageRange[] writtenPages = computeWrittenPages();
            writeResultCallback.onWriteFinished(pageRanges);

        }

    }

    private PageRange[] computeWrittenPages() {
        return new PageRange[0];
    }

    private boolean containsPage(PageRange[] pageRanges, int page) {
        for(int i=0;i<pageRanges.length;i++){
            if((page >= pageRanges[i].getStart()) && (page <= pageRanges[i].getEnd())){
                return true;
            }
        }
        return false;
    }

    private void drawPage(PdfDocument.Page page) {
        Canvas canvas = page.getCanvas();

        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(36);
        canvas.drawText("Test Title", leftMargin, titleBaseLine, paint);

        paint.setColor(Color.BLUE);
        canvas.drawRect(100,100,172,172,paint);
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}
