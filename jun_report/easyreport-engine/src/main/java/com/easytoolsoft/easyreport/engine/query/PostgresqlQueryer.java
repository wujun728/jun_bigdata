package com.easytoolsoft.easyreport.engine.query;

import com.easytoolsoft.easyreport.engine.data.ReportDataSource;
import com.easytoolsoft.easyreport.engine.data.ReportParameter;

/**
 * Postgresql数据库查询器类。
 * 在使用该查询器时,请先参考:https://jdbc.postgresql.org/download.html
 * 获取与相应版本的Postgresql jdbc driver,然后把相关jdbc driver的jar包加入该系统的类路径下(如WEB-INF/lib)
 *
 * @author Wujun
 */
public class PostgresqlQueryer extends AbstractQueryer implements Queryer {
    public PostgresqlQueryer(final ReportDataSource dataSource, final ReportParameter parameter) {
        super(dataSource, parameter);
    }
}
