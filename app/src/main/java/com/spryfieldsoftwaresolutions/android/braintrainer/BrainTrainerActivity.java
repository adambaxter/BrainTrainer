package com.spryfieldsoftwaresolutions.android.braintrainer;

import android.support.v4.app.Fragment;

public class BrainTrainerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GameFragment.newInstance();
    }
}
