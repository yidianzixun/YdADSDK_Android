package com.yidian.geek.addemo.page;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.yidian.adsdkdemo.R;
import com.yidian.geek.addemo.helper.BackSupport;
import com.yidian.newssdk.exportui.NewsPortalFragment;

public class NaviHomeActivity extends FragmentActivity {

    private final BackSupport mBackSupport = new BackSupport();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_home);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.portal_container, new NewsPortalFragment())
                .commitNowAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (!mBackSupport.canBeBack()) {
            Toast.makeText(this, "再次按返回退出", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

}
