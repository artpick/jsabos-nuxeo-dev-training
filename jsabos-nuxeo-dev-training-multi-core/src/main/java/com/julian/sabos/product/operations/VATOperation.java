package com.julian.sabos.product.operations;

import com.julian.sabos.product.services.VATService;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

/**
 * Compute the VAT operation.
 */
@Operation(id = VATOperation.ID, category = Constants.CAT_DOCUMENT, label = "Vat", description = "From a Product, compute a VAT.")
public class VATOperation {

    @Context
    CoreSession session;

    @Context
    VATService vatService;

    public static final String ID = "Document.VATOperation";

    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentModel document) {
        return session.saveDocument(vatService.computePrice(document));
    }

    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentRef doc) {
        final DocumentModel res = session.getDocument(doc);
        return run(res);
    }

}
