package org.mitlware.solution.util;

import java.util.AbstractList;
import java.util.List;

public final class Collections {

	public static List< Integer > asList( final int[] is ) {             
		return new AbstractList< Integer >() {
			public Integer get(int i) { return is[i]; }
			  public int size() { return is.length; }
		  };     
	}
	
	public static List< Float > asList( final float [] is ) {             
		return new AbstractList< Float >() {
			public Float get(int i) { return is[i]; }
			  public int size() { return is.length; }
		  };     
	} 	

	public static List< Double > asList( final double [] is ) {             
		return new AbstractList< Double >() {
			public Double get(int i) { return is[i]; }
			  public int size() { return is.length; }
		  };     
	}
	
	///////////////////////////////
	
	public static int [] asArray( List< Integer > l ) {
		int [] result = new int [ l.size() ];
		for( int i=0; i<l.size(); ++i )
			result[ i ] = l.get( i );
		return result;
	}
	
}

// End ///////////////////////////////////////////////////////////////
