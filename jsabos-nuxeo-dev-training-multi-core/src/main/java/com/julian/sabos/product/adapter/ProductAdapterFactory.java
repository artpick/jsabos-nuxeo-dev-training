package com.julian.sabos.product.adapter;

import com.julian.sabos.pv.schema.ProductVisualSchemaInfos;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class ProductAdapterFactory implements DocumentAdapterFactory {

    private static final String DUBLINCORE = "dublincore";
    private static final String PRODUCT = "Product";

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> itf) {
        if (PRODUCT.equals(doc.getType())
                && doc.hasSchema(DUBLINCORE)
                && doc.hasSchema(ProductVisualSchemaInfos.SCHEMA_NAME)){
            return new ProductAdapter(doc);
        }else{
            return null;
        }
    }
}
