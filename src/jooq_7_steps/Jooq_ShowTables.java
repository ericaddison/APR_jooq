package jooq_7_steps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.Log.Level;
import org.jooq.impl.DSL;

public class Jooq_ShowTables {
	public static void main(String[] args) {
		String userName = "empuser";
		String password = "empuser";
		String url = "jdbc:mysql://75.101.255.220:3306/employees?useSSL=false";

		// Connection is the only JDBC resource that we need
		// PreparedStatement and ResultSet are handled by jOOQ, internally
		try (Connection conn = DriverManager.getConnection(url, userName, password)) {
			
			// just for this example, set the logging level for less verbosity
			org.jooq.tools.JooqLogger.globalThreshold(Level.WARN);
						
			// get a list of all the tables in the database and print
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
			for(Table<?> table : create.meta().getTables())
				System.out.println("DB Table: " + table);
			
			/* STDOUT
			DB Table: "employees"."departments"
			DB Table: "employees"."dept_emp"
			DB Table: "employees"."dept_manager"
			DB Table: "employees"."employees"
			DB Table: "employees"."salaries"
			DB Table: "employees"."titles"
			DB Table: "employees"."current_dept_emp"
			DB Table: "employees"."dept_emp_latest_date"
			...
			 */
			
			// Run queries or SQL commands through the create object			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
