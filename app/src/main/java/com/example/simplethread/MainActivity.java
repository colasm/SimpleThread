package com.example.simplethread;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Thread.sleep;

/**
 * Cet exemple présente la définition et le lancement d'un thread simple
 * consistant à compter sans fin au rythme des secondes
 * On peut vérifier ici que
 *   - le fait de terminer un Thread supprime l'objet ;
 *   - le thread continue son comptage y compris lorsque l'activité est en pause;
 *   - back lance finish et destroy mais le thread est-il tué ? : on voit bien que non
 *   dans le log qui comptinue de compter.
 *   Lorsqu'on lance un second thread, les 2 comptent (mémory leak)
 */
public class MainActivity extends AppCompatActivity {

    private int cpt;
    private TextView mTv;
    private Button mBtn;
    private WorkingFragment mWorkingFragment;
    private Handler mHandler;
    private static final String TAG_WORKING_FRAGMENT = "MWF";

    private MThread mThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv = findViewById(R.id.compteur);
        mBtn = findViewById(R.id.button);


        // instantiation du fragment si besoin
        FragmentManager fm = getSupportFragmentManager();
        mWorkingFragment = (WorkingFragment)fm.findFragmentByTag(TAG_WORKING_FRAGMENT);
        // s'il n'existe pas
        if(mWorkingFragment == null){
            mWorkingFragment = new WorkingFragment();
            fm.beginTransaction().add(mWorkingFragment,TAG_WORKING_FRAGMENT).commit();

        }

        // récup référence sur Thread

    mThread=instantiateAndStartThread();


        // handler de messages
        mHandler = new Handler(Looper.getMainLooper() ){
            @Override
            public void handleMessage(@NonNull Message message) {
               cpt = message.what;
               mTv.setText(String.valueOf(cpt));
            }
        };


        if(mThread !=null){
            mThread.setHandler(mHandler);
        }

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mThread.getWaitingState()) {
                    resumeThread();
                    mBtn.setText("Stopper le Thread");
                }
                else {
                    pauseThread();
                    mBtn.setText("Lancer le Thread");
                }
            }
        });
    }


    private void resumeThread(){
        mThread.setWaitingState(false);
    }

    private void pauseThread(){
        //mThread.setRunningState(false);
        //mThread = null;
        //mWorkingFragment.setThread(null);
        mThread.setWaitingState(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // débrancher le handler
    }

    @Override
    protected void onResume() {
        super.onResume();
        // rebrancher le handler
    }

    private MThread instantiateAndStartThread(){
        if(mWorkingFragment.getThread()==null) {

            // définition et lancement d'un Thread

            MThread thread = new MThread();
            mWorkingFragment.setThread(thread);
            thread.start();
            return thread;
        }
        else{
            // ou récupération d'un thread déja existant
            return mWorkingFragment.getThread();
        }
    }

  

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mThread.setWaitingState(false);
        mThread.setRunningState(false);
    }
}