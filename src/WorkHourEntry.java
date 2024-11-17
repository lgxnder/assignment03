public class WorkHourEntry {
    private int id;
    private String name;
    private String role;
    private int hours;

    public WorkHourEntry(int id, String name, String role, int hours) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.hours = hours;
    }

    public int getId() 		{ return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public int getHours() 	{ return hours; }

    public void setId(int id) 			{ this.id = id; }
    public void setName(String name) 	{ this.name = name; }
    public void setRole(String role) 	{ this.role = role; }
    public void setHours(int hours) 	{ this.hours = hours; }
}