/*
 *	@file FriendsTuple.java
 *  @author Federico Conte (draxent)
 *  
 *  https://github.com/Draxent/Friendship
 *  The MIT License (MIT)
 *  Copyright (c) 2015 Federico Conte
 *  http://opensource.org/licenses/MIT
 */

package pad;

public class FriendsTuple
{
	public final Integer UserID;
	public final Integer FriendID;
	
    public FriendsTuple( Integer userID, Integer friendID )
    {
    	this.UserID = userID;
    	this.FriendID = friendID;
    }	

    public int hashCode()
    {
    	int hash1 = (this.UserID != null) ? this.UserID.hashCode() : 0;
    	int hash2 = (this.FriendID != null) ? this.FriendID.hashCode() : 0;
    	return (hash1 + hash2) * hash2 + hash1;
    }
    
    public boolean equals( Object other )
    {
    	if ( this == other ) return true;
    	if ( !(other instanceof FriendsTuple) ) return false;
    	
    	FriendsTuple ft = (FriendsTuple) other;
		boolean cond1 = ( this.UserID != null && ft.UserID != null && this.UserID.equals(ft.UserID) );
		boolean cond2 = ( this.FriendID != null && ft.FriendID != null && this.FriendID.equals(ft.FriendID) ); 
		return cond1 && cond2;
    }

    public String toString()
    { 
           return "(" + this.UserID.toString() + ", " + this.FriendID.toString() + ")"; 
    }
}