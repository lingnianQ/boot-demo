package com.baidu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author sytsnb@gmail.com
 * @Date 2022 2022/8/11 14:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article implements Serializable {
    private static final long serialVersionUID = 42L;

    private String title;
    private String content;
}
