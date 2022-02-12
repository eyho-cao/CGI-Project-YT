package com.example.servingwebcontent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="video")
@XmlAccessorType(XmlAccessType.FIELD)
public class VideoData{
	
	@XmlElement
	String videoId;
	
	@XmlElement
	String videoTitle;
	public VideoData()
	{
		
	}
	
	public VideoData(String videoId, String videoTitle)
	{
		this.videoId = videoId;
		this.videoTitle = videoTitle;
	}
	
	public String getVideoId()
	{
		return videoId;
	}
	public String getVideoTitle()
	{
		return videoTitle;
	}
	
	public void setVideoId(String videoId)
	{
		this.videoId = videoId;
	}
	
	public void setVideoTitle(String videoTitle)
	{
		this.videoTitle = videoTitle;
	}
	
	@Override
	public String toString()
	{
		//XML formatted String
		return String.format("<video>\n"
							+ "\t<videoTitle>%s</videoTitle>\n"
							+ "\t<videoId>%s</videoId>\n"
							+ "</video>",
							getVideoTitle(), getVideoId());
		//return String.format("%s : %s", getVideoTitle(), getVideoId());
	}
}
