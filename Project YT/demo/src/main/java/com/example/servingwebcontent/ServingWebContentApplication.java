package com.example.servingwebcontent;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

@SpringBootApplication
@EnableJms
public class ServingWebContentApplication {

  public static String pageLog;
  public static JmsTemplate jmsTemplate;
  public static void main(String[] args) {
    // Launch the application
    ConfigurableApplicationContext context = SpringApplication.run(ServingWebContentApplication.class, args);

    jmsTemplate = context.getBean(JmsTemplate.class);
    pageLog = "";
  }
   
   //run data retrieval function and call for messages to be sent to JMS queue: "queueA"
   public static String run()
   {
	    try {
		    ArrayList<VideoData> data = retrieveData();
		    for(int i = 0; i < data.size(); i++)
		    {
		        sendMessage(data.get(i), "queueA");
		    }
	    }
	    catch(Exception e)
	    {
	    	
	    }
	    return pageLog.toString();
   }
  
   //Sends POJO messages to JMS
  	public static void sendMessage(VideoData data, String dest)
  	{
  		 pageLog += "Sending a message to: " +dest +"\n";
        jmsTemplate.convertAndSend(dest, data.toString());
  	}
  
	//retrieves data from Youtube api and serializes data(video id and video title)
	public static ArrayList<VideoData> retrieveData()
	{
		try {
			//String yt_api_url = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=telecom&key=" +System.getenv("YT_API");
			String yt_api_url = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=telecom&key=AIzaSyChS4Z7rMA9Fx3byMDIZFyjZ1vDCbGA-FU";
			String vidId;
			String vidTitle;
			//Retrieve data from Youtube and create JSON object from result string
			URL yt_url = new URL(yt_api_url);
			URLConnection con = yt_url.openConnection();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder builder = new StringBuilder();
			
			String line = reader.readLine();
			while(line != null)
			{
				builder.append(line +"\n");
				line = reader.readLine();
			}
			reader.close();
			
			JSONObject jsonObj = new JSONObject(builder.toString());
			JSONArray items = jsonObj.getJSONArray("items");
			 //format JSON objects into a 2D array to hold data and return for later parsing
			ArrayList<VideoData> serializedData = new ArrayList<>();
			for(int i = 0; i < items.length(); i++)
			{
				vidId = items.getJSONObject(i).getJSONObject("id").get("videoId").toString();
				vidTitle = items.getJSONObject(i).getJSONObject("snippet").get("title").toString();
				VideoData data = new VideoData(vidId, vidTitle);
				serializedData.add(data);
			}
			return serializedData;
		} 
		catch(MalformedURLException e)
		{
			System.err.println(e.getMessage());
		}
		catch (IOException e)
		{
			System.err.print(e.getMessage());
		}
		return null;		
	}
	
}