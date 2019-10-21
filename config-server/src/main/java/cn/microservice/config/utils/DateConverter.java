package cn.microservice.config.utils;

import java.sql.Timestamp;
import java.util.Date;

import org.jooq.Converter;

/**
 * 日期强制转换,从datetime转换到java.util.Date.
 * @author pxy
 * @since 1.0.0
 */
public class DateConverter implements Converter<Timestamp, Date> {

    private static final long serialVersionUID = 1L;

    /**
     * datetime转换到java.util.Date.
     *
     * @param timstamp
     *            数据库的日期数据，格式为datetime
     * @return java.util.Date 格式的日期
     * @see org.jooq.Converter#from(java.lang.Object).
     * @author zhangbo
     * @since 1.0.0
     */
    public Date from(Timestamp timstamp) {
        if (timstamp != null) {
            return new Date(timstamp.getTime());
        }
        return null;
    }

    /**
     * 
     * java.util.Date转换到datetime.
     *
     * @param date
     *            java.util.Date 格式的日期
     * @return 数据库的日期数据，格式为datetime
     * @see org.jooq.Converter#to(java.lang.Object).
     * @author zhangbo
     * @since 1.0.0
     */
    public Timestamp to(Date date) {
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    public Class<Timestamp> fromType() {
        return Timestamp.class;
    }

    public Class<Date> toType() {
        return Date.class;
    }

}
