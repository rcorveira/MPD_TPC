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

/**
 *
 * @author Rui Corveira
 */
public class BindStringToUpperCase<T> implements BindMember<T> {

    private final BindMember bindMember;

    public BindStringToUpperCase(BindMember bindMember) {
        if(bindMember instanceof BindStringToUpperCase)
            throw new IllegalArgumentException("BindMember cannot be an instance of BindStringToUpperCase");
        this.bindMember = bindMember;
    }

    /**
     * bind - Default implementation - Convert Strings to UpperCase
     *
     * @param target
     * @param name
     * @param v
     * @return
     */
    public boolean bind(T target, String name, Object v) {
        if (v == null || !(v.getClass().getSimpleName().equalsIgnoreCase("string"))) {
            return false;
        }

        return bindMember.bind(target, name, convertToUpperCase(v));

    }

    private Object convertToUpperCase(Object o){
        return ((String)o).toUpperCase();

    }

}
