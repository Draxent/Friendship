/*
 *	@file FriendNumFriendTuple.java
 *  @author Federico Conte (draxent)
 *  
 *  https://github.com/Draxent/Friendship
 *  The MIT License (MIT)
 *  Copyright (c) 2015 Federico Conte
 *  http://opensource.org/licenses/MIT
 */

package pad;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class FriendNumFriendTuple implements Writable
{
	// minus one indicates that the userID has no friends
	public int FriendID = -1;
	// minus one indicates that the userID and this FriendID are already friends
	public int NumFriends = -1;
	
	public void readFields( DataInput in ) throws IOException
	{
		this.FriendID = in.readInt();
		this.NumFriends = in.readInt();
	}
	
	public void write( DataOutput out ) throws IOException
	{
		out.writeInt( this.FriendID );
		out.writeInt( this.NumFriends );	
	}
	
	public String toString()
	{
		return this.FriendID + "\t" + this.NumFriends;
	}
}
