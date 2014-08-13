package com.delin.speedlogger.Activities;

import com.delin.speedlogger.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StoredRecordActivity extends Activity {
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storedrecord);
        Button btn = (Button)findViewById(R.id.btnSubmitToSrv);
        btn.setOnClickListener(mOnClickListener);

        //mMaxSpeed = (TextView)findViewById(R.id.textMaxSpeed);
        //mDistance = (TextView)findViewById(R.id.textDistance);
        //mTime = (TextView)findViewById(R.id.totalTimevalue);

        //HandleResults();
    }

    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
            case R.id.btnSubmitToSrv:
                intent = new Intent(v.getContext(), LocalTimesActivity.class);
                break;
            }
            if (intent != null) startActivity(intent);
        }
    };
}
