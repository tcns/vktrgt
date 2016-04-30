package ru.tcns.vktrgt.domain.util.parser;

import org.apache.commons.beanutils.PropertyUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.tcns.vktrgt.anno.JsonEntity;
import ru.tcns.vktrgt.anno.JsonField;
import ru.tcns.vktrgt.anno.JsonIgnore;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by TIMUR on 29.04.2016.
 */
public class ResponseParser<T> {
    public T parseResponseString (String answer, String mainObject, Class<T> clazz) {
        JSONObject object = new JSONObject(answer);
        try {
            return parseObject(object.getJSONObject(mainObject), clazz);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
    public T parseObject(JSONObject object, Class<T> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        T t = build(clazz);
        Boolean jsonClass = clazz.isAnnotationPresent(JsonEntity.class);
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(JsonIgnore.class)) {
                continue;
            }
            JsonField ann = null;
            Boolean annPresent = f.isAnnotationPresent(JsonField.class);
            String defaultName = "";
            Boolean isRequired = false;
            if (annPresent) {
                ann = f.getAnnotation(JsonField.class);
                defaultName = ann.name();
                isRequired = ann.required();
            }
            String fieldName = f.getName();
            Class fieldType = f.getType();
            if (jsonClass || annPresent) {
                String jsonField = defaultName;
                if (defaultName.isEmpty()) {
                    jsonField = convertName(fieldName);
                }
                if (fieldType.isAnnotationPresent(JsonEntity.class)) {
                    if (isRequired) {
                        f.set(t, parseObject(object.getJSONObject(jsonField), fieldType));
                    } else {
                        if (object.optJSONObject(jsonField)!=null) {
                            setField(f, t, parseObject(object.optJSONObject(jsonField), fieldType));
                        } else {
                            setField(f, t, null);
                        }
                    }
                } else if (!(Collection.class.isAssignableFrom(fieldType))) {
                    if (isRequired) {
                        setField(f, t, object.get(jsonField));
                    } else {
                        setField(f, t, object.opt(jsonField));
                    }
                } else {
                    JSONArray array = object.getJSONArray(jsonField);
                    Class generic = (Class)((ParameterizedType)f.getGenericType()).getActualTypeArguments()[0];
                    Collection collection = null;
                    if (Set.class.isAssignableFrom(fieldType)) {
                        collection = new HashSet<>();
                    } else {
                        collection = new ArrayList<>();
                    }
                    for (int i = 0; i<array.length(); i++) {
                        if (Number.class.isAssignableFrom(generic)) {
                            collection.add(array.get(i));
                        } else if (generic.isAnnotationPresent(JsonEntity.class)) {
                            collection.add(parseObject(array.getJSONObject(i), generic));
                        }
                    }
                    setField(f, t, collection);
                }
            }

        }
        return t;
    }

    void setField(Field f, Object t, Object val) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PropertyUtils.setSimpleProperty(t, f.getName(), val);
    }
    String convertName(String input) {
        String[] strings = input.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i].toLowerCase());
            if (i < strings.length - 1) {
                builder.append("_");
            }
        }
        return builder.toString();
    }

    private T build(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }
}
