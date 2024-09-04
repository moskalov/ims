package lv.notes.ims.excpetion;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String entityName;
    private final Long id;

    public EntityNotFoundException(String entityName, Long entityId) {
        super(entityName + " with ID " + entityId + " not found");
        this.entityName = entityName;
        this.id = entityId;
    }
}
