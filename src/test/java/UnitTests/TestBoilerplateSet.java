package UnitTests;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;

import UnitTests.TestBase.InternalBaseExtender;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateSet;
import com.boilerplate.java.entities.BaseEntity;

public class TestBoilerplateSet {

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
	 * Tests the to string method of the set	
	 */
	@Test
	public void testToString(){
		BoilerplateSet<Integer> boilerplateSet = new BoilerplateSet<>();
		for(int i=1;i<3;i++){
			boilerplateSet.add(new Integer(i));			
		}
		String toString = boilerplateSet.toString();
		Assert.assertEquals("[1,2]",toString);
	}
	
	/**
	 * Tests the collection constructor
	 */
	@Test
	public void testCollectionConstructor(){
		InternalBaseExtender internalClassInstance1 =new InternalBaseExtender();
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setCreationDate(new Date());
		internalClassInstance1.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance2 =new InternalBaseExtender();
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setCreationDate(new Date());
		internalClassInstance2.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance3 =new InternalBaseExtender();
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setCreationDate(new Date());
		internalClassInstance3.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance4 =new InternalBaseExtender();
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setCreationDate(new Date());
		internalClassInstance4.setUpdationDate(new Date());
		
		ArrayList<InternalBaseExtender> arrayList = new ArrayList<>();
		arrayList.add(internalClassInstance1);
		arrayList.add(internalClassInstance2);
		arrayList.add(internalClassInstance3);
		arrayList.add(internalClassInstance4);
		
		BoilerplateSet<InternalBaseExtender> boilerplateSet
			= new BoilerplateSet<>(arrayList);
		
		if(!boilerplateSet.containsAll(arrayList)){
			Assert.fail("Set doesnt contain all elements of the list");
		}
	}
	/**
	 * Tests equals when both point to same object
	 */
	@Test
	public void testEqualsPositiveCase(){
		InternalBaseExtender internalClassInstance1 =new InternalBaseExtender();
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setCreationDate(new Date());
		internalClassInstance1.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance2 =new InternalBaseExtender();
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setCreationDate(new Date());
		internalClassInstance2.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance3 =new InternalBaseExtender();
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setCreationDate(new Date());
		internalClassInstance3.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance4 =new InternalBaseExtender();
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setCreationDate(new Date());
		internalClassInstance4.setUpdationDate(new Date());

		
		ArrayList<InternalBaseExtender> arrayList = new ArrayList<>();
		arrayList.add(internalClassInstance1);
		arrayList.add(internalClassInstance2);
		arrayList.add(internalClassInstance3);
		arrayList.add(internalClassInstance4);
		
		BoilerplateSet<InternalBaseExtender> boilerplateSet1
			= new BoilerplateSet<>(arrayList);	
		BoilerplateSet<InternalBaseExtender> boilerplateSet2 = boilerplateSet1;
		
		Assert.assertEquals(boilerplateSet1, boilerplateSet2);
	}


	/**
	 * Tests equals when both point to same object
	 */
	@Test
	public void testEqualsPositiveCaseWhenSameObjectsArePresent(){
		InternalBaseExtender internalClassInstance1 =new InternalBaseExtender();
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setCreationDate(new Date());
		internalClassInstance1.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance2 =new InternalBaseExtender();
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setCreationDate(new Date());
		internalClassInstance2.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance3 =new InternalBaseExtender();
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setCreationDate(new Date());
		internalClassInstance3.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance4 =new InternalBaseExtender();
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setCreationDate(new Date());
		internalClassInstance4.setUpdationDate(new Date());
		
		
		BoilerplateSet<InternalBaseExtender> boilerplateSet1
			= new BoilerplateSet<>();	
		boilerplateSet1.add(internalClassInstance1);
		boilerplateSet1.add(internalClassInstance2);
		boilerplateSet1.add(internalClassInstance3);
		boilerplateSet1.add(internalClassInstance4);
		
		BoilerplateSet<InternalBaseExtender> boilerplateSet2 =
			new BoilerplateSet<>();
		boilerplateSet2.add(internalClassInstance4);
		boilerplateSet2.add(internalClassInstance3);
		boilerplateSet2.add(internalClassInstance2);
		boilerplateSet2.add(internalClassInstance1);
		
		Assert.assertEquals(boilerplateSet1, boilerplateSet2);
	}
	
