package com.oleksiykovtun.android.pdfview.exampleapp;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oleksiykovtun.android.pdfview.PdfDocument;
import com.oleksiykovtun.android.pdfview.PdfDocumentException;
import com.oleksiykovtun.android.pdfview.PdfView;
import com.oleksiykovtun.android.pdfview.PdfViewPageChangeListener;

/**
 * An example of an activity which uses PdfView
 */
public class PdfActivity extends Activity implements PdfViewPageChangeListener {

    private PdfDocument pdfDocument;
    private TextView pageNumberTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.pdf_activity, null);

        if (pdfDocument == null) {
            try {
                pdfDocument = new PdfDocument(this, "/sdcard/Download/example.pdf");
            } catch (PdfDocumentException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        if (pdfDocument != null) {
            pdfDocument.setPageChangeListener(this);
            pdfDocument = updateScreenOrientation(pdfDocument);
            ((PdfView) view.findViewById(R.id.pdf_view)).setDocument(pdfDocument);
        }
        pageNumberTextView = (TextView)view.findViewById(R.id.page_number);
        setContentView(view);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (pdfDocument != null) {
            pdfDocument = updateScreenOrientation(pdfDocument);
            pdfDocument.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onPageChanged(int newActualPageNumber) {
        if (pageNumberTextView != null) {
            pageNumberTextView.setText(getText(R.string.page) + " " + newActualPageNumber);
        }
    }

    private PdfDocument updateScreenOrientation(PdfDocument pdfDocument) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            pdfDocument.setTwoPageView();
        } else {
            pdfDocument.setOnePageView();
        }
        return pdfDocument;
    }

}