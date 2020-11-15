package com.example.simplethread;

import android.os.Handler;
import android.util.Log;

public class MThread extends Thread{
private boolean mRunningState = true;
private boolean mWaiting = true;
private int cpt = 0;
private Handler mHandler=null;
    @Override
    public synchronized void run() {

            while (mRunningState) {
                try {
                    if(mWaiting) wait();
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                cpt++;
                if(mHandler !=null) {
                    mHandler.obtainMessage(cpt).sendToTarget();
                }
                Log.i("Thread", "Comptage = " + String.valueOf(cpt));
            }
        }

        public boolean getWaitingState(){
        return mWaiting;
        }
        public  void setWaitingState(boolean waiting){
            mWaiting = waiting;
            synchronized(this){
                if(!waiting) notify();
        }
        }

        public boolean getRunningState(){
        return mRunningState;
        }

    public void setRunningState(boolean runningState){
        mRunningState = runningState;
    }


    public void setHandler(Handler handler){
        mHandler = handler;
    }
    }