	/**
	 * Tests the equal method when different kind of objects are present
	 */
	@Test
	public void testEqualsPositiveCaseWhenDifferentObjectsArePresent(){
		InternalBaseExtender internalClassInstance1 =new InternalBaseExtender();
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setCreationDate(new Date());
		internalClassInstance1.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance2 =new InternalBaseExtender();
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setCreationDate(new Date());
		internalClassInstance2.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance3 =new InternalBaseExtender();
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setCreationDate(new Date());
		internalClassInstance3.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance4 =new InternalBaseExtender();
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setCreationDate(new Date());
		internalClassInstance4.setUpdationDate(new Date());


		InternalBaseExtender internalClassInstance5 =new InternalBaseExtender();
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setCreationDate(new Date());
		internalClassInstance1.setUpdationDate(new Date());

		
		BoilerplateSet<InternalBaseExtender> boilerplateSet1
			= new BoilerplateSet<>();	
		boilerplateSet1.add(internalClassInstance1);
		boilerplateSet1.add(internalClassInstance2);
		boilerplateSet1.add(internalClassInstance3);
		boilerplateSet1.add(internalClassInstance4);
		
		BoilerplateSet<InternalBaseExtender> boilerplateSet2 =
			new BoilerplateSet<>();
		boilerplateSet2.add(internalClassInstance4);
		boilerplateSet2.add(internalClassInstance3);
		boilerplateSet2.add(internalClassInstance2);
		boilerplateSet2.add(internalClassInstance5);
		
		Assert.assertNotSame(boilerplateSet1, boilerplateSet2);
	}
		
	/**
	 * Tests equals when different number of objects are present.
	 */
	@Test
	public void testEqualsPositiveCaseWhenDifferentNumberOfObjectsArePresent(){
		InternalBaseExtender internalClassInstance1 =new InternalBaseExtender();
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setCreationDate(new Date());
		internalClassInstance1.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance2 =new InternalBaseExtender();
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setCreationDate(new Date());
		internalClassInstance2.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance3 =new InternalBaseExtender();
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setCreationDate(new Date());
		internalClassInstance3.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance4 =new InternalBaseExtender();
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setCreationDate(new Date());
		internalClassInstance4.setUpdationDate(new Date());


		InternalBaseExtender internalClassInstance5 =new InternalBaseExtender();
		internalClassInstance5.setId(UUID.randomUUID().toString());
		internalClassInstance5.setId(UUID.randomUUID().toString());
		internalClassInstance5.setCreationDate(new Date());
		internalClassInstance5.setUpdationDate(new Date());
		
		BoilerplateSet<InternalBaseExtender> boilerplateSet1
			= new BoilerplateSet<>();	
		boilerplateSet1.add(internalClassInstance1);
		boilerplateSet1.add(internalClassInstance2);
		boilerplateSet1.add(internalClassInstance3);
		boilerplateSet1.add(internalClassInstance4);
		
		BoilerplateSet<InternalBaseExtender> boilerplateSet2 =
			new BoilerplateSet<>();
		boilerplateSet2.add(internalClassInstance4);
		boilerplateSet2.add(internalClassInstance3);
		boilerplateSet2.add(internalClassInstance2);
		boilerplateSet2.add(internalClassInstance5);
		
		Assert.assertNotSame(boilerplateSet1, boilerplateSet2);
	}

	/**
	 * This method tests clone
	 */
	@Test
	public void testClone(){
		InternalBaseExtender internalClassInstance1 =new InternalBaseExtender();
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setId(UUID.randomUUID().toString());
		internalClassInstance1.setCreationDate(new Date());
		internalClassInstance1.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance2 =new InternalBaseExtender();
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setId(UUID.randomUUID().toString());
		internalClassInstance2.setCreationDate(new Date());
		internalClassInstance2.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance3 =new InternalBaseExtender();
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setId(UUID.randomUUID().toString());
		internalClassInstance3.setCreationDate(new Date());
		internalClassInstance3.setUpdationDate(new Date());
		

		InternalBaseExtender internalClassInstance4 =new InternalBaseExtender();
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setId(UUID.randomUUID().toString());
		internalClassInstance4.setCreationDate(new Date());
		internalClassInstance4.setUpdationDate(new Date());

		
		ArrayList<InternalBaseExtender> arrayList = new ArrayList<>();
		arrayList.add(internalClassInstance1);
		arrayList.add(internalClassInstance2);
		arrayList.add(internalClassInstance3);
		arrayList.add(internalClassInstance4);
		
		BoilerplateSet<InternalBaseExtender> boilerplateSet1
			= new BoilerplateSet<>(arrayList);	
		BoilerplateSet<InternalBaseExtender> boilerplateSet2 = boilerplateSet1.clone();
		
		Assert.assertEquals(boilerplateSet1, boilerplateSet2);		
	}
}
