package com.yanyi.fire.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


public class RespLiveData<T> extends MutableLiveData<T> {
    public OnError errorHandler = new OnError() {
        @Override
        public void onError(Throwable e) {

        }
    };

    public void error(@Nullable OnError onError) {
        this.errorHandler = onError;
    }

    public void update() {
        setValue(getValue());
    }

    public void observeOnly(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        removeObservers(owner);
        observe(owner, observer);
    }

    public interface OnError {
        void onError(Throwable e);
    }
}
