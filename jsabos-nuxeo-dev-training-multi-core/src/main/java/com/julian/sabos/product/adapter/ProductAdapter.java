package com.julian.sabos.product.adapter;

import com.julian.sabos.pv.schema.ProductVisualSchemaInfos;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

import java.io.Serializable;
import java.util.Map;

import static com.julian.sabos.pv.schema.ProductVisualSchemaInfos.*;

/**
 *
 */
public class ProductAdapter {
    protected final DocumentModel doc;

    protected static final String DC_TITLE = "dc:title";
    protected static final String DC_DESCRIPTION = "dc:description";
    protected static final String PV_PRP_PRICE = ProductVisualSchemaInfos.PV_PRP_PRICE;

    public ProductAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    public DocumentModel toDocumentModel() {
        return this.doc;
    }

    // Basic methods
    //
    // Note that we voluntarily expose only a subset of the DocumentModel API in this adapter.
    // You may wish to complete it without exposing everything!
    // For instance to avoid letting people change the document state using your adapter,
    // because this would be handled through workflows / buttons / events in your application.
    //
    public void create() {
        doc.getCoreSession().createDocument(doc);
    }

    public void save() {
        doc.getCoreSession().saveDocument(doc);
    }

    public DocumentRef getParentRef() {
        return doc.getParentRef();
    }

    // Technical properties retrieval
    public String getId() {
        return doc.getId();
    }

    public String getName() {
        return doc.getName();
    }

    public String getPath() {
        return doc.getPathAsString();
    }

    public String getState() {
        return doc.getCurrentLifeCycleState();
    }

    // Metadata get / set
    public String getTitle() {
        return doc.getTitle();
    }

    public void setTitle(String value) {
        doc.setPropertyValue(DC_TITLE, value);
    }

    public String getDescription() {
        return (String) doc.getPropertyValue(DC_DESCRIPTION);
    }

    public void setDescription(String value) {
        doc.setPropertyValue(DC_DESCRIPTION, value);
    }

    public void setPrice(Long value) {
        doc.setPropertyValue(PV_PRP_PRICE, value);
    }

    public Long getPrice() {
        return (Long) doc.getPropertyValue(PV_PRP_PRICE);
    }


    /**
     *
     * @param value
     */
    public void setAvailableImmediately(final Boolean value) {
        doc.setPropertyValue(PRP_AVAILABLE_IMMEDIATELY, value);
    }

    /**
     *
     * @return
     */
    public Boolean getAvailableImmediately() {
        return (Boolean) doc.getPropertyValue(PRP_AVAILABLE_IMMEDIATELY);
    }

    /**
     *
     * @param value
     */
    public void setDistributorSellLocation(String value) {
        final Map<String, Object> distributorSellLocation = (Map<String, Object>) doc.getPropertyValue(PV_DISTRIBUTOR);
        distributorSellLocation.put(PV_DISTRIBUTOR_PRP_SELL_LOCATION, value);
        doc.setPropertyValue(PV_DISTRIBUTOR, (Serializable) distributorSellLocation);
    }

    /**
     *
     * @param value
     */
    public void setDistributorName(String value) {
        final Map<String, Object> distributorName = (Map<String, Object>) doc.getPropertyValue(PV_DISTRIBUTOR);
        distributorName.put(PV_DISTRIBUTOR_PRP_NAME, value);
        doc.setPropertyValue(PV_DISTRIBUTOR, (Serializable) distributorName);
    }

    /**
     *
     * @return
     */
    public String getDistributorSellLocation() {
        return (String) ((Map<String, Object>) doc.getPropertyValue(PV_DISTRIBUTOR)).get(PV_DISTRIBUTOR_PRP_SELL_LOCATION);
    }

    /**
     *
     * @return
     */
    public String getDistributorName() {
        return (String) ((Map<String, Object>) doc.getPropertyValue(PV_DISTRIBUTOR)).get(PV_DISTRIBUTOR_PRP_NAME);
    }
}
