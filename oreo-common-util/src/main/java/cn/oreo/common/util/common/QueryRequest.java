package cn.oreo.common.util.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 查询条件
 *
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Data
@ToString
public class QueryRequest implements Serializable {

    private static final long serialVersionUID = -4869594085374385813L;
    /**
     * 当前页面数据量
     */
    @ApiModelProperty(value = "一页多少条，默认：10")
    private int pageSize = 10;
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前第几页，默认：1")
    private int pageNum = 1;
    /**
     * 排序字段
     * 单个字段
     */
    @ApiModelProperty(value = "需要排序的字段，单个字段")
    private String field;
    /**
     * 排序规则，asc升序，desc降序
     */
    @ApiModelProperty(value = "排序规则，asc升序，desc降序")
    private String order;

    /**
     * 是否开启驼峰转下划线，默认开启
     */
    @ApiModelProperty(value = "是否开启驼峰转下划线，需要用下划线命名法填false")
    private Boolean isToUnderlineCase = true;
}
