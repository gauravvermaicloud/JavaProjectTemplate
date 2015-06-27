package UnitTests;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
import com.boilerplate.java.entities.BaseEntity;

/**
 * This class is used to test the methods for Base
 * @author gaurav
 */
public class TestBase {

	/**
	 * This is the sample class to extend Base for testing
	 * @author gaurav
	 */
	public class InternalBaseExtender extends BaseEntity implements Serializable{
		
		/**
		 * This is a sample field
		 */
		String sampleField;
		
		/**
		 * Gets the sample field
		 * @return The sample field
		 */
		public String getSampleField() {
			return sampleField;
		}

		/**
		 * This sets the sample field
		 * @param sampleField The sample field
		 */
		public void setSampleField(String sampleField) {
			this.sampleField = sampleField;
		}

		@Override
		public boolean validate() throws ValidationFailedException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public BaseEntity transformToInternal() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BaseEntity transformToExternal() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	/**
	 * This method tests if the to string implementation contains correct values.
	 */
	@Test
	public void testToString(){
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		String toString = internalBaseExtender.toString();
		if(!toString.contains(internalBaseExtender.getSampleField())){
			Assert.fail("The Sample field value is not contained");
		}
		
		if(!toString.contains(internalBaseExtender.getId())){
			Assert.fail("The Id field value is not contained");
		}
	}
	
	/**
	 * This is used to check the JSON strings
	 */
	@Test
	public void testToJSON(){
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		String toString = internalBaseExtender.toJSON();
		if(!toString.contains(internalBaseExtender.getSampleField())){
			Assert.fail("The Sample field value is not contained");
		}
		
		if(!toString.contains(internalBaseExtender.getId())){
			Assert.fail("The Id field value is not contained");
		}
	}	
	
	/**
	 * This is used to check the static toJSON methods
	 */
	@Test
	public void testToJSONtatic(){
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		String toString = Base.toJSON(internalBaseExtender);
		if(!toString.contains(internalBaseExtender.getSampleField())){
			Assert.fail("The Sample field value is not contained");
		}
		
		if(!toString.contains(internalBaseExtender.getId())){
			Assert.fail("The Id field value is not contained");
		}
	}
	
	/**
	 * This is used to test the methods to convert a JSON into an object
	 */
	@Test
	public void testFromJSON(){
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		String toString = Base.toJSON(internalBaseExtender);
		InternalBaseExtender copy = Base.fromJSON(toString, InternalBaseExtender.class);
		Assert.assertEquals(internalBaseExtender, copy);
	}
	
	/**
	 * This is used to test the clone / deep copy methods
	 */
	@Test
	public void testClone(){
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		InternalBaseExtender copy = (InternalBaseExtender)internalBaseExtender.clone();
		Assert.assertEquals(internalBaseExtender, copy);
	}
	
	/**
	 * This is used to test the static clone methods
	 */
	@Test
	public void testCloneStatic() throws Exception{
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		InternalBaseExtender copy = (InternalBaseExtender)Base.clone(internalBaseExtender);
		Assert.assertEquals(internalBaseExtender, copy);
	}
	
	/**
	 * This is the positive test case for equals
	 */
	@Test
	public void testEqualsPositive() throws Exception{
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		InternalBaseExtender copy = (InternalBaseExtender)Base.clone(internalBaseExtender);
		if(internalBaseExtender.equals(copy) ==false){
			Assert.fail("The objects were expected to be equal");
		}
	}

	/**
	 * This is the positive test case for equals
	 */
	@Test
	public void testEqualsPositiveSameObject() throws Exception{
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		InternalBaseExtender copy = (InternalBaseExtender)Base.clone(internalBaseExtender);
		if(internalBaseExtender.equals(internalBaseExtender) ==false){
			Assert.fail("The objects were expected to be equal");
		}
	}
	
	@Test
	public void testEqualsNegetiveDifferentClass() throws Exception{
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		Integer copy = new Integer(33);
		if(internalBaseExtender.equals(copy) ==true){
			Assert.fail("The objects were expected to be unequal");
		}
	}	
	/**
	 * This is a negetive test case for equals
	 */
	@Test
	public void testEqualsNegetiveWithId() throws Exception{
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		InternalBaseExtender copy = (InternalBaseExtender)Base.clone(internalBaseExtender);
		copy.setId(UUID.randomUUID().toString());
		if(internalBaseExtender.equals(copy) ==true){
			Assert.fail("The objects were expected to be unequal");
		}
	}	
	
	/**
	 * This is a negetive test case for equals
	 */
	@Test
	public void testEqualsNegetiveWithDate() throws Exception{
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		InternalBaseExtender copy = (InternalBaseExtender)Base.clone(internalBaseExtender);
		Date newDate = new Date();
		newDate.setYear(newDate.getYear()+3);
		copy.setCreationDate(newDate);
		if(internalBaseExtender.equals(copy) ==true){
			Assert.fail("The objects were expected to be unequal");
		}
	}
	
	/**
	 * This is used to test hash code for an object
	 */
	@Test
	public void testHashCode(){
		InternalBaseExtender internalBaseExtender = new InternalBaseExtender();
		internalBaseExtender.setSampleField(UUID.randomUUID().toString());
		internalBaseExtender.setId(UUID.randomUUID().toString());
		internalBaseExtender.setCreationDate(new Date());
		internalBaseExtender.setUpdationDate(new Date());
		if(!(internalBaseExtender.hashCode() >= Integer.MIN_VALUE && internalBaseExtender.hashCode() <= Integer.MAX_VALUE)){
			Assert.fail("Not a valid integer");
		}
	}
}
