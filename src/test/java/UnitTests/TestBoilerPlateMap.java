package UnitTests;

import static org.junit.Assert.*;

import java.util.HashMap;

import junit.framework.Assert;
import net.wimpi.telnetd.io.terminal.ansi;

import org.junit.Test;

import com.boilerplate.java.collections.BoilerplateMap;

/**
 * Tests the BoilerPlateMap
 * @author gaurav
 *
 */
public class TestBoilerPlateMap {
	
	/**
	 * Tests creation from map
	 */
	@Test
	public void testCreationFromMap(){
		HashMap<Integer,String> hashMap = new HashMap<>();
		hashMap.put(1, "A");
		hashMap.put(2, "B");
		hashMap.put(3, "C");
		
		BoilerplateMap<Integer, String> boilerPlateMap = new BoilerplateMap<>(hashMap);
		Assert.assertEquals(hashMap, boilerPlateMap);
	}
	
	/**
	 * Tests to string
	 */
	@Test
	public void testToString(){
		BoilerplateMap<Integer, String> boilerPlateMap = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		boilerPlateMap.put(3, "C");
		String s = boilerPlateMap.toString();
		Assert.assertNotNull(s);
	}
	
	/**
	 * Tests toJSON
	 */
	@Test
	public void testToJSON(){
		BoilerplateMap<Integer, String> boilerPlateMap = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		boilerPlateMap.put(3, "C");
		String s = boilerPlateMap.toJSON();
		Assert.assertNotNull(s);
	}
	
	/**
	 * Tests equals when both maps are same
	 */
	@Test
	public void testEqualsWithSameReference(){
		BoilerplateMap<Integer, String> boilerPlateMap = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		boilerPlateMap.put(3, "C");
		BoilerplateMap<Integer, String> boilerPlateMap1 = boilerPlateMap;
		Assert.assertEquals(boilerPlateMap, boilerPlateMap1);
	}
	
	@Test
	public void testEqualsWithClone(){
		BoilerplateMap<Integer, String> boilerPlateMap = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		boilerPlateMap.put(3, "C");
		BoilerplateMap<Integer, String> boilerPlateMap1 = boilerPlateMap.clone();
		Assert.assertEquals(boilerPlateMap, boilerPlateMap1);
	}

	@Test
	public void testEqualsWithUnequalNumberOfKeysNegetiveCase(){
		BoilerplateMap<Integer, String> boilerPlateMap = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		boilerPlateMap.put(3, "C");
		BoilerplateMap<Integer, String> boilerPlateMap1 = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		if(boilerPlateMap.equals(boilerPlateMap1) ){
			Assert.fail("Maps are not to be equal");
		}
	}
	
	@Test
	public void testEqualsWithMissingKeyNegetiveCase(){
		BoilerplateMap<Integer, String> boilerPlateMap = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		boilerPlateMap.put(3, "C");
		BoilerplateMap<Integer, String> boilerPlateMap1 = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		boilerPlateMap.put(5, "C");
		if(boilerPlateMap.equals(boilerPlateMap1) ){
			Assert.fail("Maps are not to be equal");
		}
	}
	
	@Test
	public void testEqualsWithSameKeyButDifferentValueNegetiveCase(){
		BoilerplateMap<Integer, String> boilerPlateMap = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		boilerPlateMap.put(3, "C");
		BoilerplateMap<Integer, String> boilerPlateMap1 = new BoilerplateMap<>();
		boilerPlateMap.put(1, "A");
		boilerPlateMap.put(2, "B");
		boilerPlateMap.put(3, "E");
		if(boilerPlateMap.equals(boilerPlateMap1) ){
			Assert.fail("Maps are not to be equal");
		}
	}
}
