package space.games.quiz.model;

import android.util.Pair;

import java.util.ArrayList;

public class Level {

    private String key;
    private String name;
    private boolean isOpen = true;
    private ArrayList<Image> images = new ArrayList<>();
    private String dialogStartText;
    private String dialogEndText;
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDialogStartText() {
        return dialogStartText;
    }

    public void setDialogStartText(String dialogStartText) {
        this.dialogStartText = dialogStartText;
    }

    public String getDialogEndText() {
        return dialogEndText;
    }

    public void setDialogEndText(String dialogEndText) {
        this.dialogEndText = dialogEndText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Pair<Image, Image> getPair() {
        int id1 = (int) (Math.random() * images.size());
        int id2 = id1;
        while (id2 == id1) {
            id2 = (int) (Math.random() * images.size());
        }
        return new Pair<>(images.get(id1), images.get(id2));
    }

}
