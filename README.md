# PDF-View-Android

A PDF viewing library for Android. Based on https://github.com/libreliodev/android

Supports 1-page and 2-page PDF viewing in a custom view.

## Usage

Put the AAR file `PDF-View-Android-1.1.0.aar` of the library into your module's folder `libs`.

Add

    repositories {
        flatDir {
            dirs 'libs'
        }
    }

and

    dependencies {
        compile(name:'PDF-View-Android-1.1.0', ext:'aar')
        compile 'commons-io:commons-io:2.4'
    }

to your module's `build.gradle` file.

Add

    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

to the Android manifest of your module.

Add

    <com.oleksiykovtun.android.pdfview.PdfView
        android:id="@+id/pdf_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

to your activity layout.

Activity code:

    public class PdfActivity extends Activity {

        private PdfDocument pdfDocument;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.pdf_activity, null);

            pdfDocument = new PdfDocument(this, "/sdcard/your-pdf-file.pdf");
            pdfDocument.setOnePageView();
            ((PdfView) view.findViewById(R.id.pdf_view)).setDocument(pdfDocument);

            setContentView(view);
        }

    }

See the example app fore a more detailed example.

## License

This project is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This project is Copyright 2012-2014 Librelio, except included libraries:
- Copyright 2006-2012 Artifex Software, Inc for the MuPDF library (http://www.mupdf.com)
- Copyright Free Beachler (http://www.freebeachler.com) for the Android P-List Parser library
- Copyright (c) 2011 Mats Hofman for the Android RSS reader library
- Copyright 2011-2013 sbstrm. Copyright 2014 drewjw81 for the Appirater Android library
