package pt.isel.mpd14.probe;

import java.util.Map;

/**
 * Created by rcorveira on 27/03/2014.
 */
public class BinderPropsAndFields implements BinderMember{


    @Override
    public <T> T bind(Class<T> klass, Map<String, Object> vals) {
        return null;
    }
}

/*

class BinderPropsAndFields extends AbstractBinder{

    private AbstractBinder binderF = new BinderFields();
    private AbstractBinder binderP = new BinderProps();


    @Override
    <T> boolean bindMember(T target, String key, Object value) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(!binderP.bindMember(target, key, value))
            return binderF.bindMember(target, key, value);
        return true;
    }


}


 */
