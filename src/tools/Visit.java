package tools;

public class Visit {
	
	private int id;
	private String name;
	private String docName;
	private String date;
	private String services;
	private String prescription;
	
	
	public Visit(int id, String name, String docName, String date, String services, String prescription) {
		super();
		this.id = id;
		this.name = name;
		this.docName = docName;
		this.date = date;
		this.services = services;
		this.prescription = prescription;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDocName() {
		return docName;
	}
	public String getDate() {
		return date;
	}
	public String getServices() {
		return services;
	}
	public String getPrescription() {
		return prescription;
	}
	
	
	
	


}
