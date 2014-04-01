package pt.isel.mpd14.probe;

/**
 * Created by rcorveira on 01/04/2014.
 */
public class BindPropNonNull extends BindProp {
    @Override
    public <T> boolean bind(T target, String name, Object v) {
        if(v == null)
            return false;

        return super.bind(target, name, v);

    }
}
