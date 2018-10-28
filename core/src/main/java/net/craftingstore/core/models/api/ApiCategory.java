package net.craftingstore.core.models.api;

public class ApiCategory {

    private int id;
    private String name;
    private String description;
    private String minecraftIconName;
    private String url;
    private Boolean subCategory;
    private Package packages[];

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMinecraftIconName() {
        return minecraftIconName;
    }

    public String getUrl() {
        return url;
    }

    public Boolean isSubCategory() {
        return subCategory;
    }

    public Package[] getpackages() {
        return packages;
    }
}
