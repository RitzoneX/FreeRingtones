package com.forritzstar.freeringtones.ui;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.forritzstar.freeringtones.MyActivity;
import com.forritzstar.freeringtones.R;

public class AboutActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tvAbout = (TextView) findViewById(R.id.tv_about);
        tvAbout.setText(tvAbout.getText() + " " + getVersionName());
    }

    private String getVersionName()//获取版本名
    {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return getString(R.string.version_unknown);
        }
    }

}
