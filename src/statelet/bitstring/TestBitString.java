package statelet.bitstring;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestBitString {

	@Test
	public void testEquals() {
		BitString a = new BitString( 32 );
		BitString b = BitString.fromInt( 0 );
		assertEquals( a, b );
	}
	
	@Test
	public void testEqualsDisjoint() {
		BitString a = new BitString( 32 );
		BitString b = new BitString( a );
		assertEquals( a, b );
		a.set( 0 );
		assertNotEquals( a, b );		
		assertEquals( a, BitString.fromInt( 1 ) );
		assertNotEquals( b, BitString.fromInt( 1 ) );		
	}
	
}

// End ///////////////////////////////////////////////////////////////
