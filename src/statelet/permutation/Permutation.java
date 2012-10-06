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

package statelet.permutation;

import java.util.Set;

//////////////////////////////////////////////////////////////////////

public interface Permutation 
{
	public Set< Integer > preimage();
	
	public int minPreimage();
	public int maxPreimage();
	
	public int image( int point );
}

// End ///////////////////////////////////////////////////////////////
