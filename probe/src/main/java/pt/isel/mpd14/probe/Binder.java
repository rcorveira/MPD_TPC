package pt.isel.mpd14.probe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Binder {

    static BinderMember bindM;

    public Binder(BinderMember bindM) {
        if(bindM == null){
            throw new IllegalArgumentException();
        }
        Binder.bindM = bindM;
    }

    public static Map<String, Object> getFieldsValues(Object o)
            throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> res = new HashMap<>();
        Field[] fs = o.getClass().getDeclaredFields();
        for (Field f : fs) {
            f.setAccessible(true);
            res.put(f.getName(), f.get(o));
        }
        return res;
    }

    public static <T> boolean bindTo(Class<T> klass, Map<String, Object> vals)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return bindM.bind(klass, vals);

    }

    public static <T> T bindToFieldsAndProperties(Class<T> klass, Map<String, Object> vals)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        return new BinderPropsAndFields().bind(klass, vals);

    }
}
class WrapperUtilites {

    final static Map<Class<?>, Class<?>> wrappers = new HashMap<>();

    static {
        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(short.class, Short.class);
        wrappers.put(boolean.class, Boolean.class);
        wrappers.put(int.class, Integer.class);

    }

    public static Class<?> toWrapper(Class<?> c) {
        return c.isPrimitive() ? wrappers.get(c) : c;
    }

}
