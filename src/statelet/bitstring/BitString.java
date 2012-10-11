/*
 * Copyright (C) Jerry Swan, 2010-2012.
 * 
 * This file is part of Hyperion, a hyper-heuristic solution-domain framework.
 * 
 * Hyperion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Hyperion is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Hyperion. If not, see <http://www.gnu.org/licenses/>.
 *
 */

//////////////////////////////////////////////////////////////////////

package statelet.bitstring;

import java.util.BitSet;
import java.util.Random;

//////////////////////////////////////////////////////////////////////

public final class BitString 
{
	private BitSet impl;
	private final int length;
	
	///////////////////////////////
	
	public static BitString fromInt( int x )
	{
		BitString result = new BitString( 32 );
		for( int i=0; i<32; ++i )
			if( ( x & ( 1 << i ) ) != 0 )
				result.set( i );
		
		return result;
	}

	public static BitString fromLong( long x )
	{
		return fromLong( x, 64 );
	}

	public static BitString fromLong( long x, int numBits )
	{
		BitString result = new BitString( numBits );
		for( int i=0; i<Math.min( 64, numBits ); ++i )
			if( ( x & ( 1L << i ) ) != 0L )
				result.set( i );
		
		return result;
	}
	
	public static BitString fromBinaryString( String x )
	{
		BitString result = new BitString( x.length() );
		for( int i=0; i<x.length(); ++i )
			if( x.charAt( i ) == '1' )
				result.set( i );
			else if( x.charAt( i ) != '0' )
				throw new IllegalArgumentException();
		
		return result;
	}
	
    /**
     * Creates a bit vector large enough to explicitly represent 
     * bits with indices in the range 0 through nbits-1.	
     */
	public BitString( int nbits )
	{
		impl = new BitSet( nbits );
		length = nbits;
	}

	public BitString( int nbits, Random random )
	{
		impl = new BitSet( nbits );
		length = nbits;
		for( int i=0; i<length; ++i )
			impl.set( i, random.nextBoolean() );
	}
	
	public BitString( BitSet x, int fromIndex, int toIndex )
	{
		impl = x.get( fromIndex, toIndex );
		this.length = toIndex - fromIndex;
	}

	/*******
	public BitVector( BigInteger x, int nbits )
	{
		final boolean xNegative = x.compareTo( BigInteger.ZERO ) < 0;
		final int xLength = xNegative ? x.bitLength() + 1 : x.bitLength();
		
		length = nbits;
		impl = new BitSet( length );
		for( int i=0; i<Math.min( xLength, nbits ); ++i )
			if( x.testBit( i ) )
				impl.set( i );
		if( xNegative && nbits > xLength + 1 )
			impl.set( xLength + 1 );			
	}
	*******/
	
	public BitString( BitString rhs )
	{
		impl = rhs.impl.get( 0, rhs.impl.size() - 1 );
		length = rhs.length;
	}
	
	/**
	 * 	Performs a logical AND of this target bit vector with the argument bit vector. 
	 */
	public void and( BitString rhs )
	{
		if( length() != rhs.length() )
			throw new IllegalArgumentException();
		
		impl.and( rhs.impl );
	}

	/**
	 * 	Clears all of the bits in this BitVector whose corresponding bit is rhs in the specified BitVector. 
	 */
	public void andNot( BitString rhs ) 
	{
		if( length() != rhs.length() )
			throw new IllegalArgumentException();
		
		impl.andNot( rhs.impl );
	}
	
	/**
	 * Returns the number of bits set to true in this BitVector.
	 */
	public int cardinality() { return impl.cardinality(); }
	

	/**
	 * Sets all of the bits in this BitVector to false.
	 */ 
	public void clear() { impl.clear(); }
	
	/**
	 * Sets the bit specified by the index to false.
	 */
	public void clear(int bitIndex) { impl.clear(bitIndex); }
    
	/**
	 * Sets the bits from the specified fromIndex(inclusive) to the specified toIndex(exclusive) to false. 
	 */
	public void clear(int fromIndex, int toIndex) { impl.clear(fromIndex, toIndex); }
 
	public BitString clone() { return new BitString( this ); } 
    
	public boolean equals( Object obj )
	{
		if( !( obj instanceof BitString ) )
			return false;
		
		BitString rhs = (BitString)obj;
		return length == rhs.length && impl.equals( rhs.impl );
	}

    /**
     * Sets the bit at the specified index to to the complement of its current value.
     */
	public void flip( int bitIndex )
	{
		if( bitIndex < 0 || bitIndex >= length() )
			throw new IllegalArgumentException();
		
		impl.flip(bitIndex);
	}

