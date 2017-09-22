package jooq_7_steps;

import static apt.jooq.employees.Tables.EMPLOYEES_;
import static apt.jooq.employees.Tables.SALARIES;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Log.Level;
import org.jooq.impl.DSL;


public class Jooq_Update {
    public static void main(String[] args) {
        String userName = "empuser";
        String password = "empuser";
        String url = "jdbc:mysql://75.101.255.220:3306/employees?useSSL=false";

        // just for this example, set the logging level for less verbosity
        org.jooq.tools.JooqLogger.globalThreshold(Level.WARN);

        // Connection is the only JDBC resource that we need
        // PreparedStatement and ResultSet are handled by jOOQ, internally
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

            // update a salary record
            create.update(SALARIES)
                  .set(SALARIES.SALARY, 2000000)
                  .where(SALARIES.EMP_NO.eq(1))
                  .execute();
            
            // query to make sure we updated the record
            Result<?> result = create
                    .select(EMPLOYEES_.FIRST_NAME, 
                            EMPLOYEES_.LAST_NAME, 
                            SALARIES.SALARY)
                    .from(SALARIES)
                    .join(EMPLOYEES_)
                    .on(EMPLOYEES_.EMP_NO.eq(SALARIES.EMP_NO))
                    .where(SALARIES.EMP_NO.eq(1))
                    .fetch();
            
            System.out.println(result);

            /* STDOUT
            +----------+---------+-------+
            |first_name|last_name| salary|
            +----------+---------+-------+
            |Linus     |Torvalds |2000000|
            +----------+---------+-------+
             */
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
