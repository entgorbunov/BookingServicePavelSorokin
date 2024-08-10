package sorokin.java.course.bookingservicepavelsorokin.users.service;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static UserRole fromString(String role) {
        for (UserRole userRole : values()) {
            if (userRole.role.equals(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Invalid user role: " + role);
    }
}
