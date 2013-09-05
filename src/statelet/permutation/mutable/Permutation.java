package statelet.permutation.mutable;

import java.util.BitSet;
import java.util.Random;

import statelet.permutation.core.ArrayForm;

//////////////////////////////////////////////////////////////////////

public final class Permutation {

	private ArrayForm perm_;
	
	///////////////////////////////
	
	public Permutation( int length )
	{
		perm_ = new ArrayForm( length );
		assert( invariant() );
	}

	public Permutation( int length, Random random )
	{
		perm_ = new ArrayForm( length, random );
		assert( invariant() );		
	}

	public Permutation( int ... perm )
	{
		perm_ = new ArrayForm( perm );
		assert( invariant() );
	}
	
	public Permutation( Permutation o )
	{
		perm_ = o.perm_.clone();
		assert( invariant() );		
	}

	private Permutation( ArrayForm o )
	{
		perm_ = o.clone();
		assert( invariant() );		
	}
	
	///////////////////////////////
	
	public void multiplyBy( Permutation other )
	{
		perm_.multiply( other.perm_ );
	}

    public void conjugateBy( Permutation other )
    {
		perm_.conjugate( other.perm_ );		
    }

    public void divideBy( Permutation other )
    {
		perm_.divide( other.perm_ );		
    }

    public void add( Permutation other )
    {
		perm_.add( other.perm_ );		
    }

    public void subtract( Permutation other )
    {
		perm_.subtract( other.perm_ );		
    }
    
	///////////////////////////////	
	
	public static Permutation 
	Multiply( Permutation p1, Permutation p2 )
	{
		Permutation result = new Permutation( p1.perm_ ); 		
		result.perm_.multiply( p2.perm_ ); 
		return result;
	}

	///////////////////////////////
	
	public static Permutation 
	Divide( Permutation p1, Permutation p2 )
	{
		Permutation result = new Permutation( p1.perm_ ); 		
		result.perm_.divide( p2.perm_ ); 
		return result;
	}

	///////////////////////////////
	
	public static Permutation 
	Mod( Permutation p1, Permutation p2 )
	{
		Permutation result = new Permutation( p1.perm_ ); 		
		result.perm_.mod( p2.perm_ ); 
		return result;
	}
	
	///////////////////////////////
	
	public static Permutation 
	Invert( Permutation p )
	{
		Permutation result = new Permutation( p.perm_ ); 		
		result.perm_.invert(); 
		return result;
	}
	
	///////////////////////////////
	
	public static Permutation 
	Add( Permutation p1, Permutation p2 )
	{
		Permutation result = new Permutation( p1.perm_ ); 		
		result.perm_.add( p2.perm_ ); 
		return result;
	}

	///////////////////////////////
	
	public static Permutation	
	Subtract( Permutation p1, Permutation p2 )
	{
		Permutation result = new Permutation( p1.perm_ ); 		
		result.perm_.subtract( p2.perm_ ); 
		return result;
	}
	
	///////////////////////////////
	
    public static Permutation 
    Conjugate( Permutation p1, Permutation p2 )
    {
		Permutation result = new Permutation( p1.perm_ ); 		
		result.perm_.conjugate( p2.perm_ ); 
		return result;
    }

    ///////////////////////////////
    
    public static Permutation 
    Commutator( Permutation p1, Permutation p2 )
    {
		Permutation result = new Permutation( p1.perm_ ); 		
		result.perm_.commutatorWith( p2.perm_ ); 
		return result;
    }

    ///////////////////////////////
    
    public static Permutation
    Power( Permutation a, int n )
    {
		Permutation result = new Permutation( a ); 		
		result.perm_.power( n ); 
		return result;
    }
	
	///////////////////////////////
	
	public int size() { return perm_.size(); } 

	///////////////////////////////

	public int [] toArray() {
		return perm_.toArray();
	}
	
	///////////////////////////////
	
	public BitSet getAscents() {
		return perm_.getAscents();
	}

	/**
	 * The inversion vector consists of elements whose value
	 * indicates the number of elements in the permutation
	 * that are lesser than it and lie on its right hand side.
        Examples
        ========

        >>> from sympy.combinatorics.permutations import Permutation
        >>> p = Permutation([4,8,0,7,1,5,3,6,2])
        >>> p.inversion_vector()
        [4, 7, 0, 5, 0, 2, 1, 1]
        >>> p = Permutation([3,2,1,0])
        >>> p.inversion_vector()
        [3, 2, 1]
        """
	 * 
	 */
	
	/***
	public int [] inversionVector() {
        
        int [] result = new int [ size() - 1 ];

        for( int i=0; i<size() - 1; ++i )
        {
            int val = 0;
            for( int j=i+1; j<size(); ++j )
                if( perm_[j] < perm_[i] )
                    ++val;
            result[i] = val;
        }
        
        return result;
	}
	***/
	
	///////////////////////////////
	
	@Override
	public Permutation clone() { return new Permutation( this ); }
	
	@Override	
	public int hashCode() { return perm_.hashCode(); }
	
	@Override	
	public boolean equals( Object o )
	{
		if( !( o instanceof Permutation ) )
			return false;
		Permutation rhs = (Permutation)o;
		return perm_.equals( rhs.perm_ );
	}

	public String toString()
	{
		return perm_.toString();
	}
	
	///////////////////////////////
	
	public boolean invariant()
	{
		return perm_.invariant();
	}
	
	///////////////////////////////

	public static boolean isPermutation( int ... perm ) {
		
		BitSet check = new BitSet( perm.length );
		for( int i=0; i<perm.length; ++i ) {
			if( perm[ i ] < 0 || perm[ i ] >= perm.length )
				return false;
			
			check.set( perm[ i ] );
		}
		return check.cardinality() == perm.length;
	}
}

// End ///////////////////////////////////////////////////////////////

