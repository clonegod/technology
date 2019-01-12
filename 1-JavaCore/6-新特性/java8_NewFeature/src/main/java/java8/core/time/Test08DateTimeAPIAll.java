package java8.core.time;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

import org.junit.Test;

public class Test08DateTimeAPIAll {

	/**
	 * 日期计算、日期格式化、日期解析
	 * 
	 * 日期 Date 
	 * 时间 Time
	 * 
	 * Date 与 Calendar 的缺点
	 * 	月份从0开始，不符合习惯
	 * 	DateFormat不是线程安全的
	 * 	Date和Calendar类都是可以变的
	 * 
	 * 新的日期时间API
	 * 	LocalDateTime 为人设计
	 *  Instant 	   为机器设计
	 */
	
	// LocalDate、LocalTime、Instant、Duration 以及Period
	@Test
	public void testLocalDate() {
		LocalDate localDate = LocalDate.of(2018, 8, 23);
		LocalDate today = LocalDate.now();
		System.out.println(localDate.getYear() + "-" + localDate.getMonthValue() + "-" +localDate.getDayOfMonth());
		System.out.println(today.getYear() + "-" + today.getMonthValue() + "-" + today.getDayOfMonth());
	}
	
	@Test
	public void testLocalTime() {
		LocalTime localTime = LocalTime.of(23, 59, 59);
		LocalTime now = LocalTime.now(ZoneId.systemDefault());
		System.out.println(localTime.getHour() + ":" + localTime.getMinute() + ":" + localTime.getSecond()); 
		System.out.println(now.getHour() + ":" + now.getMinute() + ":" + now.getSecond()); 
	}
	
	@Test
	public void testParse() {
		LocalDate localDate1 = LocalDate.parse("2018-08-08");
		LocalDate localDate2 = LocalDate.parse("2018-08-08", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		System.out.println(localDate1);
		System.out.println(localDate2);
		
		LocalTime localTime1 = LocalTime.parse("12:12:12"); 
		LocalTime localTime2 = LocalTime.parse("12:12:12", DateTimeFormatter.ofPattern("HH:mm:ss"));
		System.out.println(localTime1);
		System.out.println(localTime2);
		
	}
	
	@Test
	public void testLocalDateTime() {
		LocalDateTime ldt1 = LocalDateTime.now();
		LocalDateTime ldt2 = LocalDateTime.of(2018, 8, 8, 23, 59, 59);
		System.out.println(ldt1);
		System.out.println(ldt2);
		
		LocalDate ld = ldt1.toLocalDate();
		LocalTime lt = ldt1.toLocalTime();
		System.out.println(ld + " " + lt);
		
		LocalDateTime ldt3 = ldt1.toLocalDate().atTime(23, 59, 59);
		System.out.println(ldt3);
	}
	
	// Instant的设计初衷是为了便于机器使用
	// 以Unix元年时间（传统的设定为UTC时区1970年1月1日午夜时分）开始所经历的秒数进行计算。
	@Test
	public void testMachineTime() {
		Instant now = Instant.now(); 
		long currentTimeMills = now.toEpochMilli();
		System.out.println(currentTimeMills);
		System.out.println(System.currentTimeMillis());
	}
	
	// Duration类主要用于以秒和纳秒衡量时间的长短，
	// Period类以年、月或者日的方式对多个时间单位建模
	@Test
	public void testDurationVsPeriod() {
//		Duration d1 = Duration.between(time1, time2);
//		Duration d1 = Duration.between(dateTime1, dateTime2);
//		Duration d2 = Duration.between(instant1, instant2);
		
//		Period tenDays = Period.between(LocalDate.of(2014, 3, 8), LocalDate.of(2014, 3, 18));
		
//		Duration threeMinutes = Duration.ofMinutes(3);
//		Duration threeMinutes = Duration.of(3, ChronoUnit.MINUTES);
//		Period tenDays = Period.ofDays(10);
//		Period threeWeeks = Period.ofWeeks(3);
//		Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
		
		Duration d = Duration.ofHours(1).minus(Duration.ofMillis(1));
		System.out.println(d.getSeconds());
	}
	
	@Test
	public void testDateCalc() {
		LocalDate date1 = LocalDate.of(2014, 3, 18);
		LocalDate date2 = date1.plusWeeks(1);
		LocalDate date3 = date2.minusYears(3);
		LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS);
		
		System.out.println(date1);
		System.out.println(date2);
		System.out.println(date3);
		System.out.println(date4);
		
		LocalDate date = LocalDate.of(2014, 3, 18);
		date = date.with(ChronoField.MONTH_OF_YEAR, 9); // 更新操作
		date = date.plusYears(2).minusDays(10); // 加减操作
		System.out.println(date);
	}
	
