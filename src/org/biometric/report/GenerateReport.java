package org.biometric.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.biometric.config.AppConfigInfo;
import org.biometric.persist.model.Attendance;
import org.biometric.persist.model.Employee;
import org.biometric.time.TimeCalculation;
import org.biometric.time.WorkingHours;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

public class GenerateReport {

	private static final float FONT_SIZE = 10;
	static Date init_time, emp_per_day_time;
	private static long half_day_count, full_day_count, total_days_count, absent_count;

	public static void getReport(Date from_date, Date to_date) throws Exception {
		Date date = from_date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(from_date);
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss");
		String[] table_heading = { "Name", "Total Days", "Full days", "Half days", "Days Absent",
				"Total working hours" };

		String dest = AppConfigInfo.getDefaultStorage() + "attendance_report_summarized("
				+ date_format.format(from_date) + " to " + date_format.format(to_date) + ").pdf";
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		Document doc = new Document(pdfDoc);

		Paragraph p = new Paragraph();
		p.setFontSize(FONT_SIZE);
		p.add("*Summarized attendance report for the period " + date_format.format(from_date) + " to "
				+ date_format.format(to_date) + "*");
		doc.add(p);

		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			DetachedCriteria criteria = DetachedCriteria.forClass(Employee.class);
			List emp_list = criteria.getExecutableCriteria(session).list();
			if (emp_list.size() > 0) {
				WorkingHours.calculateWorkingHours();
				Iterator emp_itr = emp_list.iterator();

				while (emp_itr.hasNext()) {
					getInitTime();
					getEmployeePerDayInitTime();
					Employee employee = (Employee) emp_itr.next();
					p = new Paragraph();
					p.setFontSize(FONT_SIZE);
					p.setUnderline();
					p.add("Employee ID: " + employee.getId());
					doc.add(p);
					while (date.compareTo(to_date) <= 0) {
						generatePDF(session, employee, date);
						cal.add(Calendar.DATE, 1);
						date = date_format.parse(date_format.format(cal.getTime()));
						total_days_count++;
						getEmployeePerDayInitTime();

					}

					Table table = new Table(6);
					Cell heading;
					for (int i = 0; i < 6; i++) {
						heading = new Cell();
						heading.setTextAlignment(TextAlignment.CENTER);
						heading.setFontSize(FONT_SIZE);
						heading.setFontColor(Color.WHITE);
						heading.setBackgroundColor(Color.GRAY, 20);
						heading.add(new Paragraph(table_heading[i]));
						table.addCell(heading);
					}

					Cell nameCell = new Cell();
					nameCell.setTextAlignment(TextAlignment.CENTER);
					nameCell.setFontSize(FONT_SIZE);
					nameCell.add(employee.getName());
					table.addCell(nameCell);

					Cell totalDaysCell = new Cell();
					totalDaysCell.setTextAlignment(TextAlignment.CENTER);
					totalDaysCell.setFontSize(FONT_SIZE);
					totalDaysCell.add(String.valueOf(total_days_count));
					table.addCell(totalDaysCell);

					Cell totalDaysPresentCell = new Cell();
					totalDaysPresentCell.setTextAlignment(TextAlignment.CENTER);
					totalDaysPresentCell.setFontSize(FONT_SIZE);
					totalDaysPresentCell.add(String.valueOf(full_day_count));
					table.addCell(totalDaysPresentCell);

					Cell halfdaysCell = new Cell();
					halfdaysCell.setTextAlignment(TextAlignment.CENTER);
					halfdaysCell.setFontSize(FONT_SIZE);
					halfdaysCell.add(String.valueOf(half_day_count));
					table.addCell(halfdaysCell);

					Cell daysAbsentCell = new Cell();
					daysAbsentCell.setTextAlignment(TextAlignment.CENTER);
					daysAbsentCell.setFontSize(FONT_SIZE);
					daysAbsentCell.add(String.valueOf(absent_count));
					table.addCell(daysAbsentCell);

					Cell totalworkingHoursCell = new Cell();
					totalworkingHoursCell.setTextAlignment(TextAlignment.CENTER);
					totalworkingHoursCell.setFontSize(FONT_SIZE);
					totalworkingHoursCell.add(time_format.format(init_time));
					table.addCell(totalworkingHoursCell);

					doc.add(table);
					resetCount();
					cal.setTime(from_date);
					date = from_date;
				}
				doc.close();
			}
		}
	}

	public static void getReport(Date from_date, Date to_date, Employee employee, Session session) throws Exception {
		Date date = from_date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(from_date);
		SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat date_format1 = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss");
		String[] table_heading = { "Name", "Total Days", "Full days", "Half days", "Days Absent",
				"Total working hours" };

		String dest = AppConfigInfo.getDefaultStorage() + "(" + employee.getId() + ")attendance_report_summarized("
				+ date_format1.format(from_date) + " to " + date_format1.format(to_date) + ").pdf";
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		Document doc = new Document(pdfDoc);

		Paragraph p = new Paragraph();
		p.setFontSize(FONT_SIZE);
		p.add("*Summarized attendance report for the period " + date_format1.format(from_date) + " to "
				+ date_format1.format(to_date) + "*");
		doc.add(p);

		getInitTime();
		getEmployeePerDayInitTime();
		p = new Paragraph();
		p.setFontSize(FONT_SIZE);
		p.setUnderline();
		p.add("Employee ID: " + employee.getId());
		doc.add(p);
		while (date.compareTo(to_date) <= 0) {
			generatePDF(session, employee, date);
			cal.add(Calendar.DATE, 1);
			date = date_format.parse(date_format.format(cal.getTime()));
			total_days_count++;

		}

		Table table = new Table(6);
		Cell heading;
		for (int i = 0; i < 6; i++) {
			heading = new Cell();
			heading.setTextAlignment(TextAlignment.CENTER);
			heading.setFontSize(FONT_SIZE);
			heading.setFontColor(Color.WHITE);
			heading.setBackgroundColor(Color.GRAY, 20);
			heading.add(new Paragraph(table_heading[i]));
			table.addCell(heading);
		}
		Cell nameCell = new Cell();
		nameCell.setTextAlignment(TextAlignment.CENTER);
		nameCell.setFontSize(FONT_SIZE);
		nameCell.add(employee.getName());
		table.addCell(nameCell);

		Cell totalDaysCell = new Cell();
		totalDaysCell.setTextAlignment(TextAlignment.CENTER);
		totalDaysCell.setFontSize(FONT_SIZE);
		totalDaysCell.add(String.valueOf(total_days_count));
		table.addCell(totalDaysCell);

		Cell totalDaysPresentCell = new Cell();
		totalDaysPresentCell.setTextAlignment(TextAlignment.CENTER);
		totalDaysPresentCell.setFontSize(FONT_SIZE);
		totalDaysPresentCell.add(String.valueOf(full_day_count));
		table.addCell(totalDaysPresentCell);

		Cell halfdaysCell = new Cell();
		halfdaysCell.setTextAlignment(TextAlignment.CENTER);
		halfdaysCell.setFontSize(FONT_SIZE);
		halfdaysCell.add(String.valueOf(half_day_count));
		table.addCell(halfdaysCell);

		Cell daysAbsentCell = new Cell();
		daysAbsentCell.setTextAlignment(TextAlignment.CENTER);
		daysAbsentCell.setFontSize(FONT_SIZE);
		daysAbsentCell.add(String.valueOf(absent_count));
		table.addCell(daysAbsentCell);

		Cell totalworkingHoursCell = new Cell();
		totalworkingHoursCell.setTextAlignment(TextAlignment.CENTER);
		totalworkingHoursCell.setFontSize(FONT_SIZE);
		totalworkingHoursCell.add(time_format.format(init_time));
		table.addCell(totalworkingHoursCell);

		doc.add(table);
		resetCount();
		cal.setTime(from_date);
		date = from_date;
		doc.close();
	}

	public static void generatePDF(Session session, Employee e, Date date) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Attendance.class);
		criteria.add(Restrictions.eq("employee", e));
		criteria.add(Restrictions.eq("date", date));
		List attn_list = criteria.getExecutableCriteria(session).list();
		if (attn_list.size() > 0) {
			Iterator attn_itr = attn_list.iterator();
			while (attn_itr.hasNext()) {
				Attendance a = (Attendance) attn_itr.next();
				init_time = TimeCalculation.CalculateTime(init_time, a.getWorking_hours());
				emp_per_day_time = TimeCalculation.CalculateTime(emp_per_day_time, a.getWorking_hours());
			}
			countDays(emp_per_day_time);
		} else {
			DateFormat format = new SimpleDateFormat("HH:mm:ss");
			countDays(format.parse("00:00:00"));
		}
	}

	private static Date getInitTime() throws Exception {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		String i_time = "00:00:00";
		init_time = format.parse(i_time);
		return init_time;
	}

	private static Date getEmployeePerDayInitTime() throws Exception {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		String i_time = "00:00:00";
		emp_per_day_time = format.parse(i_time);
		return emp_per_day_time;
	}

	private static void resetCount() {
		half_day_count = 0;
		full_day_count = 0;
		absent_count = 0;
		total_days_count = 0;
	}

	private static void countDays(Date init_time) throws Exception {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date full_day_time = df.parse(AppConfigInfo.getFullWorkingHours());
		Date half_day_time = df.parse(AppConfigInfo.getHalfWorkingHours());

		if (init_time.compareTo(full_day_time) > 0 || init_time.compareTo(full_day_time) == 0) {
			full_day_count++;

		} else if (init_time.compareTo(half_day_time) > 0 && init_time.compareTo(full_day_time) < 0) {
			half_day_count++;
		} else {
			absent_count++;
		}
	}

}
