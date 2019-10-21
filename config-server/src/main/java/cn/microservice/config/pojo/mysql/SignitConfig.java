/*
 * This file is generated by jOOQ.
*/
package cn.microservice.config.pojo.mysql;


import cn.microservice.config.pojo.mysql.tables.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SignitConfig extends SchemaImpl {

    private static final long serialVersionUID = -1886520281;

    /**
     * The reference instance of <code>signit_config</code>
     */
    public static final SignitConfig SIGNIT_CONFIG = new SignitConfig();

    /**
     * The table <code>signit_config.property</code>.
     */
    public final Property PROPERTY = cn.microservice.config.pojo.mysql.tables.Property.PROPERTY;

    /**
     * No further instances allowed
     */
    private SignitConfig() {
        super("signit_config", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Property.PROPERTY);
    }
}
