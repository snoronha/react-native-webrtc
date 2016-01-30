package com.oney.WebRTCModule;

import android.opengl.GLSurfaceView;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;

import org.webrtc.MediaStream;
import org.webrtc.RendererCommon;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

public class RTCVideoViewManager extends SimpleViewManager<GLSurfaceView> {
    private final static String TAG = RTCVideoViewManager.class.getCanonicalName();

    public static final String REACT_CLASS = "RTCVideoView";
    public ThemedReactContext mContext;
    private VideoRenderer.Callbacks localRender;
    private WritableMap _properties;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @UIProp(UIProp.Type.NUMBER)
    public static final String PROP_STREAM_URL = "streamURL";

    @Override
    public GLSurfaceView createViewInstance(ThemedReactContext context) {
        mContext = context;
        GLSurfaceView view = new GLSurfaceView(context);
        // view.setPreserveEGLContextOnPause(true);
        // view.setKeepScreenOn(true);
        VideoRendererGui.setView(view, new Runnable() {
            @Override
            public void run() {
            }
        });

        RendererCommon.ScalingType scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
        localRender = VideoRendererGui.create(
                0, 0,
                100, 100, scalingType, false);
        return view;
    }

    @ReactProp(name = PROP_STREAM_URL, defaultInt = -1)
    public void setStreamId(GLSurfaceView view, @Nullable int streamId) {

        if (streamId >= 0) {
            WebRTCModule module = mContext.getNativeModule(WebRTCModule.class);
            MediaStream mediaStream = module.mMediaStreams.get(streamId);
            Log.d(TAG, "set mediaStream: " + (mediaStream == null ? "null" : "nonull"));

            RendererCommon.ScalingType scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;

            mediaStream.videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
            VideoRendererGui.update(localRender,
                    0, 0,
                    100, 100, scalingType, false);
        }
    }

    private WritableMap getProperties() {
        if (_properties == null) {
            _properties = Arguments.createMap();
        }
        return _properties;
    }

}
