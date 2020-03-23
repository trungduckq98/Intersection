package com.deathmarch.intersection.model;

public class Comment {
    private String cmtId;
    private String cmtPostId;
    private String cmtUserId;
    private String cmtContent;
    private long cmtTime;

    public Comment(String cmtId, String cmtPostId, String cmtUserId, String cmtContent, long cmtTime) {
        this.cmtId = cmtId;
        this.cmtPostId = cmtPostId;
        this.cmtUserId = cmtUserId;
        this.cmtContent = cmtContent;
        this.cmtTime = cmtTime;
    }

    public Comment() {
    }

    public String getCmtId() {
        return cmtId;
    }

    public void setCmtId(String cmtId) {
        this.cmtId = cmtId;
    }

    public String getCmtPostId() {
        return cmtPostId;
    }

    public void setCmtPostId(String cmtPostId) {
        this.cmtPostId = cmtPostId;
    }

    public String getCmtUserId() {
        return cmtUserId;
    }

    public void setCmtUserId(String cmtUserId) {
        this.cmtUserId = cmtUserId;
    }

    public String getCmtContent() {
        return cmtContent;
    }

    public void setCmtContent(String cmtContent) {
        this.cmtContent = cmtContent;
    }

    public long getCmtTime() {
        return cmtTime;
    }

    public void setCmtTime(long cmtTime) {
        this.cmtTime = cmtTime;
    }
}
