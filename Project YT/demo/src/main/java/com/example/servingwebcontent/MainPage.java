package com.example.servingwebcontent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@Controller
public class MainPage {
	public String pageLog = "";
	
	//Receive and "consume" data from queueA then edit video title and send data to queueB
	@JmsListener(destination = "queueA")
	public void receiveMessage(String data)
	{
		try
		{
		VideoData video = xmlToObj(data);
		pageLog +="Receieved Video Date from Queue A:\n" +data +"\n------------\n";
		VideoData editedData = replaceText(video);
		ServingWebContentApplication.sendMessage(editedData, "QueueB");
		pageLog += "\nSending a message to: queueB \n"; //sendMessage doesnt return anything, ServingWebContentApplication is a static class and so the log data cannot be retrieved for that specific instance without more additions to the class
		pageLog += "------------\n";
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	@GetMapping("/mainPage")
	public String mainPage(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) throws InterruptedException {
		pageLog += ServingWebContentApplication.run();
		Thread.sleep(2000);//wait for all messages sent to be recieved
		model.addAttribute("log", pageLog);
		System.out.println(pageLog);
		return "mainPage";
	}

	/*
	public String runProjYT()
	{
		//ServingWebContentApplication retrieve = new ServingWebContentApplication();
		pageLog.append(ServingWebContentApplication.run());
		return "mainPage";
	}
	*/
	
	
	//Converts XML string to VideoData object
	public VideoData xmlToObj(String xml) throws JAXBException, XMLStreamException, UnsupportedEncodingException
	{
		InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		JAXBContext jaxbContext = JAXBContext.newInstance(VideoData.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader source = factory.createXMLEventReader(stream);
		JAXBElement<VideoData> element = jaxbUnmarshaller.unmarshal(source, VideoData.class);
		VideoData video = element.getValue();
		return video;
	}
	
	//changes text from "telecom" to "teleco"
	public VideoData replaceText(VideoData data)
	{
		String title = data.getVideoTitle();
		String temp = title.replaceAll("(?i)telecom", "teleco");
		data.setVideoTitle(temp);
		pageLog +=String.format("Edited Video Title: %s", data.getVideoTitle());
		return data;
	}
	
	public String getPageLog()
	{
		return pageLog.toString();
	}
	
	
}