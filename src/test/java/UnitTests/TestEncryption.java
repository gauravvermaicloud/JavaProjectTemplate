package UnitTests;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import com.boilerplate.framework.Encryption;

public class TestEncryption {

	@Test
	public void testGetHashCode(){
		Assert.assertEquals(1,Encryption.getHashCode(1));
		Assert.assertEquals(2987074,Encryption.getHashCode("abcd"));
	}
}