	/**
	 * Sets each bit from the specified fromIndex(inclusive) to the specified toIndex(exclusive) to the complement of its current value.
	 */
	public void flip(int fromIndex, int toIndex)
	{
		if( fromIndex < 0 || fromIndex >= length() )
			throw new IllegalArgumentException();
		if( toIndex < 0 || toIndex >= length() )
			throw new IllegalArgumentException();
		
		impl.flip(fromIndex, toIndex);
	}
    
	/**
	 * Returns the value of the bit with the specified index. 
	 */
	public boolean get(int bitIndex)
	{
		if( bitIndex < 0 || bitIndex >= length() )
			throw new IllegalArgumentException();
		
		return impl.get( bitIndex );
	}
    
	public int hashCode() { return impl.hashCode();	}

    /**
     * Returns true if the specified BitVector has any bits rhs to true that are also rhs to true in this BitVector.
     */
	public boolean intersects(BitString rhs)
	{
		if( length() != rhs.length() )
			throw new IllegalArgumentException();
		
		return impl.intersects( rhs.impl );
	}
 
	/**
	 * Returns true if this BitVector contains no bits that are rhs to true.
	 */
	public boolean isEmpty() { return impl.isEmpty(); }
    
	/**
	 * Returns the length of this BitVector.
	 * Note that this is not the same as BitSet.length, which
	 * returns the logical size, i.e. the  
	 */
	public int length() { return length; } 
 
	/**
	 * Returns the index of the first bit that is rhs to false that occurs on or after the specified starting index. 
	 */
	public int nextClearBit(int fromIndex)
	{
		if( fromIndex < 0 || fromIndex >= length() )
			throw new IllegalArgumentException();
		
		int result = impl.nextClearBit( fromIndex );
		if( result == length )
			result = -1;
		return result;
	}
 
	/**
	 * Returns the index of the first bit that is rhs to true that occurs on or after the specified starting index. 
	 */
	public int nextSetBit(int fromIndex)
	{
		if( fromIndex < 0 || fromIndex >= length() )
			throw new IllegalArgumentException();
		
		return impl.nextSetBit( fromIndex );		
	}

	/**
	 * 	Performs a logical NOT of this bit vector 
	 */
	public void not()
	{
		BitSet allOnes = new BitSet( impl.size() );
		allOnes.flip( 0, allOnes.size() );
		impl.xor( allOnes );
	}
	
	/**
	 * Performs a logical OR of this bit vector with the bit vector argument. 
	 */
	public void or(BitString rhs)
	{
		if( length() != rhs.length() )
			throw new IllegalArgumentException();
		
		impl.or( rhs.impl );
	}
    
	/**
	 * Sets the bit at the specified index to true.
	 */
	public void set( int bitIndex )
	{
		if( bitIndex < 0 || bitIndex >= length() )
			throw new IllegalArgumentException();

		impl.set( bitIndex );
	}
    
	/**
	 * Sets the bit at the specified index to the specified value. 
	 */
	public void set(int bitIndex, boolean value)
	{
		if( bitIndex < 0 || bitIndex >= length() )
			throw new IllegalArgumentException();

		impl.set( bitIndex, value );
	}

	/**
     * Sets the bits from the specified fromIndex(inclusive) to the specified toIndex(exclusive) to true.
     */
	public void set(int fromIndex, int toIndex)
	{
		if( fromIndex < 0 || fromIndex >= length() )
			throw new IllegalArgumentException();
		if( toIndex < 0 || toIndex >= length() )
			throw new IllegalArgumentException();

		impl.set( fromIndex, toIndex );
	}

	/**
	 * Sets the bits from the specified fromIndex(inclusive) to the specified toIndex(exclusive) to the specified value. 
	 */
	public void set(int fromIndex, int toIndex, boolean value)
	{
		if( fromIndex < 0 || fromIndex >= length() )
			throw new IllegalArgumentException();
		if( toIndex < 0 || toIndex >= length() )
			throw new IllegalArgumentException();

		impl.set( fromIndex, toIndex, value );
	}

	public BitString subVector( int fromIndex, int toIndex )
	{
		return new BitString( impl, fromIndex, toIndex );
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		for( int i=0; i<length(); ++i )
			result.append( impl.get( length() - i - 1 ) ? "1" : "0" );
		
		return result.toString();
	}
    
	/**
	 * Performs a logical XOR of this bit set with the bit set argument. 
	 */
	public void xor(BitString rhs)
	{
		if( length() != rhs.length() )
			throw new IllegalArgumentException();
		
		impl.xor( rhs.impl );
	}
	
	public static int HammingDistance( BitString a, BitString b )
	{
		if( a.length() != b.length() )
			throw new IllegalArgumentException();
		
		BitSet aa = b.impl.get( 0, b.impl.size() - 1 );
		aa.xor( b.impl );
		return aa.cardinality();
	}
}

// End ///////////////////////////////////////////////////////////////
