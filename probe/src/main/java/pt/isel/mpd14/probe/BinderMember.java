package pt.isel.mpd14.probe;

import java.util.Map;

/**
 * Created by rcorveira on 25/03/2014.
 */
interface BinderMember {

    public <T> boolean bind(Class<T> klass, Map<String, Object> vals);
}
