package com.uama.vieoplayer.example;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.uama.videoplayer.NiceVideoPlayer;
import com.uama.videoplayer.NiceVideoPlayerManager;
import com.uama.videoplayer.TBVideoPlayerController;
import com.uama.vieoplayer.R;

/**
 * Created by XiaoJianjun on 2017/7/7.
 * 如果你需要在播放的时候按下Home键能暂停，回调此Fragment又继续的话，需要继承自CompatHomeKeyFragment
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class DemoScrollFragenment extends Fragment {
    
    private NiceVideoPlayer mNiceVideoPlayer;
    private ScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.uama_activity, container, false);
        mScrollView = view.findViewById(R.id.scroll_view);
        mNiceVideoPlayer = view.findViewById(R.id.nice_video_player);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        trigger();
    }
    
    private void init() {
        
        mNiceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
        String videoUrl = "http://video.zjssst.com/fd6bf554d1ec4462855f17fb78870507/0b03c7c3e5454cc9b34386344277eec7-5287d2089db37e62345123a1be272f8b.mp4";
        mNiceVideoPlayer.setUp(videoUrl, null);
        TBVideoPlayerController controller = new TBVideoPlayerController(getActivity());
        controller.setLenght(98000);
        Glide.with(this)
                .load("http://pic.qiantucdn.com/58pic/19/73/22/570f6abca6f01_1024.jpg")
                .placeholder(R.drawable.img_default)
                .crossFade()
                .into(controller.imageView());
        mNiceVideoPlayer.setController(controller);
    }
    
    private void trigger() {
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                int[] location = new int[2];
                mNiceVideoPlayer.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
                
                if (location[1] < 0) {
                    if (!mNiceVideoPlayer.isIdle()) {
                        if (!mNiceVideoPlayer.isTinyWindow()) {
                            Log.i("msg", "获取在当前窗口内的绝对坐标" + location[0] + "####----####" + location[1]);
                            mNiceVideoPlayer.enterTinyWindow();
                        }
                    }
                } else {
                    if (mNiceVideoPlayer.isTinyWindow()) {
                        Log.i("msg", "获取在当前窗口内的绝对坐标" + location[0] + "####----####" + location[1]);
                        mNiceVideoPlayer.exitTinyWindow();
                    }
                }
            }
        });
    }
    
    @Override
    public void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
    
    public boolean onBackPressed() {
        return NiceVideoPlayerManager.instance().onBackPressd();
    }
}
