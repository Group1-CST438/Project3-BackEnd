package API;

import java.util.UUID;

public class applications {
    private UUID id;
    private UUID user_id;
    private UUID project_id;
    private UUID role_id;
    private String status;

    public applications() {
    }

    public applications(UUID id, UUID user_id, UUID project_id, UUID role_id, String status) {
        this.id = id;
        this.user_id = user_id;
        this.project_id = project_id;
        this.role_id = role_id;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public UUID getProject_id() {
        return project_id;
    }

    public void setProject_id(UUID project_id) {
        this.project_id = project_id;
    }

    public UUID getRole_id() {
        return role_id;
    }

    public void setRole_id(UUID role_id) {
        this.role_id = role_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
