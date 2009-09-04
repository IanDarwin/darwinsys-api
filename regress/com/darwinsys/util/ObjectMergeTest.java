package rejmi.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rejmi.action.ObjectMerge;
import darwinian.contacts.Contact;

public class ObjectMergeTest {

	Contact c1 = new Contact();
	Contact c2 = new Contact();
	Contact result = null;

	@Before
	public void setup() {
	}
	
	@Test
	public void testMergeOneString() throws Exception {
		c1.setFirstName("ABC");
		c2.setFirstName(null);
		result = (Contact) ObjectMerge.merge(c2, c1);
		assertEquals("One String", "ABC", result.getFirstName());
	}

	@Test
	public void testMergeTwoStrings() throws Exception {
		c1.setFirstName("Ian");
		c2.setLastName("Darwin");
		result = (Contact) ObjectMerge.merge(c1, c2);
		assertEquals("Merge Nulls", "Ian Darwin", result.getDisplayName());
	}
	
	@Test
	public void testMergeInts() throws Exception {
		c1.setId(42L);
		result = (Contact) ObjectMerge.merge(c1, c1);
		assertEquals(42L, result.getId());
	}
	
	@Test
	public void testTransitivity() throws Exception {
		c1.setHomeCity("Hogtown");
		assertTrue("Transitivity test", ObjectMerge.merge(c1,c2).equals(ObjectMerge.merge(c2,c1)));
	}
}
