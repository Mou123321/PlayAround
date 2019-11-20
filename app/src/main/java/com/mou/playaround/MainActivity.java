package com.mou.playaround;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageErrorListener, OnPageChangeListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ConstraintLayout constraintLayout;

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        ImageView menuButton = findViewById(R.id.menu_icon);

        constraintLayout = findViewById(R.id.content);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.files:
                        Toast.makeText(MainActivity.this, "files",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.mail:
                        Toast.makeText(MainActivity.this, "mails",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.people:
                        Toast.makeText(MainActivity.this, "people",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.sort:
                        Toast.makeText(MainActivity.this, "sort",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setting:
                        Toast.makeText(MainActivity.this, "setting",Toast.LENGTH_SHORT).show();
                        break;

                }
                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });

        pdfView =  findViewById(R.id.pdfView);

        new RetrivePDFStream().execute("https://www.mondotheque.be/wiki/images/c/cc/Dave_Eggers_The_Circle.pdf");

//        pdfView.fromUri(uri)
//                .onPageChange(this)
//                .enableAnnotationRendering(true)
//                .onLoad(this)
//                .scrollHandle(new DefaultScrollHandle(this))
//                .spacing(10) // in dp
//                .onPageError(this)
//                .load();
    }


    class RetrivePDFStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL uri = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) uri.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .onLoad(MainActivity.this) // called after document is loaded and starts to be rendered
                    .onPageChange(MainActivity.this)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .swipeHorizontal(true)
                    .spacing(10)
                    .load();
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        System.out.println("load completele");
    }

    @Override
    public void onPageError(int page, Throwable t) {
        System.out.println( "Cannot load page " + page);
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        System.out.println( "page is " + page);
    }
}
