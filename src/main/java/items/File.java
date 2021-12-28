package items;

import com.fasterxml.jackson.annotation.JsonProperty;

public class File {

    private final String copyright;
    private final String date;
    private final String explanation;
    private final String media_type;
    private final String service_version;
    private final String title;
    private final String url;


    public File(@JsonProperty("copyright") String copyright,
                @JsonProperty("date") String date,
                @JsonProperty("explanation") String explanation,
                @JsonProperty("media_type") String media_type,
                @JsonProperty("service_version") String service_version,
                @JsonProperty("title") String title,
                @JsonProperty("url") String url
    ) {
        this.copyright = copyright;
        this.date = date;
        this.explanation = explanation;
        this.media_type = media_type;
        this.service_version = service_version;
        this.title = title;
        this.url = url;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getService_version() {
        return service_version;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "File{" +
                "copyright='" + copyright + '\'' +
                ",\ndate='" + date + '\'' +
                ",\nexplanation='" + explanation + '\'' +
                ",\nmedia_type='" + media_type + '\'' +
                ",\nservice_version='" + service_version + '\'' +
                ",\ntitle='" + title + '\'' +
                ",\nurl='" + url + '\'' +
                "\n}";
    }
}
