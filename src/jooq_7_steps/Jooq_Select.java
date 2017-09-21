package jooq_7_steps;

import static apt.jooq.employees.Tables.EMPLOYEES_;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Log.Level;
import org.jooq.impl.DSL;

public class Jooq_Select {
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

			// find last names of employees hired after 1/1/2000
			Result<Record2<String,String>> lateHires = create
								.select(EMPLOYEES_.FIRST_NAME, EMPLOYEES_.LAST_NAME)
								.from(EMPLOYEES_)
								.where(EMPLOYEES_.HIRE_DATE.gt(Date.valueOf("2000-01-01")))
								.fetch();

			// count the number of employees born before 1955
			Result<Record1<Integer>> num = create.selectCount()
								.from(EMPLOYEES_)
								.where(EMPLOYEES_.BIRTH_DATE.lt(Date.valueOf("1955-01-01")))
								.fetch();
			
			System.out.println("Number of gray beards: " + num.getValue(0, 0));
			
			System.out.println(lateHires);
			
			/* STDOUT
			Number of gray beards: 67294
			+----------+----------+
			|first_name|last_name |
			+----------+----------+
			|Ulf       |Flexer    |
			|Seshu     |Rathonyi  |
			|Randi     |Luit      |
			|Ennio     |Alblas    |
			|Volkmar   |Perko     |
			|Xuejun    |Benzmuller|
			|Shahab    |Demeyer   |
			|Jaana     |Verspoor  |
			|Jeong     |Boreale   |
			|Yucai     |Gerlach   |
			|Bikash    |Covnot    |
			|Hideyuki  |Delgrande |
			+----------+----------+
			 */
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
