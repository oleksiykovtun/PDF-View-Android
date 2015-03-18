package com.oleksiykovtun.android.pdfview;

/**
 * PDF document exception
 */
public class PdfDocumentException extends Exception {

    public PdfDocumentException(String message) {
        super(message);
    }

    public PdfDocumentException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
