package herringbone.com.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by adlee on 11/28/14.
 */
public class SelfieDetailActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selfie_detail_view);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String filePath = extras.getString("Extra_Message");
            ImageView mSelfieLarge = (ImageView) findViewById(R.id.fullsize);
            Bitmap bigPic = SelfieAdapter.setPic(filePath, mSelfieLarge);
            mSelfieLarge.setImageBitmap(bigPic);
        }
    }

}
