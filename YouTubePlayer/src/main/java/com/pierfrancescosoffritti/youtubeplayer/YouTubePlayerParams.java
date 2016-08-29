package com.pierfrancescosoffritti.youtubeplayer;

/**
 * https://developers.google.com/youtube/player_parameters#Parameters
 */
public class YouTubePlayerParams {
    private int autoplay = 0;
    private int autohide = 1;
    private int rel = 0;
    private int showinfo = 0;
    private int enablejsapi = 1;
    private int disablekb = 1;
    private String cc_lang_pref = "en";
    private int controls = 1;

    public int getAutoplay() {
        return autoplay;
    }

    public void setAutoplay(int autoplay) {
        this.autoplay = autoplay;
    }

    public int getAutohide() {
        return autohide;
    }

    public void setAutohide(int autohide) {
        this.autohide = autohide;
    }

    public int getRel() {
        return rel;
    }

    public void setRel(int rel) {
        this.rel = rel;
    }

    public int getShowinfo() {
        return showinfo;
    }

    public void setShowinfo(int showinfo) {
        this.showinfo = showinfo;
    }

    public int getEnablejsapi() {
        return enablejsapi;
    }

    public void setEnablejsapi(int enablejsapi) {
        this.enablejsapi = enablejsapi;
    }

    public int getDisablekb() {
        return disablekb;
    }

    public void setDisablekb(int disablekb) {
        this.disablekb = disablekb;
    }

    public String getCc_lang_pref() {
        return cc_lang_pref;
    }

    public void setCc_lang_pref(String cc_lang_pref) {
        this.cc_lang_pref = cc_lang_pref;
    }

    public int getControls() {
        return controls;
    }

    public void setControls(int controls) {
        this.controls = controls;
    }
}
