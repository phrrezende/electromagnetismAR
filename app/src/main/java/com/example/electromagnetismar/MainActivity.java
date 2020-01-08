package com.example.electromagnetismar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import org.artoolkitx.arx.arxj.ARActivity;
import org.artoolkitx.arx.arxj.assets.AssetHelper;
import org.artoolkitx.arx.arxj.rendering.ARRenderer;

public class MainActivity extends ARActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInstance();
        setContentView(R.layout.activity_main);
    }


    private void initializeInstance() {
        // Unpack assets to cache directory so native library can read them.
        // N.B.: If contents of assets folder changes, be sure to increment the
        // versionCode integer in the modules build.gradle file.
        AssetHelper assetHelper = new AssetHelper(getAssets());
        assetHelper.cacheAssetFolder(this, "Data");
        assetHelper.cacheAssetFolder(this, "cparam_cache");
    }

    @Override
    protected ARRenderer supplyRenderer() {
        return new ElectromagnetismRenderer(getApplicationContext());
    }

    @Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout) this.findViewById(R.id.mainFrameLayout);
    }
}
