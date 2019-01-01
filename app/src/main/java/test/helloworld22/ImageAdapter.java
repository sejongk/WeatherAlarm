package test.helloworld22;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.WINDOW_SERVICE;

public class ImageAdapter extends BaseAdapter implements ListAdapter {
    private Context mContext;

    public Bitmap bm;
    //ArrayList<String> f = new ArrayList<String>();// list of file paths
    public ArrayList<Bitmap> mThumbIds = new ArrayList<>();
    public Bitmap[] mThumb;
    public BitmapFactory.Options options = new BitmapFactory.Options();
    private File[] listFile;
    public int width;

    public ImageAdapter(Context c) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) c.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        mContext = c;

        File directory= new File("/storage/emulated/0/DCIM/Camera");
        Log.d("fileDirectory : ",Environment.getDataDirectory()+"/DCIM/Camera");
        listFile = directory.listFiles();
        options.inSampleSize = 8;

        for (int i = 0; i < listFile.length; i++)
        {

            if(listFile[i].getName().endsWith(".jpg")) {//Log.d("Files", "FileName:" + listFile[i].getAbsolutePath());
                bm = BitmapFactory.decodeFile(listFile[i].getAbsolutePath(),options);
                mThumbIds.add(bm);
            }

        }
        mThumb = new Bitmap[mThumbIds.size()];
        mThumb = mThumbIds.toArray(mThumb);

    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) { // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(width/3, width/3));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        Log.e("Frag", "position :"+position);
        Log.e("Frag", "mthumb :"+mThumb.length);
        if( mThumbIds.size() > position ) {
            imageView.setImageBitmap(mThumb[position]);
        }
        Log.e("Frag", "Test is running");
        return imageView;
    }

}