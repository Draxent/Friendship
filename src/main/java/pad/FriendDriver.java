/*
 *	@file FriendDriver.java
 *  @author Federico Conte (draxent)
 *  
 *  https://github.com/Draxent/Friendship
 *  The MIT License (MIT)
 *  Copyright (c) 2015 Federico Conte
 *  http://opensource.org/licenses/MIT
 */

package pad;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class FriendDriver
{
	public static void main(String[] args) throws Exception 
	{
		Configuration conf = new Configuration();
		String otherArgs[] = new GenericOptionsParser( conf, args ).getRemainingArgs();
		
		if ( otherArgs.length != 2 )
		{
			System.out.println( "Usage: FriendDriver <graph_input> <output>" );
			System.exit(1);
		}
		
		Path graphInput = new Path( otherArgs[0] );
		Path outputDir = new Path( otherArgs[1] );

		Job job = new Job( conf, "Friendship" );
		job.setJarByClass( FriendDriver.class );

		job.setMapOutputKeyClass( IntWritable.class );
		job.setMapOutputValueClass( FriendNumFriendTuple.class );
		job.setOutputKeyClass( IntWritable.class );
		job.setOutputValueClass( Text.class );

		job.setMapperClass( FriendMapper.class );
		job.setReducerClass( FriendReducer.class );

		job.setInputFormatClass( TextInputFormat.class );
		job.setOutputFormatClass( TextOutputFormat.class );

		FileInputFormat.addInputPath( job, graphInput );
		FileOutputFormat.setOutputPath( job, outputDir );

		System.exit( job.waitForCompletion(true) ? 0 : 1 );
	}
}
