package cn.microservice.config.dao;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.jooq.Condition;
import org.jooq.Field;

import cn.microservice.config.pojo.mysql.tables.pojos.Property;

/**
 * 配置中心,配置文件数据库持久化操作.
 *
 * @author zhangbo
 * @since 1.0.0
 */
public interface PropertyMapper {

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
    Boolean insertOne(@Valid @NotBlank String application, @NotBlank String profile, @NotBlank String label,
            @NotBlank String properties);

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
    List<Property> selectAll(@Valid @NotBlank String application, @NotBlank String profile, @NotBlank String label,
            Condition otherQueryCondition);

    /**
     * 批量更新某项属性.
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
    public <T> Integer updateBatch(@Valid Field<T> field, @NotNull T value, @NotNull Condition updateCondition);
}
