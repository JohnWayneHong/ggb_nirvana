package com.jgw.common_library.livedata;

import androidx.annotation.MainThread;
import androidx.lifecycle.MutableLiveData;

import com.jgw.common_library.utils.LogUtils;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * 连续发送不丢值的LiveData
 */
public class ValueKeeperLiveData<T> extends MutableLiveData<T> {

    private volatile LinkedList<T> queuedValues = new LinkedList<T>();

    @Override
    public void postValue(T value) {
        // We queue the value to ensure it is delivered
        // even if several ones are posted right after.
        // Then we call the base, which will eventually
        // call setValue().
        synchronized (this){
            queuedValues.offer(value);
            super.postValue(value);
        }

    }

    @Override
    @MainThread
    public void setValue(T value) {
//        super.setValue(value);
        // We first try to remove the value from the queue just
        // in case this line was reached from postValue(),
        // otherwise we will have it duplicated in the queue.
        synchronized (this) {
            queuedValues.remove(value);

            // We queue the new value and finally deliver the
            // entire queue of values to the observers.
            queuedValues.offer(value);
            while (!queuedValues.isEmpty()){
                try {
                    super.setValue(queuedValues.poll());
                }catch (NoSuchElementException exception){
                    LogUtils.showLog("error",exception.toString());
                }
            }
        }
    }

}
