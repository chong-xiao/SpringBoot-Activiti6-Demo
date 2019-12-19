package com.cazj.common.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * ZtreeNode中的数据
 * @author UID
 *
 */
@Data
public class Node implements Serializable { 
	private static final long serialVersionUID = 2772838767764045571L;
	private Integer id;
	private String name;
	private Integer parentId;
}
