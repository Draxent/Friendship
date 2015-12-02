/*
 *	@file NumFriendsComparator.java
 *  @author Federico Conte (draxent)
 *  
 *  https://github.com/Draxent/Friendship
 *  The MIT License (MIT)
 *  Copyright (c) 2015 Federico Conte
 *  http://opensource.org/licenses/MIT
 */

package pad;

import java.util.Comparator;
import java.util.Map;

public class NumFriendsComparator implements Comparator<Integer>
{
    Map<Integer, Integer> base;

    public NumFriendsComparator( Map<Integer, Integer> base )
    {
        this.base = base;
    }

    // Decreasing order
    public int compare( Integer a, Integer b )
    {
        if ( base.get(a) >= base.get(b) ) return -1;
        else return 1;
    }
}