package com.ggb.nirvanaclub.manager;

public class TokenLoseEfficacy {

    private static TokenLoseEfficacy mTokenLoseEfficacy;

    private TokenLoseEfficacy(){}

    public static TokenLoseEfficacy getInstance(){
        if(mTokenLoseEfficacy == null){
            mTokenLoseEfficacy = new TokenLoseEfficacy();
        }
        return mTokenLoseEfficacy;
    }


    public interface OnTokenLoseEfficacyListener{
        void onTokenLoseEfficacy();
    }

    private OnTokenLoseEfficacyListener mOnTokenLoseEfficacyListener;

    public void setOnTokenLoseEfficacyListener(OnTokenLoseEfficacyListener mOnTokenLoseEfficacyListener) {
        this.mOnTokenLoseEfficacyListener = mOnTokenLoseEfficacyListener;
    }

    public void tokenLoseEfficacyLoginOut(){
        if(mOnTokenLoseEfficacyListener!=null){
            mOnTokenLoseEfficacyListener.onTokenLoseEfficacy();
        }
    }
}
