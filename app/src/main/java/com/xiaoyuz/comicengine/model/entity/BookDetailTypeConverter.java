package com.xiaoyuz.comicengine.model.entity;

import com.google.gson.Gson;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by zhangxiaoyu on 16-11-17.
 */
public class BookDetailTypeConverter implements PropertyConverter<BaseBookDetail, String> {

    @Override
    public BaseBookDetail convertToEntityProperty(String databaseValue) {
        Gson gson = new Gson();
        return gson.fromJson(databaseValue, BaseBookDetail.class);
    }

    @Override
    public String convertToDatabaseValue(BaseBookDetail entityProperty) {
        Gson gson = new Gson();
        return gson.toJson(entityProperty);
    }
}
