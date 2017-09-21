package jooq_7_steps;

import static apt.jooq.employees.Tables.EMPLOYEES_;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.Log.Level;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import apt.jooq.employees.enums.EmployeesGender;

public class Jooq_Insert {
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

			// insert a record, ignoring if it already exists
			create.insertInto(EMPLOYEES_, EMPLOYEES_.EMP_NO, 
										  EMPLOYEES_.FIRST_NAME, 
										  EMPLOYEES_.LAST_NAME,
										  EMPLOYEES_.GENDER,
										  EMPLOYEES_.BIRTH_DATE,
										  EMPLOYEES_.HIRE_DATE)
				  .values(1, "Linus", 
						  "Torvalds", 
						  EmployeesGender.M, 
						  Date.valueOf("1969-12-28"), 
						  Date.valueOf("1991-08-25"))
				  .onDuplicateKeyIgnore()
				  .execute();

			Result<Record> result = create.select()
									.from(EMPLOYEES_)
									.where(EMPLOYEES_.EMP_NO.eq(1))
									.fetch();
			
			System.out.println(result);
			
			/* STDOUT RESULT 
			+------+----------+----------+---------+------+----------+
			|emp_no|birth_date|first_name|last_name|gender|hire_date |
			+------+----------+----------+---------+------+----------+
			|     1|1969-12-28|Linus     |Torvalds |M     |1991-08-25|
			+------+----------+----------+---------+------+----------+
			*/
			
		}

		// For the sake of this tutorial, let's keep exception handling simple
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}