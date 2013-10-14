package com.darwinsys.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ObjectMergeTest {

	static class Person {
		Long id;
		String firstName;
		String lastName;
		public Person() {
			// empty
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		@Override
		public String toString() {
			return firstName + " " + lastName;
		}
		@Override
		public int hashCode() {
			final int PRIME = 31;
			int result = 1;
			result = PRIME * result + ((firstName == null) ? 0 : firstName.hashCode());
			result = PRIME * result + ((id == null) ? 0 : id.hashCode());
			result = PRIME * result + ((lastName == null) ? 0 : lastName.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Person other = (Person) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (firstName == null) {
				if (other.firstName != null)
					return false;
			} else if (!firstName.equals(other.firstName))
				return false;
			if (lastName == null) {
				if (other.lastName != null)
					return false;
			} else if (!lastName.equals(other.lastName))
				return false;
			return true;
		}
	}
	Person c1 = new Person();
	Person c2 = new Person();
	Person result = null;

	@Before
	public void setup() {
	}
	
	@Test
	public void testMergeOneString() throws Exception {
		c1.setFirstName("ABC");
		c2.setFirstName(null);
		result = (Person) ObjectMerge.merge(c2, c1);
		assertEquals("One String", "ABC", result.getFirstName());
	}

	@Test
	public void testMergeTwoStrings() throws Exception {
		c1.setFirstName("Ian");
		c2.setLastName("Darwin");
		result = (Person) ObjectMerge.merge(c1, c2);
		assertEquals("Merge Nulls", "Ian Darwin", result.toString());
	}
	
	@Test
	public void testMergeInts() throws Exception {
		c1.setId(42L);
		result = (Person) ObjectMerge.merge(c1, c1);
		assertEquals("Merge Integers", Long.valueOf(42), result.getId());
	}
	
	@Test
	public void testTransitivity() throws Exception {
		c1.setFirstName("Robin");
		assertTrue("Transitivity test", ObjectMerge.merge(c1,c2).equals(ObjectMerge.merge(c2,c1)));
	}
}
