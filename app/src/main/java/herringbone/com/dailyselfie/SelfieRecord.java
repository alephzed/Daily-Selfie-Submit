package herringbone.com.dailyselfie;

/**
 * Created by adlee on 11/25/14.
 */
public class SelfieRecord {
    private String mRecordLocation;

    public String getmRecordLocation() {
        return mRecordLocation;
    }

    public void setmRecordLocation(String mRecordLocation) {
        this.mRecordLocation = mRecordLocation;
    }

    public boolean equals(Object other) {
        SelfieRecord otherSelfie = (SelfieRecord)other;
        if (otherSelfie.getmRecordLocation().equals(this.getmRecordLocation())) {
            return true;
        }
        return false;
    }
}
