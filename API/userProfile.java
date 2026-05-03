package API;

import java.util.UUID;

public class userProfile {
    private UUID id;
    private UUID user_id;
    private String display_name;
    private String bio;
    private String county;
    private String experience_level;
    private String school;
    private String major;

    public userProfile() {
    }

    public userProfile(UUID id, UUID user_id, String display_name, String bio, String county, String experience_level, String school, String major) {
        this.id = id;
        this.user_id = user_id;
        this.display_name = display_name;
        this.bio = bio;
        this.county = county;
        this.experience_level = experience_level;
        this.school = school;
        this.major = major;
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

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getExperience_level() {
        return experience_level;
    }

    public void setExperience_level(String experience_level) {
        this.experience_level = experience_level;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
