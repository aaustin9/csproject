package toromu.familymap.models;

import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.HashSet;

/**
 * Map Settings Class
 * Singleton method to retain all map settings for the Main and Map Activities
 * Created by Austin on 07-Dec-16.
 */
public class MapSettings {
    public static final MapSettings SINGLETON = new MapSettings();
    private int mapType;
    private HashSet<String> eventTypeExclusions;
    private boolean showMales;
    private boolean showFemales;
    private boolean showMotherSide;
    private boolean showFatherSide;
    private HashSet<Integer> offFilters;
    private String spouseLinesColor;
    private String familyTreeLinesColor;
    private String lifeStoryLinesColor;
    private boolean isSpouseLines;
    private boolean isFamilyTreeLines;
    private boolean isLifeStoryLines;

    private MapSettings() {
        mapType = GoogleMap.MAP_TYPE_NORMAL;
        eventTypeExclusions = new HashSet<>();
        offFilters = new HashSet<>();
        showMales = true;
        showFemales = true;
        showMotherSide = true;
        showFatherSide = true;
        lifeStoryLinesColor = "Red";
        familyTreeLinesColor = "Green";
        spouseLinesColor = "Blue";
        isLifeStoryLines = true;
        isFamilyTreeLines = true;
        isSpouseLines = true;
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
    }

    public HashSet<String> getEventTypeExclusions() { return eventTypeExclusions; }

    public boolean isShowMales() {
        return showMales;
    }

    public boolean isShowFemales() {
        return showFemales;
    }

    public boolean isShowMotherSide() {
        return showMotherSide;
    }

    public boolean isShowFatherSide() {
        return showFatherSide;
    }

    public String getSpouseLinesColor() {
        return spouseLinesColor;
    }

    public String getFamilyTreeLinesColor() {
        return familyTreeLinesColor;
    }

    public String getLifeStoryLinesColor() {
        return lifeStoryLinesColor;
    }

    public boolean isSpouseLines() {
        return isSpouseLines;
    }

    public boolean isFamilyTreeLines() {
        return isFamilyTreeLines;
    }

    public boolean isLifeStoryLines() {
        return isLifeStoryLines;
    }

    public void setSpouseLinesColor(String spouseLinesColor) {
        this.spouseLinesColor = spouseLinesColor;
    }

    public void setFamilyTreeLinesColor(String familyTreeLinesColor) {
        this.familyTreeLinesColor = familyTreeLinesColor;
    }

    public void setLifeStoryLinesColor(String lifeStoryLinesColor) {
        this.lifeStoryLinesColor = lifeStoryLinesColor;
    }

    public void setSpouseLines(boolean spouseLines) {
        isSpouseLines = spouseLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        isFamilyTreeLines = familyTreeLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        isLifeStoryLines = lifeStoryLines;
    }

    public void switchShowMales() {
        if(showMales) showMales = false;
        else showMales = true;
    }

    public void switchShowFemales() {
        if(showFemales) showFemales = false;
        else showFemales = true;
    }

    public void switchShowMotherSide() {
        if(showMotherSide) showMotherSide = false;
        else showMotherSide = true;
    }

    public void switchShowFatherSide() {
        if(showFatherSide) showFatherSide = false;
        else showFatherSide = true;
    }

    public void switchShowEventType(String type) {
        if(eventTypeExclusions.contains(type)) eventTypeExclusions.remove(type);
        else eventTypeExclusions.add(type);
    }

    public void switchOffFilters(int filterNumber) {
        if(offFilters.contains(filterNumber)) offFilters.remove(filterNumber);
        else offFilters.add(filterNumber);
    }

    public HashSet<Integer> getOffFilters() { return offFilters; }

//    public void
}
