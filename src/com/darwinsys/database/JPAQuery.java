package demo;

import java.io.Console;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/*
 * Interactive query runner.
 * Could be a nice general-purpose program but for the complexity
 * of setting JPA up for each project.
 */
public class JPAQuery {

	static EntityManagerFactory entityMgrFactory = null;

	public static void main(String[] args) {

		String unitName = args.length == 0 ?  "jpademo" : args[0];

		JPAQuery program = new JPAQuery();

		// Do i/o initializations before JPA to fail fast
		program.init();

		System.out.println("JPAQuery.main() -- starting JPA");

		// This would be done for you
		// were you running in an EE App Server
		EntityManager entityManager = null;
		try {
			entityMgrFactory = 
			Persistence.createEntityManagerFactory(unitName);
			System.out.println("Created EntityManagerFactory");
			entityManager = entityMgrFactory.createEntityManager();

			// Delegate
			program.run(entityManager);

		} catch (Exception e) {
			System.out.println("teh jpa she fail: " + e);
			return;
		} finally {	
			if (entityManager != null)
				entityManager.close();
			if (entityMgrFactory != null)
				entityMgrFactory.close();
		}
	}
		
	Console console;

	void init() {
                if ((console = System.console()) == null) {
			throw new RuntimeException("Can't get Console");
		}
	}

	@SuppressWarnings("unchecked")
	void run(EntityManager entityManager) throws Exception {

		String queryStr;
		Query query = null;


		while(true) {

			try {
			queryStr = console.readLine("Enter JPA Query: ");
			if (queryStr == null || "quit".equals(queryStr)) {
				return;
			}

			query = entityManager.createQuery(queryStr);

			List<Object> list = query.getResultList();
			System.out.println("Found " + list.size() + " results:");
			for (Object o : list) {
				System.out.println(o);
			}
			System.out.println();
			} catch (Exception e) {
				System.out.println("Error: " + e);
			}
		}

	}
}