	@Test
	public void testTemporalAdjuste() {
		LocalDate date = LocalDate.now().plusDays(1);
		date = date.with(new NextWorkingDay());
		System.out.println(date);
	}
	
	private static  class NextWorkingDay implements TemporalAdjuster {
		@Override
		public Temporal adjustInto(Temporal temporal) {
			DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
			else if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		}
	}
	
	
	/**
	 * 所有的DateTimeFormatter实例都是线程安全的，单例的。
	 */
	@Test
	public void testFormatDateTime() {
		LocalDateTime localDate = LocalDateTime.now();
		String s1 = localDate.format(DateTimeFormatter.BASIC_ISO_DATE); // 20180824
		String s2 = localDate.format(DateTimeFormatter.ISO_DATE); // 2018-08-24
		String s3 = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); // 2018-08-24T11:15:09.948
		String s4 = localDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日  HH:mm:ss")); // 2018年08月24日  11:19:51
		String s5 = localDate.format(customFormatter); // 2018年08月24日  11:19:51
		System.out.println(s1);
		System.out.println(s2);
		System.out.println(s3);
		System.out.println(s4);
		System.out.println(s5);
		
		// 将seconds转为HH:mm:ss格式
		System.out.println(
				LocalTime.MIN.plusSeconds(60*60*3+60+1).toString()
				);
	}
	
	public void testParseDateTimeString() {
		LocalDate date1 = LocalDate.parse("20180824", DateTimeFormatter.BASIC_ISO_DATE);
		LocalDate date2 = LocalDate.parse("2018-08-24", DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDateTime date3 = LocalDateTime.parse("2018/08/24 11:18:34", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
		System.out.println(date1);
		System.out.println(date2);
		System.out.println(date3);
	}
	
	static DateTimeFormatter customFormatter = new DateTimeFormatterBuilder()
			.appendText(ChronoField.YEAR)
			.appendLiteral("@")
			.appendText(ChronoField.MONTH_OF_YEAR)
			.appendLiteral("@")
			.appendText(ChronoField.DAY_OF_MONTH)
			.parseCaseInsensitive()
			.toFormatter(Locale.US);
	
	
	/**
	 * 日期API工具：大多数日期/时间API类都实现了一系列工具方法，如：加/减天数、周数、月份数，等等。
	 * 还有其他的工具方法能够使用TemporalAdjuster调整日期，并计算两个日期间的周期。
	 *
	 */
	public static void main(String[] args) {

		LocalDate today = LocalDate.now();

		// Get the Year, check if it's leap year
		System.out.println("Year " + today.getYear() + " is Leap Year? " + today.isLeapYear());

		// Compare two LocalDate for before and after
		System.out.println("Today is before 01/01/2015? " + today.isBefore(LocalDate.of(2015, 1, 1)));

		// Create LocalDateTime from LocalDate
		System.out.println("Current Time=" + today.atTime(LocalTime.now()));

		// plus and minus operations
		System.out.println("10 days after today will be " + today.plusDays(10));
		System.out.println("3 weeks after today will be " + today.plusWeeks(3));
		System.out.println("20 months after today will be " + today.plusMonths(20));

		System.out.println("10 days before today will be " + today.minusDays(10));
		System.out.println("3 weeks before today will be " + today.minusWeeks(3));
		System.out.println("20 months before today will be " + today.minusMonths(20));

		// Temporal adjusters for adjusting the dates
		System.out.println("First date of this month= " + today.with(TemporalAdjusters.firstDayOfMonth()));
		LocalDate lastDayOfYear = today.with(TemporalAdjusters.lastDayOfYear());
		System.out.println("Last date of this year= " + lastDayOfYear);

		Period period = today.until(lastDayOfYear);
		System.out.println("Period Format= " + period);
		System.out.println("Months remaining in the year= " + period.getMonths());
	}
	
}