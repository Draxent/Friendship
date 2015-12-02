/*
 *	@file FriendMapper.java
 *  @author Federico Conte (draxent)
 *  
 *  https://github.com/Draxent/Friendship
 *  The MIT License (MIT)
 *  Copyright (c) 2015 Federico Conte
 *  http://opensource.org/licenses/MIT
 */

package pad;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

// For each line of input, generates user pairs that have common friend, or are already friends.
public class FriendMapper extends Mapper<LongWritable, Text, IntWritable, FriendNumFriendTuple> 
{
	private static final int MEMORY_THRESHOLD = 80;
	private IntWritable userID;
	private FriendNumFriendTuple friend;
	private Map<FriendsTuple, Integer> counter;

	public void setup ( Context context )
	{
		userID = new IntWritable();
		friend = new FriendNumFriendTuple();
		counter = new HashMap<FriendsTuple, Integer>();
	}

	public void map( LongWritable _, Text value, Context context ) throws IOException, InterruptedException 
	{
		// Read line.
		String line = value.toString();
		// Split the line on the tab character.
		String userID_friendList[] = line.split( "\t" );
		
		// Extract the user.
		Integer user = Integer.parseInt( userID_friendList[0] );
		
		// If FriendList is not present.
		if( userID_friendList.length == 1 )
		{
			userID.set( user );
			// Default value of friend is -> (-1, -1) i.e userID has no friends.
			context.write( userID, friend );
			return;
		}
		
		// Split by "," to find the list of friends of userID.
		String friends[] = userID_friendList[1].split( "," );
		int length = friends.length;
		// Convert friends list in Integer type.
		Integer friendList[] = new Integer[length];
		for ( int i = 0; i < length; i++ )
			friendList[i] = Integer.parseInt( friends[i] );
		
		// Insert in the counter the friends of userID (0 means already friends).
		for ( int i = 0; i < length; i++ )
			counter.put( new FriendsTuple(user, friendList[i]), 0 );
		
		// For every pair combination of friendList, insert in the counter the friends suggestion for userID.
		for ( int i = 0; i < length; i++ )
		{
			for ( int j = 0; j < length; j++ )
			{
				if ( j != i )
				{
					FriendsTuple ft = new FriendsTuple( friendList[i], friendList[j] ); 
					Integer numFriends = counter.get( ft );
					
					if ( numFriends == null ) counter.put( ft, 1 );
					// If they are already friends, keep this information.
					else if ( numFriends == -1 ) counter.put( ft, -1 );
					else counter.put( ft, numFriends + 1 );
				}
			}
		}
		
		// If the memory is starting to become a concern, 
		// free it after have printed the partial result obtained until now.
		final long totalMemory = Runtime.getRuntime().totalMemory();
		final long freeMemory = Runtime.getRuntime().freeMemory();
		final float percMemUsed = ((float) (totalMemory - freeMemory) / totalMemory) * 100;
		if ( percMemUsed > MEMORY_THRESHOLD )
		{
			printResults( context );
			counter.clear();
		}
	}
	
	public void cleanup( Context context ) throws IOException, InterruptedException
	{
		printResults( context );
	}
	
	public void printResults( Context context ) throws IOException, InterruptedException
	{
		for ( FriendsTuple ft : counter.keySet() )
		{
			userID.set( ft.UserID );
			friend.FriendID = ft.FriendID;
			friend.NumFriends = counter.get( ft );
			context.write( userID, friend );
		}			
	}
}