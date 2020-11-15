package com.example.simplethread;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WorkingFragment extends Fragment {
    private MThread mthread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setThread(MThread thread){
        mthread = thread;
    }

    public MThread getThread(){
        return mthread;
    }


}
