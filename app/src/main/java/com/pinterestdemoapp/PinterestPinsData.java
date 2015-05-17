package com.pinterestdemoapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PinterestPinsData implements Serializable {
    private PinterestData data;

    public PinterestData getData() {
        return data;
    }

    public void setData(PinterestData data) {
        this.data = data;
    }

    public class PinterestData implements Serializable {
        private List<PinterestPins> pins;

        public List<PinterestPins> getPins() {
            return pins;
        }

        public void setPins(List<PinterestPins> pins) {
            this.pins = pins;
        }
    }

    public class PinterestPins implements Serializable {
        private String description;
        private PinterestImages images;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


        public PinterestImages getImages() {
            return images;
        }

        public void setImages(PinterestImages images) {
            this.images = images;
        }
    }

    public class PinterestImages implements Serializable {
        @SerializedName("237x")
        private ImageDetails details;

        public ImageDetails getDetails() {
            return details;
        }

        public void setDetails(ImageDetails details) {
            this.details = details;
        }
    }

    public class ImageDetails implements Serializable {
        private String url;
        private int width;
        private int height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
