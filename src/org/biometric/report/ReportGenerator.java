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

public class ReportGenerator {
	static Date init_time;
	static String total_working_hours;
	public static final float FONT_SIZE = 10;
	private static boolean isMoreThanOne = false;
	private static boolean isPrintHeader = true;
	private static long half_day_count, full_day_count, total_days_count, absent_count;

	public static void generateReport(Date from_date, Date to_date) throws Exception {
		Date date = from_date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(from_date);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		String dest = AppConfigInfo.getDefaultStorage() + "attendance_report_detailed(" + format.format(from_date)
				+ " to " + format.format(to_date) + ").pdf";
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		Document doc = new Document(pdfDoc);

		Paragraph p = new Paragraph();
		p.setFontSize(FONT_SIZE);
		p.add("*Detailed attendance report for the period " + format.format(from_date) + " to " + format.format(to_date)
				+ "*");
		doc.add(p);

		try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
				Session session = sessionFactory.openSession()) {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Employee.class);
			List emp_list = detachedCriteria.getExecutableCriteria(session).list();
			if (emp_list.size() > 0) {
				WorkingHours.calculateWorkingHours();
				Iterator emp_itr = emp_list.iterator();
				while (emp_itr.hasNext()) {
					resetCount();
					isPrintHeader = true;
					Employee employee = (Employee) emp_itr.next();
					p = new Paragraph();
					p.setFontSize(FONT_SIZE);
					p.setUnderline();
					p.add("Employee ID: " + employee.getId());
					doc.add(p);
					while (date.compareTo(to_date) <= 0) {
						init_time = getInitTime();
						createPDF(session, date, employee, doc);
						cal.add(Calendar.DATE, 1);
						date = format.parse(format.format(cal.getTime()));
						total_days_count++;
					}
					if (full_day_count <= 0 && absent_count <= 0 && half_day_count <= 0) {
						p = new Paragraph();
						p.setFontSize(FONT_SIZE);
						p.setTextAlignment(TextAlignment.CENTER);
						p.add("No records.");
						doc.add(p);
					}
					p = new Paragraph();
					p.setFontSize(FONT_SIZE);
					p.add("Total Days: " + total_days_count + ", Full days: " + full_day_count + ", Days absent: "
							+ absent_count + ", Half days: " + half_day_count);
					doc.add(p);
					cal.setTime(from_date);
					date = from_date;
				}
				doc.close();
			}
		}
	}

	public static void generateReport(Date from_date, Date to_date, Employee employee, Session session)
			throws Exception {
		Date date = from_date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(from_date);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat date_format = new SimpleDateFormat("dd-MMM-yyyy");

		String dest = AppConfigInfo.getDefaultStorage() + "(" + employee.getId() + ")attendance_report_detailed("
				+ date_format.format(from_date) + " to " + date_format.format(to_date) + ").pdf";
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		Document doc = new Document(pdfDoc);

		Paragraph p = new Paragraph();
		p.setFontSize(FONT_SIZE);
		p.add("*Detailed attendance report for the period " + date_format.format(from_date) + " to "
				+ date_format.format(to_date) + "*");
		doc.add(p);

		resetCount();
		isPrintHeader = true;
		p = new Paragraph();
		p.setFontSize(FONT_SIZE);
		p.setUnderline();
		p.add("Employee ID: " + employee.getId());
		doc.add(p);
		while (date.compareTo(to_date) <= 0) {
			init_time = getInitTime();
			createPDF(session, date, employee, doc);
			cal.add(Calendar.DATE, 1);
			date = format.parse(format.format(cal.getTime()));
			total_days_count++;
		}
		if (full_day_count <= 0 && absent_count <= 0 && half_day_count <= 0) {
			p = new Paragraph();
			p.setFontSize(FONT_SIZE);
			p.setTextAlignment(TextAlignment.CENTER);
			p.add("No records.");
			doc.add(p);
		}
		p = new Paragraph();
		p.setFontSize(FONT_SIZE);
		p.add("Total Days: " + total_days_count + ", Full days: " + full_day_count + ", Days absent: " + absent_count
				+ ", Half days: " + half_day_count);
		doc.add(p);
		cal.setTime(from_date);
		date = from_date;

		doc.close();
	}

	public static void createPDF(Session session, Date date, Employee e, Document doc) throws Exception {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Table table = new Table(6);

		DetachedCriteria criteria = DetachedCriteria.forClass(Attendance.class);
		criteria.add(Restrictions.eq("employee", e));
		criteria.add(Restrictions.eq("date", date));
		List attn_list = criteria.getExecutableCriteria(session).list();
		if (attn_list.size() > 0) {
			if (isPrintHeader) {
				String[] table_heading = { "Employee Name", "Date", "In-Time", "Out-Time", "Working Hours", "Remark" };
				addTableHeader(table, table_heading);
				isPrintHeader = false;
			}
			Iterator attn_itr = attn_list.iterator();

			if (attn_list.size() > 1) {
				isMoreThanOne = true;
			}
			while (attn_itr.hasNext()) {
				Attendance a = (Attendance) attn_itr.next();
				if (isMoreThanOne) {
					addNameCell(e.getName(), table);
					addDateCell(a.getDate(), table);
					addInTimeCell(a.getIn_time(), table);
					addOutTimeCell(a.getOut_time(), table);
					addWorkingHoursCell(a.getWorking_hours(), table);
					addNormalRemarkCell("--", table);
					init_time = TimeCalculation.CalculateTime(init_time, a.getWorking_hours());
				} else {
					init_time = TimeCalculation.CalculateTime(init_time, a.getWorking_hours());
					addNameCell(e.getName(), table);
					addDateCell(a.getDate(), table);
					addInTimeCell(a.getIn_time(), table);
					addOutTimeCell(a.getOut_time(), table);
					addTotalWorkingHours(init_time, table);
					addRemarks(init_time, table);
				}
			}

			if (isMoreThanOne) {
				addTotalCell("Total Hours:", table);
				addTotalWorkingHours(init_time, table);
				addRemarks(init_time, table);
			}
			isMoreThanOne = false;
			doc.add(table);
			Paragraph p = new Paragraph();
			p.add(" ");
			doc.add(p);
		} else {
			if (isPrintHeader) {
				String[] table_heading = { "Employee Name", "Date", "In-Time", "Out-Time", "Working Hours", "Remark" };
				addTableHeader(table, table_heading);
				isPrintHeader = false;
			}

			addNameCell(e.getName(), table);
			addDateCell(date, table);
			addInTimeCell(df.format(df.parse("00:00:00")), table);
			addOutTimeCell(df.format(df.parse("00:00:00")), table);
			addTotalWorkingHours(df.parse(df.format(df.parse("00:00:00"))), table);
			addRemarks(df.parse(df.format(df.parse("00:00:00"))), table);
			doc.add(table);
			Paragraph p = new Paragraph();
			p.add(" ");
			doc.add(p);
		}
	}

	private static void addNameCell(String value, Table table) {
		Cell nameCell = new Cell();
		nameCell.setTextAlignment(TextAlignment.CENTER);
		nameCell.setFontSize(FONT_SIZE);
		nameCell.add(value);
		table.addCell(nameCell);
	}

	private static void addTotalCell(String value, Table table) {
		Cell totalCell = new Cell(1, 4);
		totalCell.setTextAlignment(TextAlignment.CENTER);
		totalCell.setFontSize(FONT_SIZE);
		totalCell.add(value);
		table.addCell(totalCell);
	}

	private static void addInTimeCell(Date inTime, Table table) {
		Cell inTimeCell = new Cell();
		inTimeCell.setTextAlignment(TextAlignment.CENTER);
		inTimeCell.setFontSize(FONT_SIZE);
		inTimeCell.add(inTime.toString());
		table.addCell(inTimeCell);
	}

	private static void addOutTimeCell(Date OutTime, Table table) {
		Cell outTimeCell = new Cell();
		outTimeCell.setTextAlignment(TextAlignment.CENTER);
		outTimeCell.setFontSize(FONT_SIZE);
		outTimeCell.add(OutTime.toString());
		table.addCell(outTimeCell);
	}

	private static void addInTimeCell(String inTime, Table table) {
		Cell inTimeCell = new Cell();
		inTimeCell.setTextAlignment(TextAlignment.CENTER);
		inTimeCell.setFontSize(FONT_SIZE);
		inTimeCell.add(inTime);
		table.addCell(inTimeCell);
	}

	private static void addOutTimeCell(String OutTime, Table table) {
		Cell outTimeCell = new Cell();
		outTimeCell.setTextAlignment(TextAlignment.CENTER);
		outTimeCell.setFontSize(FONT_SIZE);
		outTimeCell.add(OutTime);
		table.addCell(outTimeCell);
	}

	private static void addDateCell(Date date, Table table) {
		Cell dateCell = new Cell();
		dateCell.setTextAlignment(TextAlignment.CENTER);
		dateCell.setFontSize(FONT_SIZE);
		DateFormat df = new SimpleDateFormat("dd MMM yyyy");
		dateCell.add(df.format(date));
		table.addCell(dateCell);
	}

	private static void addWorkingHoursCell(Date working_hours, Table table) {
		Cell workingHoursCell = new Cell();
		workingHoursCell.setTextAlignment(TextAlignment.CENTER);
		workingHoursCell.setFontSize(FONT_SIZE);
		workingHoursCell.add(working_hours.toString());
		table.addCell(workingHoursCell);
	}

	private static void addRedRemarkCell(String remark, Table table) {
		Cell remarksCell = new Cell();
		remarksCell.setTextAlignment(TextAlignment.CENTER);
		remarksCell.setFontSize(FONT_SIZE);
		remarksCell.setFontColor(Color.WHITE);
		remarksCell.setBackgroundColor(Color.RED, 15);
		remarksCell.add(remark);
		table.addCell(remarksCell);
	}

	private static void addGreenRemarkCell(String remark, Table table) {
		Cell remarksCell = new Cell();
		remarksCell.setTextAlignment(TextAlignment.CENTER);
		remarksCell.setFontSize(FONT_SIZE);
		remarksCell.setBackgroundColor(Color.GREEN, 15);
		remarksCell.add(remark);
		table.addCell(remarksCell);
	}

	private static void addYellowRemarkCell(String remark, Table table) {
		Cell remarksCell = new Cell();
		remarksCell.setTextAlignment(TextAlignment.CENTER);
		remarksCell.setFontSize(FONT_SIZE);
		remarksCell.setBackgroundColor(Color.YELLOW, 15);
		remarksCell.add(remark);
		table.addCell(remarksCell);
	}

	private static void addNormalRemarkCell(String remark, Table table) {
		Cell remarksCell = new Cell();
		remarksCell.setTextAlignment(TextAlignment.CENTER);
		remarksCell.setFontSize(FONT_SIZE);
		remarksCell.add(remark);
		table.addCell(remarksCell);
	}

	private static void addTotalWorkingHours(Date total_hours, Table table) {
		Cell totalworkingHoursCell = new Cell();
		totalworkingHoursCell.setTextAlignment(TextAlignment.CENTER);
		totalworkingHoursCell.setFontSize(FONT_SIZE);
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		totalworkingHoursCell.add(df.format(total_hours));
		table.addCell(totalworkingHoursCell);
	}

	private static void addRemarks(Date init_time, Table table) throws Exception {
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date full_day_time = df.parse(AppConfigInfo.getFullWorkingHours());
		Date half_day_time = df.parse(AppConfigInfo.getHalfWorkingHours());

		if (init_time.compareTo(full_day_time) > 0 || init_time.compareTo(full_day_time) == 0) {
			addGreenRemarkCell("Present", table);
			full_day_count++;

		} else if (init_time.compareTo(half_day_time) > 0 && init_time.compareTo(full_day_time) < 0) {
			addYellowRemarkCell("Half-Day", table);
			half_day_count++;
		} else {
			addRedRemarkCell("Absent", table);
			absent_count++;
		}
	}

	private static void addTableHeader(Table table, String[] header_values) {
		for (int i = 0; i < header_values.length; i++) {
			Cell heading = new Cell();
			heading.setTextAlignment(TextAlignment.CENTER);
			heading.setFontSize(FONT_SIZE);
			heading.setFontColor(Color.WHITE);
			heading.setBackgroundColor(Color.GRAY, 20);
			heading.add(new Paragraph(header_values[i]));
			table.addCell(heading);
		}
	}

	private static Date getInitTime() throws Exception {
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		String i_time = "00:00:00";
		init_time = format.parse(i_time);
		return init_time;
	}

	private static void resetCount() {
		half_day_count = 0;
		full_day_count = 0;
		absent_count = 0;
		total_days_count = 0;
	}

	public static long getHalf_day_count() {
		return half_day_count;
	}

	public static long getFull_day_count() {
		return full_day_count;
	}

	public static long getTotal_days_count() {
		return total_days_count;
	}

	public static long getAbsent_count() {
		return absent_count;
	}

}
