package ru.tcns.vktrgt.domain.external.vk.internal;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

/**
 * Created by TIMUR on 20.04.2016.
 */
public class QGroup extends EntityPathBase<Group> {
    public static final QGroup group = new QGroup("group");
    public final StringPath name = createString("name");
    public final StringPath screenName = createString("screenName");
    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QGroup(String variable) {
        super(Group.class, PathMetadataFactory.forVariable(variable));
    }

    public QGroup(Path<Group> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroup(PathMetadata<?> metadata) {
        super(Group.class, metadata);
    }
}
