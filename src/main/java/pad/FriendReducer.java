/*
 *	@file FriendReducer.java
 *  @author Federico Conte (draxent)
 *  
 *  https://github.com/Draxent/Friendship
 *  The MIT License (MIT)
 *  Copyright (c) 2015 Federico Conte
 *  http://opensource.org/licenses/MIT
 */

package pad;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

// For each user, find the 10 IDs that have most common friends with the user.
public class FriendReducer extends Reducer<IntWritable, FriendNumFriendTuple, IntWritable, Text> 
{
	private static final int MAX_FRIENDS_SUGGESTED = 10;
	private static final Text EMPTY = new Text( "" );
	private static final List<Integer> MINUS_ONE = Arrays.asList( -1 );
	
	// Local counter for a single userID.
	private Map<Integer, Integer> counter = new HashMap<Integer, Integer>();
	// Necessary structure to order the map.
	private NumFriendsComparator nfc = new NumFriendsComparator( counter );
	// Values can no longer be retrieved by their key, i.e. get always returns null.
	private TreeMap<Integer, Integer> sorted_counter = new TreeMap<Integer, Integer>( nfc );
	// Used to build the recommended list of friends
	private StringBuilder recommendedList = new StringBuilder();
	private Text result = new Text();
	
	public void reduce( IntWritable userID, Iterable<FriendNumFriendTuple> values, Context context ) throws IOException, InterruptedException 
	{
		// Clear all the local data structure.
		counter.clear();
		sorted_counter.clear();
		recommendedList.setLength( 0 );

		// Add all the suggested friends found for userID into the local counter
		for ( FriendNumFriendTuple val : values )
		{
			// If userID has no friends
			if ( val.FriendID == -1 )
			{
				context.write( userID, EMPTY );
				return;
			}
				
			Integer numFriends = counter.get( val.FriendID );
			
			if ( numFriends == null ) counter.put( val.FriendID, val.NumFriends );
			// If they are already friends, keep this information.
			else if ( numFriends == -1 ) counter.put( val.FriendID, -1 );
			else counter.put( val.FriendID, numFriends + val.NumFriends );
		}

		// Remove all minus one values, we don't want to suggest them
		counter.values().removeAll( MINUS_ONE );
		// Sort the suggested friends in decreasing order based on the associated number
		sorted_counter.putAll( counter );
		
		// Build the recommended list string
		int num_result = 0;
		for ( Integer f : sorted_counter.keySet() )
		{
			if ( num_result >= MAX_FRIENDS_SUGGESTED ) break;
			if ( num_result > 0)
				recommendedList.append( ',' );
			
			recommendedList.append( f );
			num_result++;
		}
		
		// Print the result
		result.set( recommendedList.toString() );
		context.write( userID, result );
	}
}
