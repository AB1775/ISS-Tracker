package com.isstracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseParser {
    static final ObjectMapper objectMapper = new ObjectMapper();

    public static class ISSDataTemplate {
        String message;
        long timestamp;
        double latitude;
        double longitude;
    }

    public static class ISSPosition {
        @JsonProperty("latitude")
        private double latitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        @JsonProperty("longitude")
        private double longitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public static class ISSResponse {
        @JsonProperty("timestamp")
        private long timestamp;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @JsonProperty("iss_position")
        private ISSPosition issPosition;

        public ISSPosition getISSPosition() {
            return issPosition;
        }

        public void setISSPosition(ISSPosition issPosition) {
            this.issPosition = issPosition;
        }

        @JsonProperty("message")
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static ISSDataTemplate populateDataObject() throws Exception {
        String response = APIClient.getAPIResponse(APIClient.apiURL);
        ISSResponse issResponseOM = objectMapper.readValue(response, ISSResponse.class);

        String message = issResponseOM.getMessage();
        long timestamp = issResponseOM.getTimestamp();
        double longitude = issResponseOM.issPosition.getLongitude();
        double latitude = issResponseOM.issPosition.getLatitude();

        ISSDataTemplate issObject = new ISSDataTemplate();
        issObject.message = message;
        issObject.timestamp = timestamp;
        issObject.longitude = longitude;
        issObject.latitude = latitude;

        return issObject;
    }
}
