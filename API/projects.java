package API;

import java.util.UUID;

public class projects {
    private UUID id;
    private UUID user_id;
    private String title;
    private String general_description;
    private String type;
    private String county;

    public projects() {
    }

    public projects(UUID id, UUID user_id, String title, String general_description, String type, String county) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.general_description = general_description;
        this.type = type;
        this.county = county;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGeneral_description() {
        return general_description;
    }

    public void setGeneral_description(String general_description) {
        this.general_description = general_description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
