package com.aclib.aclib_deploy.ThirdPartyService;

import java.util.List;

public class GoogleBooksResponse {
    private List<Item> items;

    // Getters and setters
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    public static class Item {
        private String id;
        private String selfLink;
        private VolumeInfo volumeInfo;

        // Getters and setters
        public VolumeInfo getVolumeInfo() {
            return volumeInfo;
        }

        public void setVolumeInfo(VolumeInfo volumeInfo) {
            this.volumeInfo = volumeInfo;
        }

        //Only getter
        public String getId() {
            return id;
        }

        //getter and setter selfLinks
        public String getSelfLink() {
            return selfLink;
        }

        public void setSelfLink(String selfLink) {
            this.selfLink = selfLink;
        }
    }
}