package statelet.util;

public final class Arrays {

	public static int min( int [] array )
	{
		if( array.length == 0 )
			throw new IllegalArgumentException();
		
		int min = array[0];
		for( int i=0; i<array.length; ++i )
			if( array[i] < min ) 
				min = array[i];
		return min;
	}

	public static int max( int [] array )
	{
		if( array.length == 0 )
			throw new IllegalArgumentException();
		
		int max = array[0];
		for( int i=0; i<array.length; ++i )
			if( array[i] > max) 
				max = array[i];
		return max;
	}

	public static int minIndex( int [] array )
	{
		int j = 0;
		for( int i=1; i<array.length;  ++i )
			if( array[i] < array[j] )
				j = i;
		return j;
	}
	
	public static int maxIndex( int [] array )
	{
		int j = 0;
		for( int i=1; i<array.length;  ++i )
			if( array[i] > array[j] )
				j = i;
		return j;
	}
}

// End ///////////////////////////////////////////////////////////////

