package com.yidian.geek.addemo.helper;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Surface;

import com.yidian.adsdk.protocol.ydvd.IMediaInterface;
import com.yidian.adsdk.protocol.ydvd.YdMediaInterface;
import com.yidian.adsdk.protocol.ydvd.YdMediaManager;
import com.yidian.adsdk.protocol.ydvd.YdVideoPlayerManager;
import com.yidian.adsdk.utils.ThreadUtils;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;


public class YdMediaIjkPlayer extends YdMediaInterface implements IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnSeekCompleteListener,
        IMediaPlayer.OnTimedTextListener {
    IjkMediaPlayer ijkMediaPlayer;
    IMediaInterface iMediaInterface = YdVideoPlayerManager.getCurrentJzvd();

    @Override
    public void start() {
        ijkMediaPlayer.start();
    }

    @Override
    public void prepare() {
        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 1024 * 1024);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);

        ijkMediaPlayer.setOnPreparedListener(YdMediaIjkPlayer.this);
        ijkMediaPlayer.setOnVideoSizeChangedListener(YdMediaIjkPlayer.this);
        ijkMediaPlayer.setOnCompletionListener(YdMediaIjkPlayer.this);
        ijkMediaPlayer.setOnErrorListener(YdMediaIjkPlayer.this);
        ijkMediaPlayer.setOnInfoListener(YdMediaIjkPlayer.this);
        ijkMediaPlayer.setOnBufferingUpdateListener(YdMediaIjkPlayer.this);
        ijkMediaPlayer.setOnSeekCompleteListener(YdMediaIjkPlayer.this);
        ijkMediaPlayer.setOnTimedTextListener(YdMediaIjkPlayer.this);

        try {
            ijkMediaPlayer.setDataSource(currentDataSource.toString());
            ijkMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            ijkMediaPlayer.setScreenOnWhilePlaying(true);
            ijkMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        ijkMediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return ijkMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        ijkMediaPlayer.seekTo(time);
    }

    @Override
    public void release() {
        if (ijkMediaPlayer != null) {
            ijkMediaPlayer.release();
        }
    }

    @Override
    public long getCurrentPosition() {
        return ijkMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return ijkMediaPlayer.getDuration();
    }

    @Override
    public void setSurface(Surface surface) {
        ijkMediaPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        ijkMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public void onRelease() {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                if (YdVideoPlayerManager.getCurrentJzvd() != null) {
                    YdVideoPlayerManager.getCurrentJzvd().onRelease();
                }
            }
        });
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        ijkMediaPlayer.start();
        if (currentDataSource.toString().toLowerCase().contains("mp3")) {
            ThreadUtils.post2UI(new Runnable() {
                @Override
                public void run() {
                    iMediaInterface = YdVideoPlayerManager.getCurrentJzvd();
                    if (iMediaInterface != null) {
                        iMediaInterface.onPrepared();
                    }
                }
            });
        }
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        YdMediaManager.instance().currentVideoWidth = iMediaPlayer.getVideoWidth();
        YdMediaManager.instance().currentVideoHeight = iMediaPlayer.getVideoHeight();
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                iMediaInterface = YdVideoPlayerManager.getCurrentJzvd();
                if (iMediaInterface != null) {
                    iMediaInterface.onVideoSizeChanged();
                }
            }
        });
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                iMediaInterface = YdVideoPlayerManager.getCurrentJzvd();
                if (iMediaInterface != null) {
                    iMediaInterface.onAutoCompletion();
                }
            }
        });
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                iMediaInterface = YdVideoPlayerManager.getCurrentJzvd();
                if (iMediaInterface != null) {
                    iMediaInterface.onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, final int what, final int extra) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                iMediaInterface = YdVideoPlayerManager.getCurrentJzvd();
                if (iMediaInterface != null) {
                    if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        iMediaInterface.onPrepared();
                    } else {
                        iMediaInterface.onInfo(what, extra);
                    }
                }
            }
        });
        return false;
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, final int percent) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                iMediaInterface = YdVideoPlayerManager.getCurrentJzvd();
                if (iMediaInterface != null) {
                    iMediaInterface.setBufferProgress(percent);
                }
            }
        });
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        ThreadUtils.post2UI(new Runnable() {
            @Override
            public void run() {
                iMediaInterface = YdVideoPlayerManager.getCurrentJzvd();
                if (iMediaInterface != null) {
                    iMediaInterface.onSeekComplete();
                }
            }
        });
    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {

    }
}
