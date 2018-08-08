package com.julian.sabos.product.services;

import org.nuxeo.ecm.core.api.DocumentModel;

public interface VATService {

    /**
     * Compute the price of a product according to some rules.
     * @param input
     * @return
     */
    DocumentModel computePrice(DocumentModel input);
}
