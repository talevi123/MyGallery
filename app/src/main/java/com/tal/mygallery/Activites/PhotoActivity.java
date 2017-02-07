package com.tal.mygallery.Activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tal.mygallery.R;

/**
 * Created by tal on 29/12/16.
 */
public class PhotoActivity extends AppCompatActivity {

    ImageView image;
    TextView created_time;
    Button btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        getPhoto();
    }

    public void getPhoto() {
        image = (ImageView) findViewById(R.id.image);
        created_time = (TextView) findViewById(R.id.created);
        btn = (Button) findViewById(R.id.back_btn);

        Bundle bundle = getIntent().getExtras();

        if ((bundle.getString("Type")).equals(getString(R.string.facebook))) {
            Picasso.with(this).load(bundle.getString("image")).fit().into(image);
        } else {
            Picasso.with(this).load("file://" + bundle.getString("image")).fit().into(image);
        }

        created_time.setText(bundle.getString("created_time"));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
