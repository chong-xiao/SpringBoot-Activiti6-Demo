package com.cazj.pojo;

import java.io.Serializable;

import lombok.Data;

@Data
public class EmpRole implements Serializable {

	private static final long serialVersionUID = 1197446019647206808L;
	private Integer id;
	private Integer empId;
	private Integer roleId;
}
