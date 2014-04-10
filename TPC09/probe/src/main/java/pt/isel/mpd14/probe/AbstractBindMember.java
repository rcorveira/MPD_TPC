/*
 * Copyright (C) 2014 Miguel Gamboa at CCISEL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.isel.mpd14.probe;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import pt.isel.mpd14.probe.util.SneakyUtils;

/**
 *
 * @author Miguel Gamboa at CCISEL
 */
public abstract class AbstractBindMember<T> implements BindMember<T> {

    protected Map<AnnotatedElement, Formatter> formats = new HashMap<>();

    protected void addFormatter(AnnotatedElement mb, Class<?> klass) {
        Format a = mb.getAnnotation(Format.class);
        if (a != null) try {
            Class<? extends Formatter> klassFrt = a.formatterKlass();
            if (klassFrt != Formatter.class) {
                Formatter frt = klassFrt.newInstance();
                formats.put(mb, frt);
            }
            else if (!a.formatterMethod().equals("")) {
                Method m = klass.getDeclaredMethod(a.formatterMethod(), String.class);
                m.setAccessible(true);
                formats.put(mb, getFormatter(mb, m));  // o -> m.invoke(mb, o)
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException ex) {
            SneakyUtils.throwAsRTException(ex);
        }
    }

    protected Object format(AnnotatedElement mb, Object v) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Formatter f = formats.get(mb);
        if (f != null) {
            v = f.format(v);
        }

        return v;
    }

    private Formatter getFormatter(AnnotatedElement mb, Method m){
        return new Formatter(){

            @Override
            public Object format(Object o) throws InvocationTargetException, IllegalAccessException {
                return m.invoke(mb, o);
            }
        };
    }
}
