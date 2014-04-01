package pt.isel.mpd14.probe;

/**
 * Created by rcorveira on 01/04/2014.
 */
public class BindNonNull implements BindMember {

    private BindMember bm;

    public BindNonNull(BindMember newBM ){
        bm = newBM;
    }

    @Override
    public <T> boolean bind(T target, String name, Object v) {
        if(v == null){
            return false;
        }
        return bm.bind(target, name, v);
    }
}
