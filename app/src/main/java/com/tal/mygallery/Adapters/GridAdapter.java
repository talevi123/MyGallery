package com.tal.mygallery.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tal.mygallery.DB.SQLiteHelper;
import com.tal.mygallery.R;

/**
 * Created by tal on 28/12/16.
 */
public class GridAdapter extends CursorAdapter {

    private int type;

    public GridAdapter(Context context, Cursor c, int type) {
        super(context, c, true);
        this.type = type;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.grid_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView icon = (ImageView) view.findViewById(R.id.icon);

        if (type == R.string.phone) {
            Picasso.with(context).load("file://" + cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_PICTURE))).
                    fit().into(icon);
        } else {
            Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_PICTURE))).
                    fit().into(icon);
        }
    }

}
