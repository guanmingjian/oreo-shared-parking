package cn.oreo.common.util.entity.old;

import lombok.ToString;

@ToString
public class Map {
    private Integer id;

    private Integer clas;

    private Double longitude;

    private Double latitude;

    private String location;

    private Integer carportTotal;

    private Integer carportUsed;

    private Integer carTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClas() {
        return clas;
    }

    public void setClas(Integer clas) {
        this.clas = clas;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public Integer getCarportTotal() {
        return carportTotal;
    }

    public void setCarportTotal(Integer carportTotal) {
        this.carportTotal = carportTotal;
    }

    public Integer getCarportUsed() {
        return carportUsed;
    }

    public void setCarportUsed(Integer carportUsed) {
        this.carportUsed = carportUsed;
    }

    public Integer getCarTotal() {
        return carTotal;
    }

    public void setCarTotal(Integer carTotal) {
        this.carTotal = carTotal;
    }
}