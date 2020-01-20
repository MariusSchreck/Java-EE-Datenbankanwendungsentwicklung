package de.hs.da.hskleinanzeigen.entity.json;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;

import java.lang.reflect.InvocationTargetException;

public class EntityIdResolver extends SimpleObjectIdResolver {

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey id) {
        Object res = resolveIdSimple(id);
        if (res != null){
            return res;
        }
        try {
            res = id.scope.getConstructor(id.key.getClass()).newInstance(id.key);
            this.bindItem(id, res);
            return res;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object resolveIdSimple(ObjectIdGenerator.IdKey id){
        return super.resolveId(id);
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {

        return resolverType.getClass() == getClass();
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        // 19-Dec-2014, tatu: Important: must re-create without existing mapping; otherwise bindings leak
        //    (and worse, cause unnecessary memory retention)
        return new EntityIdResolver();
    }
}
