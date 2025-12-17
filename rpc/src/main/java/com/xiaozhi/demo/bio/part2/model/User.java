package com.xiaozhi.demo.bio.part2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author DD
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String userName;
    private Boolean sex;
}