package API;

import java.util.UUID;

public class roles {
    private UUID id;
    private UUID project_id;
    private String role_name;
    private String role_description;
    private int slots;

    public roles() {
    }

    public roles(UUID id, UUID project_id, String role_name, String role_description, int slots) {
        this.id = id;
        this.project_id = project_id;
        this.role_name = role_name;
        this.role_description = role_description;
        this.slots = slots;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProject_id() {
        return project_id;
    }

    public void setProject_id(UUID project_id) {
        this.project_id = project_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_description() {
        return role_description;
    }

    public void setRole_description(String role_description) {
        this.role_description = role_description;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }
}
