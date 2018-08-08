package com.julian.sabos.product.adapter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.*;

import javax.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@Deploy({"com.julian.sabos.jsabos-nuxeo-dev-training-core"})
@PartialDeploy(bundle = "studio.extensions.jsabos-SANDBOX", extensions = {TargetExtensions.ContentModel.class})
public class TestProductAdapter {
    private static final String FRANCE = "France";
    private static final String AUCHAN = "Auchan";
    private static final String doctype = "Product";
    private static final String testTitle = "My Adapter Title";
    private static final Long testPrice = 120L;

    @Inject
    CoreSession session;

    @Test
    public void shouldCallTheAdapter() {


        final DocumentModel doc = session.createDocumentModel("/", "test-adapter", doctype);
        final ProductAdapter adapter = doc.getAdapter(ProductAdapter.class);

        Assert.assertNotNull("The adapter can't be used on the " + doctype + " document type", adapter);

        adapter.setTitle(testTitle);
        adapter.setPrice(testPrice);
        adapter.setDistributorSellLocation(FRANCE);
        adapter.setDistributorName(AUCHAN);

        adapter.create();
        // session.save() is only needed in the context of unit tests
        session.save();

        Assert.assertEquals("Product title does not match when using the adapter", testTitle, adapter.getTitle());
        Assert.assertEquals("Product price does not match when using the adapter", testPrice, adapter.getPrice());
        Assert.assertEquals("Product distributor sell location does not match when using the adapter", FRANCE, adapter.getDistributorSellLocation());
        Assert.assertEquals("Product distributor name does not match when using the adapter", AUCHAN, adapter.getDistributorName());
    }
}
