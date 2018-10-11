package com.qurasense.labApi.assay.model;

import java.util.Arrays;

public enum AssayUnit {
    PERCENT("%"),
    MCG_DL("mcg/dL"),
    MG_DL("mg/dL"),
    MG_L("mg/L"),
    NG_ML("ng/mL"),
    U_L("U/L"),
    UG_DL("ug/dL"),
    ULU_ML("ulU/mL");

    public final String unitType;

    AssayUnit(String unitType) {
        this.unitType = unitType;
    }

    public static AssayUnit of(String unitType) {
        if (unitType == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.unitType.equalsIgnoreCase(unitType.trim()))
                .findFirst()
                .get();
    }
}
