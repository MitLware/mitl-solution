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

package statelet.bitvector;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Consumer;

//////////////////////////////////////////////////////////////////////

public final class BitVector 
{
	private BitSet impl;
	private final int length;
	
	///////////////////////////////
	
	public static BitVector fromInt( int x )
	{
		BitVector result = new BitVector( 32 );
		for( int i=0; i<32; ++i )
			if( ( x & ( 1 << i ) ) != 0 )
				result.set( i );
		
		return result;
	}

	public static BitVector fromLong( long x )
	{
		return fromLong( x, 64 );
	}

	public static BitVector fromLong( long x, int numBits )
	{
		BitVector result = new BitVector( numBits );
		for( int i=0; i<Math.min( 64, numBits ); ++i )
			if( ( x & ( 1L << i ) ) != 0L )
				result.set( i );
		
		return result;
	}
	
	public static BitVector fromBinaryString( String x )
	{
		BitVector result = new BitVector( x.length() );
		for( int i=0; i<x.length(); ++i )
			if( x.charAt( i ) == '1' )
				result.set( x.length() - i - 1 );
			else if( x.charAt( i ) != '0' )
				throw new IllegalArgumentException();
		
		return result;
	}

	///////////////////////////////

	/*
	 * From http://stackoverflow.com/questions/29526985/java-from-biginteger-to-bitset-and-back 
	 * BigInteger is big-endian andBitSet is little-endian, so the bytes will be reversed when 
	 * trying to convert directly from one to the other via toByteArray(). 
	 * The simplest solution is to just reverse them again. 
	 */
	
	public BitVector( int numBits, BigInteger x ) {
	
		length = numBits;
		
		byte [] sourceArray = x.toByteArray();		
		if( numBits > sourceArray.length * 8 )
			throw new IllegalArgumentException();
		
		impl = BitSet.valueOf(reverse( sourceArray ));
	}
	
	public BigInteger toBigInteger() {
		return new BigInteger(1, reverse(impl.toByteArray() ) );		
	}

	private static byte[] reverse(byte[] bytes) {
	    for(int i = 0; i < bytes.length/2; i++) {
	        byte temp = bytes[i];
	        bytes[i] = bytes[bytes.length-i-1];
	        bytes[bytes.length-i-1] = temp;
	    }
	    return bytes;
	}	
	
	///////////////////////////////	
		
    /**
     * Creates a bit vector large enough to explicitly represent 
     * bits with indices in the range 0 through nbits-1.	
     */
	public BitVector( int nbits )
	{
		impl = new BitSet( nbits );
		length = nbits;
	}

	public BitVector( int nbits, Random random )
	{
		impl = new BitSet( nbits );
		length = nbits;
		for( int i=0; i<length; ++i )
			impl.set( i, random.nextBoolean() );
	}
	
	public BitVector( BitSet x, int fromIndex, int toIndex )
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
	
	public BitVector( BitVector rhs )
	{
		impl = (BitSet)rhs.impl.clone();
		length = rhs.length;
	}
	
	///////////////////////////////
	
	/**
	 * 	Performs a logical AND of this target bit vector with the argument bit vector. 
	 */
	public void and( BitVector rhs )
	{
		if( length() != rhs.length() )
			throw new IllegalArgumentException();
		
		impl.and( rhs.impl );
	}

	/**
	 * 	Clears all of the bits in this BitVector whose corresponding bit is rhs in the specified BitVector. 
	 */
	public void andNot( BitVector rhs ) 
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
	
	///////////////////////////////
 
	private static final class SparseIterator 
	implements Iterator< Integer > 
	{
	    private final BitSet bitset_;
	    private int index_ = -1;

	    ///////////////////////////////
	    
	    public SparseIterator( BitSet bitset ) 
	    {
	        bitset_ = bitset;
	        index_ = bitset.nextSetBit( 0 ); 
	    }

	    public boolean hasNext() 
	    {
	    	return index_ != -1;
	    }

	    public Integer next() 
	    {
	        if( !hasNext() )
	            throw new NoSuchElementException();
	        
	        int result = index_;
	        index_ = bitset_.nextSetBit( ++index_ );
	        return result;
	    }

	    public void remove() {
	        throw new UnsupportedOperationException();
	    }

		@Override
		public void forEachRemaining(Consumer<? super Integer> action) {
			while( hasNext() )
				action.accept(next());	    
		}
	}
	
	///////////////////////////////
	
	public Iterator< Integer > sparseIterator() {
		return new SparseIterator( impl );
	}
	
	///////////////////////////////
	
	public boolean invariant() {
		return impl.length() <= length() && impl.size() >= length();
	}
	
	///////////////////////////////	
	
	public BitVector clone() { return new BitVector( this ); } 
    
	public boolean equals( Object obj )
	{
		if( !( obj instanceof BitVector ) )
			return false;
		
		BitVector rhs = (BitVector)obj;
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
		if( toIndex <= 0 || toIndex > length() )
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
	public boolean intersects(BitVector rhs)
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
		if( fromIndex < 0) // || fromIndex >= length() )
			throw new IllegalArgumentException();
		
		// the contract at http://docs.oracle.com/javase/7/docs/api/java/util/BitSet.html#nextSetBit(int) requires 
		// this behaviour rather than the original throwing of an exception above
		if (fromIndex >= length())
			return -1;
		
		return impl.nextSetBit( fromIndex );		
	}

	/**
	 * 	Performs a logical NOT of this bit vector 
	 */
	public void not()
	{
		BitSet allOnes = new BitSet( length );
		allOnes.flip( 0, length );
		impl.xor( allOnes );
	}
	
	/**
	 * Performs a logical OR of this bit vector with the bit vector argument. 
	 */
	public void or(BitVector rhs)
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
		if( toIndex <= 0 || toIndex > length() )
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
		if( toIndex <= 0 || toIndex > length() )
			throw new IllegalArgumentException();

		impl.set( fromIndex, toIndex, value );
	}

	public BitVector subVector( int fromIndex, int toIndex )
	{
		return new BitVector( impl, fromIndex, toIndex );
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
	public void xor(BitVector rhs)
	{
		if( length() != rhs.length() )
			throw new IllegalArgumentException();
		
		impl.xor( rhs.impl );
	}
	
	public static int HammingDistance( BitVector a, BitVector b )
	{
		if( a.length() != b.length() )
			throw new IllegalArgumentException();
		
		BitSet aa = (BitSet)a.impl.clone();
		aa.xor( b.impl );
		return aa.cardinality();
	}
}

// End ///////////////////////////////////////////////////////////////
