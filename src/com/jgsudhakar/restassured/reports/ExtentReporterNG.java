package com.jgsudhakar.restassured.reports;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

//import com.relevantcodes.extentreports.ExtentReports;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;

/**
 * @Author : Sudhakar Tangellapalli
 * @File : com.jgsudhakar.restassured.reports.ExtentReporterNG
 * @Date : 16/07/2020
 */
public class ExtentReporterNG implements IReporter {
	/*
	 * 
	 * 
	 * public static Map<String, String> requestLoggerMap = new HashMap<>(); public
	 * static Map<String, String> responseLoggerMap = new HashMap<>(); public static
	 * Map<String, String> errorLoggerMap = new HashMap<>();
	 * 
	 * private ExtentReports extent;
	 * 
	 * @Override public void generateReport(List<XmlSuite> xmlSuites, List<ISuite>
	 * suites, String outputDirectory) { extent = new ExtentReports(outputDirectory
	 * + File.separator + "ExtentReportTestNG.html", true);
	 * 
	 * for (ISuite suite : suites) { Map<String, ISuiteResult> result =
	 * suite.getResults();
	 * 
	 * for (ISuiteResult r : result.values()) { ITestContext context =
	 * r.getTestContext();
	 * 
	 * buildTestNodes(context.getPassedTests(), LogStatus.PASS);
	 * buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
	 * buildTestNodes(context.getSkippedTests(), LogStatus.SKIP); } }
	 * extent.assignProject("Mobeix Admin Project");
	 * extent.config().reportName("Mobeix"); extent.flush(); extent.close(); }
	 * 
	 *//**
		 * This method will be used to generate extent report. This will be always
		 * listening to all the test Cases Running in the system. This will be
		 * calculating the starting time and ending Time. Here we are grouping the Test
		 * cases.
		 */
	/*
	 * private void buildTestNodes(IResultMap tests, LogStatus status) { ExtentTest
	 * test;
	 * 
	 * if (tests.size() > 0) { for (ITestResult result : tests.getAllResults()) {
	 * 
	 * String method = result.getMethod().getMethodName();
	 * 
	 * test = extent.startTest(result.getMethod().getDescription());
	 * 
	 * test.getTest().setStartedTime(getTime(result.getStartMillis()));
	 * test.getTest().setEndedTime(getTime(result.getEndMillis())); for (String
	 * group : result.getMethod().getGroups()) { test.assignCategory(group);
	 * test.setDescription(result.getName()); }
	 * 
	 * String message = "Test " + status.toString().toLowerCase() + "ed"; if
	 * (result.getThrowable() != null) message = result.getThrowable().getMessage();
	 * 
	 * test.log(status, message); if(null != requestLoggerMap.get(method)) { String
	 * request = requestLoggerMap.get(method); test.log(LogStatus.INFO, request); }
	 * 
	 * if(null != responseLoggerMap.get(method)) { String response =
	 * responseLoggerMap.get(method); test.log(LogStatus.INFO,
	 * (StringEscapeUtils.escapeJava(response)).replace("\r", "").replace("n ",
	 * "").replace("\\", "")); }
	 * 
	 * 
	 * extent.endTest(test); } } }
	 * 
	 *//**
		 * This method will return the Current Date with Time
		 */
	/*
	 * private Date getTime(long millis) { Calendar calendar =
	 * Calendar.getInstance(); calendar.setTimeInMillis(millis); return
	 * calendar.getTime(); }
	 */}
