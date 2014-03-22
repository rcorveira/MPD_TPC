package pt.isel.mpd14.probe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Binder {

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

    public static <T> T bindToProps(Class<T> klass, Map<String, Object> vals)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (klass == null || vals == null) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> aux = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        aux.putAll(vals);
        vals = aux;

        T target = klass.newInstance();
        Method[] ms = klass.getMethods();
        for (Method m : ms) {
            String mName = m.getName();
            if (mName.substring(0, 3).compareTo("set") != 0) {
                continue;
            }
            String propName = mName.substring(3);
            if (!vals.containsKey(propName)) {
                continue;
            }
            Object v = vals.get(propName);
            Class<?>[] paramsKlasses = m.getParameterTypes();
            if (paramsKlasses.length != 1) {
                continue;
            }
            Class<?> propType = WrapperUtilites.toWrapper(paramsKlasses[0]);
            if (propType.isAssignableFrom(v.getClass())) {
                m.setAccessible(true);
                m.invoke(target, v);
            }
        }
        return target;
    }

    public static <T> T bindToFields(Class<T> klass, Map<String, Object> vals)
            throws InstantiationException, IllegalAccessException {
        T target = klass.newInstance();
        Field[] fields = klass.getDeclaredFields();
        for (Field f : fields) {
            bindField(target, vals, f);
        }
        return target;
    }

    public static <T> T bindToFieldsAndProperties(Class<T> klass, Map<String, Object> vals)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {

        if (klass == null || vals == null) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> aux = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        aux.putAll(vals);
        vals = aux;

        T target = klass.newInstance();

        Field[] fields = klass.getDeclaredFields();

        for (Field field : fields) {
            if (!bindProp(target, vals, field)) {
                bindField(target, vals, field);
            }
        }

        return target;
    }

    private static <T> boolean bindProp(T target, Map<String, Object> vals, Field field) throws InvocationTargetException, IllegalAccessException {

        Method[] ms = target.getClass().getMethods();
        String fName = field.getName();
        for (Method m : ms) {
            String mName = m.getName();
            if (mName.substring(0, 3).compareTo("set") != 0) {
                continue;
            }
            String propName = mName.substring(3);
            if (!vals.containsKey(propName)) {
                continue;
            }
            Object v = vals.get(propName);
            Class<?>[] paramsKlasses = m.getParameterTypes();
            if (paramsKlasses.length != 1) {
                continue;
            }
            Class<?> propType = WrapperUtilites.toWrapper(paramsKlasses[0]);
            if (propType.isAssignableFrom(v.getClass())) {
                m.setAccessible(true);
                m.invoke(target, v);
                return true;
            }
        }
        return false;
    }

    public static <T> boolean bindField(T target, Map<String, Object> vals, Field f)
            throws IllegalArgumentException, IllegalAccessException {
        String fName = f.getName();
        if (vals.containsKey(fName)) {
            Class<?> fType = f.getType();
            Object fValue = vals.get(fName);
            f.setAccessible(true);
            if (fType.isPrimitive()) {
                fType = f.get(target).getClass();
            }
            /*
             * Verifica se o tipo do campo (fType) é tipo base do tipo de fValue.
             * Nota: Tipo base inclui superclasses ou superinterfaces.
             */
            if (fType.isAssignableFrom(fValue.getClass())) {
                f.set(target, fValue);
                return true;
            } else {
                return false;
            }
        }

        return false;
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
