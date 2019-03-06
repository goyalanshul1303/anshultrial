package com.app.carton.provider;

import java.util.LinkedHashMap;

/**
 * Created by aggarwal.swati on 12/28/18.
 */

public class DataView {
    private LinkedHashMap<Integer,String>
            consumerType;

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
    public LinkedHashMap<Integer, String> getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(LinkedHashMap<Integer, String> consumerType) {
        this.consumerType = consumerType;
        consumerType.put(-1,"Select consumer type");
        consumerType.put(1,"FMCG");
        consumerType.put(2,"FOOD");

    }
    LinkedHashMap<Integer, String> corrugationType = new LinkedHashMap<>();
    LinkedHashMap<Integer, String> boxType = new LinkedHashMap<>();

    LinkedHashMap<Integer, String> typeOfPrinting = new LinkedHashMap<>();
    LinkedHashMap<Integer, String> typesOfCartons = new LinkedHashMap<>();
}
