package com.julian.sabos.product.services;

import com.julian.sabos.product.adapter.ProductAdapter;
import com.julian.sabos.product.services.descriptor.VATDescriptor;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class VATServiceImpl extends DefaultComponent implements VATService {

    // VAT static value.
    private static Long VAT;

    @Override
    public DocumentModel computePrice(final DocumentModel input) {
        final ProductAdapter product = input.getAdapter(ProductAdapter.class);
        if (product!=null) {
            product.setPrice(computeVAT(product.getPrice()));
            return product.toDocumentModel();
        }
        return null;
    }

    /**
     * Compute VAT
     *
     * @param price
     * @return
     */
    private static Long computeVAT(final Long price) {
        return price * VAT;
    }

    @Override
    public void activate(ComponentContext context) {
        VAT = -10L;
    }

    @Override
    public void deactivate(ComponentContext context) {
        VAT = null;
    }


    @Override
    public void start(ComponentContext context) {
        super.start(context);
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        final VATDescriptor vatDescriptor = (VATDescriptor) contribution;
        VAT = vatDescriptor.getRate();
    }
}
