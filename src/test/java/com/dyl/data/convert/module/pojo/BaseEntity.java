package com.dyl.data.convert.module.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class  BaseEntity<ID extends Serializable> implements Serializable  {

    private ID id;
}
