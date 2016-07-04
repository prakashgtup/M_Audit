package com.maudit.model;

/**
 *
 */
public class MediaModel {
    String imagePath,imageName,imageBase64Value;
    int scheduleQuestionID,scheduleId,imageId, captureId,subSectionId,sectionId,questionMediaID,IsLocalImage,localSequenceNo,IsFromServer;

    public int getIsFromServer() {
        return IsFromServer;
    }

    public void setIsFromServer(int isFromServer) {
        IsFromServer = isFromServer;
    }

    public String getImageBase64Value() {
        return imageBase64Value;
    }

    public void setImageBase64Value(String imageBase64Value) {
        this.imageBase64Value = imageBase64Value;
    }

    public int getLocalSequenceNo() {
        return localSequenceNo;
    }

    public void setLocalSequenceNo(int localSequenceNo) {
        this.localSequenceNo = localSequenceNo;
    }

    public int getIsLocalImage() {
        return IsLocalImage;
    }

    public void setIsLocalImage(int isLocalImage) {
        IsLocalImage = isLocalImage;
    }

    public int getSubSectionId() {
        return subSectionId;
    }

    public void setSubSectionId(int subSectionId) {
        this.subSectionId = subSectionId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getQuestionMediaID() {
        return questionMediaID;
    }

    public void setQuestionMediaID(int questionMediaID) {
        this.questionMediaID = questionMediaID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }



    public int getScheduleQuestionID() {
        return scheduleQuestionID;
    }

    public void setScheduleQuestionID(int scheduleQuestionID) {
        this.scheduleQuestionID = scheduleQuestionID;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }



    public int getCaptureId() {
        return captureId;
    }

    public void setCaptureId(int captureId) {
        this.captureId = captureId;
    }






    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


}
