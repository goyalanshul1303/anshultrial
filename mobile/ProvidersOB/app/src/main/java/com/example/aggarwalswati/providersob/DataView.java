package com.example.aggarwalswati.providersob;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aggarwal.swati on 12/2/18.
 */

public class DataView {

    public LinkedHashMap<String, String> getStatesMap() {
        return statesMap;
    }

    public void setStatesMap(LinkedHashMap<String, String> statesMap) {
        this.statesMap = statesMap;
        statesMap.put("", "Select State");
        statesMap.put("Andhra Pradesh", "Andhra Pradesh");
        statesMap.put("Andaman and Nicobar Islands", "Andaman and Nicobar Islands");
        statesMap.put("Arunachal Pradesh", "Arunachal Pradesh");
        statesMap.put("Assam", "Assam");
        statesMap.put("Bihar", "Bihar");
        statesMap.put("Chandigarh", "Chandigarh");
        statesMap.put("Chhattisgarh", "Chhattisgarh");
        statesMap.put("Gujarat", "Gujarat");
        statesMap.put("Haryana", "Haryana");
        statesMap.put("Himachal Pradesh", "Himachal Pradesh");
        statesMap.put("Jammu and Kashmir", "Jammu and Kashmir");
        statesMap.put("Jharkhand", "Jharkhand");
        statesMap.put("Karnataka", "Karnataka");
        statesMap.put("Madhya Pradesh", "Madhya Pradesh");
        statesMap.put("Maharashtra", "Maharashtra");
        statesMap.put("Delhi", "Delhi");
        statesMap.put("Punjab", "Punjab");
        statesMap.put("Rajasthan", "Rajasthan");
        statesMap.put("Uttar Pradesh", "Uttar Pradesh");
        statesMap.put("Uttarakhand", "Uttarakhand");
    }

    LinkedHashMap<String, String> statesMap = new LinkedHashMap<>();

    public LinkedHashMap<Integer, String> getOperatingHrs() {
        return operatingHrs;
    }

    public void setOperatingHrs(LinkedHashMap<Integer, String> operatingHrs) {

        this.operatingHrs = operatingHrs;
        operatingHrs.put(-1, "Choose Operating Hours");
        operatingHrs.put(1, "Day");
        operatingHrs.put(2, "Night");
        operatingHrs.put(3, "24 Hours");
    }

    // Spinner Drop down elements
    LinkedHashMap<Integer, String> operatingHrs = new LinkedHashMap<>();

    public LinkedHashMap<Integer, String> getTypeOfPrinting() {
        return typeOfPrinting;
    }

    public void setTypeOfPrinting(LinkedHashMap<Integer, String> typeOfPrinting) {
        this.typeOfPrinting = typeOfPrinting;
        typeOfPrinting.put(-1, "Choose printing  types");
        typeOfPrinting.put(0, "NA");
        typeOfPrinting.put(1, "1 color");
        typeOfPrinting.put(2, "2 color");
        typeOfPrinting.put(3, "3 color");
        typeOfPrinting.put(4, "4 color");

    }


    public LinkedHashMap<Integer, String> getTypesOfCartons() {
        return typesOfCartons;
    }

    public void setTypesOfCartons(LinkedHashMap<Integer, String> typesOfCartons) {
        this.typesOfCartons = typesOfCartons;
        typesOfCartons.put(1, "Corrugated");
        typesOfCartons.put(2, "Die Cutting");

    }

    public LinkedHashMap<Integer, String> getCorrugationType() {
        return corrugationType;
    }

    public void setCorrugationType(LinkedHashMap<Integer, String> corrugationType) {
        this.corrugationType = corrugationType;
        corrugationType.put(1, "Broad");
        corrugationType.put(2, "Narrow");
        corrugationType.put(3, "Slim");
    }

    public LinkedHashMap<Integer, String> getBoxType() {
        return boxType;
    }

    public void setBoxType(LinkedHashMap<Integer, String> boxType) {

        this.boxType = boxType;
//        boxType.put("HB","Hard Boxes");
        boxType.put(1, "1 Ply");
        boxType.put(3, "3 Ply");
        boxType.put(5, "5 Ply");
        boxType.put(7, "7 Ply");
    }

    LinkedHashMap<Integer, String> corrugationType = new LinkedHashMap<>();
    LinkedHashMap<Integer, String> boxType = new LinkedHashMap<>();

    LinkedHashMap<Integer, String> typeOfPrinting = new LinkedHashMap<>();
    LinkedHashMap<Integer, String> typesOfCartons = new LinkedHashMap<>();
}
