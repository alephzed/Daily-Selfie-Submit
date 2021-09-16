package herringbone.com.dailyselfie;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adlee on 11/25/14.
 */
public class SelfieAdapter extends BaseAdapter{

    private ArrayList<SelfieRecord> list = new ArrayList<SelfieRecord>();
    private static LayoutInflater inflater = null;
    private Context mContext;

    public SelfieAdapter( Context context ) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        ViewHolder holder;

        SelfieRecord curr = list.get(position);

        if (null == convertView) {
            holder = new ViewHolder();
            newView = inflater.inflate(R.layout.selfie_thumbnail_view, null);
            holder.picture = (ImageView) newView.findViewById(R.id.thumbnail);
            holder.photo_name = (TextView) newView.findViewById(R.id.photo_name);
            newView.setTag(holder);

        } else {
            holder = (ViewHolder) newView.getTag();
        }
        Bitmap bitmap = setPic(curr.getmRecordLocation(), newView.findViewById(R.id.thumbnail));
        String[] path = curr.getmRecordLocation().split("/");
        String name = path[path.length-1].split("\\.")[0].substring(5);

        holder.photo_name.setText(name);
        holder.picture.setImageBitmap(bitmap);
        return newView;
    }

    public void add(SelfieRecord listItem) {
        if (!list.contains(listItem)) {
            list.add(listItem);
            notifyDataSetChanged();
        }
    }


    static class ViewHolder {

        ImageView picture;
        TextView photo_name;
        //TextView place;

    }

    public static Bitmap setPic( String fileLocation, View imageView) {
        // Get the dimensions of the View
        int targetW = imageView.getLayoutParams().width;
        int targetH = imageView.getLayoutParams().height;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileLocation, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fileLocation, bmOptions);
        return bitmap;
    }
}
