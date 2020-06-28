package uchet.repository;

import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.DateType;
import org.hibernate.type.TimestampType;

public class MySQLServerDialect extends MySQLDialect {

    public MySQLServerDialect() {
        super();
        registerFunction("ADD_DATE", new SQLFunctionTemplate(DateType.INSTANCE, "date_add(?1, INTERVAL ?2 ?3)"));
    }
}
