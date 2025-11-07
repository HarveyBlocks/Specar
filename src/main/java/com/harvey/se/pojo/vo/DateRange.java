package com.harvey.se.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author <a href="mailto:harvey.blocks@outlook.com">Harvey Blocks</a>
 * @version 1.0
 * @date 2025-11-08 07:05
 */
@Data
public class DateRange implements Serializable {
    /**
     * null for 不设限制
     */
    private final Date from;
    /**
     * null for 不设限制
     */
    private final Date to;
}