package com.oleksiykovtun.android.pdfview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * PDF View based on MuPDF (com.artifex.mupdfdemo.MuPDFCore)
 */
public class PdfView extends FrameLayout {

    public PdfView(Context context) {
        super(context);
    }

    public PdfView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PdfView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDocument(PdfDocument pdfDocument) {
        if (pdfDocument != null) {
            addView(pdfDocument);
        }
    }

}
