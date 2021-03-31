package cn.oreo.common.model.entity.dto.server.order;

import lombok.Data;

/**
 * @author GuanMingJian
 * @since 2021/1/23
 */
@Data
public class CancelReserveDto {

    private Integer cancelType;
    private Long reserveId;

    public CancelReserveDto(Integer cancelType, Long reserveId) {
        this.cancelType = cancelType;
        this.reserveId = reserveId;
    }

    public CancelReserveDto() {

    }
}
