package pt.isel.mpd14.probe;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by rcorveira on 25/03/2014.
 */
public class BinderFields implements BinderMember {


    @Override
    public <T> boolean bind(Class<T> klass, Map<String, Object> vals) {
        Field[] fields = klass.getClass().getDeclaredFields();
        for (Field f : fields) {
            String fName = f.getName();
            if (fName.equals(key)) {
                Class<?> fType = f.getType();
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
        }
        return false;

    }

}
}


/*

public class BinderFields extends AbstractBinder{

    @Override
    <T> boolean bindMember(T target, String key, Object fValue) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                Field[] fields = target.getClass().getDeclaredFields();
        for (Field f : fields) {
            String fName = f.getName();
            if (fName.equals(key)) {
                Class<?> fType = f.getType();
                f.setAccessible(true);
                if (fType.isPrimitive()) {
                    fType = f.get(target).getClass();
                }
                /*
                 * Verifica se o tipo do campo (fType) é tipo base do tipo de fValue.
                 * Nota: Tipo base inclui superclasses ou superinterfaces.
                 */
/*
if (fType.isAssignableFrom(fValue.getClass())) {
        f.set(target, fValue);
        return true;
        } else {
        return false;
        }
        }
        }
        return false;

        }

        }

 */