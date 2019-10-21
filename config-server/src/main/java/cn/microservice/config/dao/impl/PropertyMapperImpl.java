package cn.microservice.config.dao.impl;

import static cn.microservice.config.pojo.mysql.tables.Property.PROPERTY;

import java.util.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.microservice.config.dao.PropertyMapper;
import cn.microservice.config.pojo.mysql.tables.pojos.Property;
import cn.microservice.config.pojo.mysql.tables.records.PropertyRecord;

/**
 * 配置中心,配置文件数据库持久化操作实现.
 *
 * @author zhangbo
 * @since 1.0.0
 */
@Repository
public class PropertyMapperImpl implements PropertyMapper {

    @Autowired
    DSLContext dsl;

    private static final int DEFAULT_VERSION = 1;

    protected Date getCurrentDate() {
        // TODO 获取本地时间是存在问题的，最好是根据项目，自行封装一个获取时间的方法
        return new Date();
    }

    /**
     * 添加一个配置.
     *
     * @param application
     *            application
     * @param profile
     *            profile
     * @param label
     *            label
     * @param properties
     *            配置文件的string格式
     * @return 是否添加成功
     * @author zhangbo
     * @since 1.0.0
     */
    public Boolean insertOne(String application, String profile, String label, String properties) {
        PropertyRecord record = dsl.selectFrom(PROPERTY)
                .where(keyCondition(application, profile, label))
                .orderBy(PROPERTY.VERSION.desc())
                .limit(1)
                .fetchOne();
        PropertyRecord insertedRecord = dsl.newRecord(PROPERTY);
        if (record != null) {
            insertedRecord.setVersion(record.getVersion() + 1);
        } else {
            insertedRecord.setVersion(DEFAULT_VERSION);
        }
        insertedRecord.setActive(true);
        insertedRecord.setApplication(application);
        insertedRecord.setProfile(profile);
        insertedRecord.setLabel(label);
        insertedRecord.setProperty(properties);
        insertedRecord.setCreatedDatetime(getCurrentDate());
        return insertedRecord.insert() == 1;
    }

    /**
     * 条件查询配置.
     *
     * @param application
     *            application
     * @param profile
     *            profile
     * @param label
     *            label
     * @param otherQueryCondition
     *            额外的查询条件
     * @return 查询返回的列表结果
     * @author zhangbo
     * @since 1.0.0
     */
    public List<Property> selectAll(String application, String profile, String label, Condition otherQueryCondition) {
        Condition filters = keyCondition(application, profile, label);
        filters = otherQueryCondition != null ? filters.and(otherQueryCondition) : filters;
        return dsl.selectFrom(PROPERTY)
                .where(filters)
                .fetch()
                .into(Property.class);
    }

    /**
     * 批量更新某项属性.
     * 
     * @param <T>
     *            字段数据类型
     * 
     * @param field
     *            更新字段
     * @param value
     *            更新值
     * @param updateCondition
     *            更新条件
     * @return 更新数量
     * @author zhangbo
     * @since 1.0.0
     */
    public <T> Integer updateBatch(Field<T> field, T value, Condition updateCondition) {
        return dsl.update(PROPERTY)
                .set(PROPERTY.MODIFIED_DATETIME, getCurrentDate())
                .set(field, value)
                .where(updateCondition)
                .execute();
    }

    private Condition keyCondition(String application, String profile, String label) {
        return PROPERTY.APPLICATION.eq(application)
                .and(PROPERTY.PROFILE.eq(profile))
                .and(PROPERTY.LABEL.eq(label));
    }

}
