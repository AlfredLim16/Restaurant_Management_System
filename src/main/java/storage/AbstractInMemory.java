package storage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author admin
 * @param <EntityType>
 */
public abstract class AbstractInMemory<EntityType> {

    protected final HashMap<Integer, EntityType> storage = new HashMap<>();
    protected int nextEntityId = 1;

    protected abstract int getEntityId(EntityType entity);
    protected abstract void setEntityId(EntityType entity, int id);

    public void create(EntityType newEntity){
        setEntityId(newEntity, nextEntityId++);
        storage.put(getEntityId(newEntity), newEntity);
    }

    public EntityType get(int entityId){
        return storage.get(entityId);
    }

    public ArrayList<EntityType> getAll(){
        return new ArrayList<>(storage.values());
    }

    public void update(EntityType existingEntity){
        int id = getEntityId(existingEntity);
        if(storage.containsKey(id)){
            storage.put(id, existingEntity);
        }
    }

    public void delete(int entityId){
        storage.remove(entityId);
    }
}
