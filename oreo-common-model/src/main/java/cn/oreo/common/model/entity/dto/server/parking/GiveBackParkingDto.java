package cn.oreo.common.model.entity.dto.server.parking;

import lombok.Data;

@Data
public class GiveBackParkingDto {

    private Long parkingId;

    private Long communityId;

    private Integer parkingStartTime;

    private Integer parkingEndTime;
}
