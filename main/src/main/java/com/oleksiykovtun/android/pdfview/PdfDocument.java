package com.oleksiykovtun.android.pdfview;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.artifex.mupdfdemo.LinkInfoExternal;
import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.view.DocumentReaderView;

/**
 * PDF document view based on MuPDF (com.artifex.mupdfdemo.MuPDFCore)
 */
public class PdfDocument extends DocumentReaderView {

    private static final String TAG = "PdfDocument";
    private MuPDFCore core;
    private Context context;
    private PdfViewPageChangeListener delegate = null;
    private int documentPageNumber = 1;

    public PdfDocument(Context context, String filePath) throws PdfDocumentException {
        super(context, new SparseArray<LinkInfoExternal[]>());
        this.context = context;
        try {
            core = openFile(filePath);
        } catch (Throwable e) {
            Log.e(TAG, "Cannot open PDF file", e);
            throw new PdfDocumentException("Cannot open PDF file", e);
        }
    }

    public void setOnePageView() {
        setDisplayPages(1);
    }

    public void setTwoPageView() {
        setDisplayPages(2);
    }

    public int getDisplayPages() {
        int displayPages = 0;
        if (core != null) {
            displayPages = core.getDisplayPages();
        } else {
            Log.e(TAG, "Cannot get display pages count. No file opened");
        }
        return displayPages;
    }

    public void setDocumentPageNumber(int documentPageNumber) {
        if (core != null) {
            int pageNumber = documentPageNumber - 1;
            setDisplayedViewIndex((getDisplayPages() == 2)
                    ? (pageNumber / 2 + pageNumber % 2)
                    : (pageNumber));
            this.documentPageNumber = documentPageNumber;
        } else {
            Log.e(TAG, "Cannot set document page number. No file opened");
        }
    }

    public int getDocumentPageNumber() {
        return documentPageNumber;
    }

    public void setPageChangeListener(PdfViewPageChangeListener delegate) {
        this.delegate = delegate;
        delegate.onPageChanged(documentPageNumber);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (core != null) {
            setDocumentPageNumber(documentPageNumber);
            setAdapter(new MuPDFPageAdapter(context, core));
        }
    }

    @Override
    protected void onMoveToChild(View view, int showingPageNumber) {
        if (core != null) {
            super.onMoveToChild(view, showingPageNumber);
            documentPageNumber = (getDisplayPages() == 2)
                    ? (showingPageNumber > 0)
                        ? (showingPageNumber * 2)
                        : (1)
                    : (showingPageNumber + 1);
            if (delegate != null) {
                delegate.onPageChanged(documentPageNumber);
            }
        }
    }

    @Override
    protected void onContextMenuClick() {

    }

    @Override
    protected void onBuy(String path) {

    }

    private MuPDFCore openFile(String path) throws Throwable {
        core = new MuPDFCore(path);
        setAdapter(new MuPDFPageAdapter(context, core));
        return core;
    }

    private void setDisplayPages(int displayPages) {
        if (core != null) {
            core.setDisplayPages(displayPages);
        } else {
            Log.e(TAG, "Cannot set display pages count. No file opened");
        }
    }

}
