package Model;

public class ItemCategory {
    private String name;
    private String pathName;

    public ItemCategory(String name, String pathName){
        this.name = name;
        this.pathName = pathName;
    }

    @Override
    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public String getPathName() {
        return pathName;
    }
}
