package jooq_7_steps;

import static apt.jooq.library.Tables.*;

import java.sql.Connection;
import java.sql.DriverManager;

import org.jooq.DSLContext;
import org.jooq.Log.Level;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class Jooq_Insert {
	public static void main(String[] args) {
		String userName = "libuser";
		String password = "libuser";
		String url = "jdbc:mysql://75.101.255.220:3306/library?useSSL=false";

		// just for this example, set the logging level for less verbosity
		org.jooq.tools.JooqLogger.globalThreshold(Level.WARN);

		// Connection is the only JDBC resource that we need
		// PreparedStatement and ResultSet are handled by jOOQ, internally
		try (Connection conn = DriverManager.getConnection(url, userName, password)) {
			DSLContext create = DSL.using(conn, SQLDialect.MYSQL);

			// insert a record, ignoring if it already exists
			create.insertInto(AUTHOR, AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME)
				  .values(100, "Hermann", "Hesse")
				  .onDuplicateKeyIgnore()
				  .execute();

			Result<Record> result = create.select().from(AUTHOR).fetch();

			for (Record r : result) {
				Integer id = r.getValue(AUTHOR.ID);
				String firstName = r.getValue(AUTHOR.FIRST_NAME);
				String lastName = r.getValue(AUTHOR.LAST_NAME);

				System.out.println("ID: " + id + " first name: " + firstName + " last name: " + lastName);
			}
		}

		// For the sake of this tutorial, let's keep exception handling simple
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}