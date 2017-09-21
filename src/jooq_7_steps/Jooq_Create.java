package jooq_7_steps;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import static apt.jooq.employees.Tables.EMPLOYEES_;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Log.Level;
import org.jooq.impl.DSL;

public class Jooq_Create {
	public static void main(String[] args) {
			
		String userName = "empuser";
		String password = "empuser";
		String url = "jdbc:mysql://75.101.255.220:3306/employees?useSSL=false";
	
		// Connection is the only JDBC resource that we need
		// PreparedStatement and ResultSet are handled by jOOQ, internally
		try (Connection conn = DriverManager.getConnection(url, userName, password)) {
			
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
	
			// just for this example, set the logging level for less verbosity
			org.jooq.tools.JooqLogger.globalThreshold(Level.WARN);
			
			// create a new table called MILLENIALS
			create.createTable("MILLENIALS").as(
				       create.select(
				    	 EMPLOYEES_.EMP_NO,
				         EMPLOYEES_.FIRST_NAME,
				         EMPLOYEES_.LAST_NAME,
				         EMPLOYEES_.BIRTH_DATE)
				      .from(EMPLOYEES_)
				      .where(EMPLOYEES_.BIRTH_DATE.gt(Date.valueOf("1982-01-01")))
				      ).execute();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
