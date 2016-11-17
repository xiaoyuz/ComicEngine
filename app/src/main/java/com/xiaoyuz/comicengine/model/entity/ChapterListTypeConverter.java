package com.xiaoyuz.comicengine.model.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-17.
 */
public class ChapterListTypeConverter implements PropertyConverter<List<BaseChapter>, String> {

    @Override
    public List<BaseChapter> convertToEntityProperty(String databaseValue) {
        Gson gson = new Gson();
        Type type =  new TypeToken<List<BaseChapter>>(){}.getType();
        return gson.fromJson(databaseValue, type);
    }

    @Override
    public String convertToDatabaseValue(List<BaseChapter> entityProperty) {
        Gson gson = new Gson();
        return gson.toJson(entityProperty);
    }
}
