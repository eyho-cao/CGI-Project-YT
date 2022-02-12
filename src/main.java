import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String id;
		String title;
		ArrayList<String[]> data = retrieveData();
		for(int i = 0; i < data.size(); i++)
		{
			id = data.get(i)[0];
			title = data.get(i)[1];
			System.out.println("[ " +id + " , " +title +" ]");
		}
	}

	//gets data from Youtube api and serializes data(video id and video title)
	public static ArrayList<String[]> retrieveData() throws IOException
	{
		try {
			String yt_api_url = "https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=telecom&key=" +System.getenv("YT_API");
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
			ArrayList<String[]> serializedData = new ArrayList<>();
			for(int i = 0; i < items.length(); i++)
			{
				vidId = items.getJSONObject(i).getJSONObject("id").get("videoId").toString();
				vidTitle = items.getJSONObject(i).getJSONObject("snippet").get("title").toString();
				String[] data = {vidId, vidTitle};
				serializedData.add(data);
			}
			return serializedData;
		} 
		catch(MalformedURLException e)
		{
			System.err.println("Bad URL");
		}
		catch (IOException e)
		{
			System.err.print("IO Error");
		}
		return null;		
	}
	
	
}
