package jooq_7_steps;

import static apt.jooq.employees.Tables.EMPLOYEES_;
import static apt.jooq.employees.Tables.SALARIES;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Log.Level;
import org.jooq.impl.DSL;

import apt.jooq.employees.enums.EmployeesGender;

public class Jooq_Join {
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

			// create some field aliases for a cleaner query
			Field<Integer> maxS = DSL.max(SALARIES.SALARY);
			Field<Integer> empNo = EMPLOYEES_.EMP_NO;
			Field<EmployeesGender> gender = EMPLOYEES_.GENDER;
			
			// find the 10 highest (ever) earners in the company by gender
			Result<?> highEarners = create
								.select(empNo, gender, maxS)
								.from(EMPLOYEES_)
								.join(SALARIES)
								.on(empNo.eq(SALARIES.EMP_NO))
								.groupBy(empNo)
								.orderBy(maxS.desc())
								.limit(10)
								.fetch();
			
			System.out.println(highEarners);
			
			/* STDOUT
			 * +------+------+------+
			|emp_no|gender|   max|
			+------+------+------+
			| 43624|M     |158220|
			|254466|M     |156286|
			| 47978|M     |155709|
			|253939|M     |155513|
			|109334|M     |155377|
			| 80823|M     |154459|
			|493158|M     |154376|
			|205000|M     |153715|
			|266526|F     |152710|
			|237542|F     |152687|
			+------+------+------+
			 */
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
