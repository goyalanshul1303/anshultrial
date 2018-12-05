package com.example.aggarwalswati.providersob;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aggarwal.swati on 12/2/18.
 */

public class DataView {
    public Map<String, String> getOperatingHrs() {
        return operatingHrs;
    }

    public void setOperatingHrs(Map<String, String> operatingHrs) {

        this.operatingHrs = operatingHrs;
        operatingHrs.put("","Choose Operating Hours");
        operatingHrs.put("day","Day");
        operatingHrs.put("night","Night");
        operatingHrs.put("full","24 Hours");
    }

    // Spinner Drop down elements
    Map<String, String> operatingHrs = new LinkedHashMap<>();

    public Map<String, String> getTypeOfPrinting() {
        return typeOfPrinting;
    }

    public void setTypeOfPrinting(Map<String, String> typeOfPrinting) {
        this.typeOfPrinting = typeOfPrinting;
        typeOfPrinting.put("na","NA");
        typeOfPrinting.put("1","1 color");
        typeOfPrinting.put("2","2 color");
        typeOfPrinting.put("3","3 color");
        typeOfPrinting.put("4","4 color");

    }

    public Map<String, String> getTypesOfCartons() {
        return typesOfCartons;
    }

    public void setTypesOfCartons(Map<String, String> typesOfCartons) {
        this.typesOfCartons = typesOfCartons;
        typesOfCartons.put("C","Corrugated");
        typesOfCartons.put("D","Die Cutting");

    }

    public Map<String, String> getCorrugationType() {
        return corrugationType;
    }

    public void setCorrugationType(Map<String, String> corrugationType) {
        this.corrugationType = corrugationType;
        corrugationType.put("B","Broad");
        corrugationType.put("N","Narrow");
        corrugationType.put("S","Slim");
    }

    public Map<String, String> getBoxType() {
        return boxType;
    }

    public void setBoxType(Map<String, String> boxType) {

        this.boxType = boxType;
        boxType.put("HB","Hard Boxes");
        boxType.put("1","1 Ply");
        boxType.put("3","3 Ply");
        boxType.put("5","5 Ply");
        boxType.put("7","7 Ply");
    }

    Map<String, String> corrugationType = new LinkedHashMap<>();
    Map<String, String> boxType = new LinkedHashMap<>();

    Map<String, String> typeOfPrinting = new LinkedHashMap<>();
    Map<String, String> typesOfCartons = new LinkedHashMap<>();
}
