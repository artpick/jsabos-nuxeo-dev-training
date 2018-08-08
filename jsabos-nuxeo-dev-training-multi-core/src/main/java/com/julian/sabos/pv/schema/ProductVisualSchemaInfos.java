package com.julian.sabos.pv.schema;

public class ProductVisualSchemaInfos {

    // DO not instanciate
    private ProductVisualSchemaInfos() throws IllegalAccessException {
        throw new IllegalAccessException("This Helper class should only be used for static operations");
    }

    public static final String SCHEMA_NAME = "productVisualSchemas";
    public static final String SCHEMA_PREFIX = "pv";

    public static final String PRP_PRICE = "price";
    public static final String PRP_AVAILABLE_IMMEDIATELY = "availableImmediately";

    public static final String PV_PRP_PRICE = SCHEMA_PREFIX + ":" + PRP_PRICE;
    public static final String PV_DISTRIBUTOR = SCHEMA_PREFIX + ":" + "distributor";
    public static final String PV_DISTRIBUTOR_PRP_NAME = "name";
    public static final String PV_DISTRIBUTOR_PRP_SELL_LOCATION = "sellLocation";
}
