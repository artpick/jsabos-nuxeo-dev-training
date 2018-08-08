package com.julian.sabos.product.services;

import com.google.inject.Inject;
import com.julian.sabos.product.schema.ProductSchemaInfos;
import com.julian.sabos.pv.schema.ProductVisualSchemaInfos;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.*;

import static com.julian.sabos.pv.schema.ProductVisualSchemaInfos.PRP_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features({PlatformFeature.class})
@Deploy("com.julian.sabos.jsabos-nuxeo-dev-training-core")
@RepositoryConfig(init = DefaultRepositoryInit.class)
@PartialDeploy(bundle = "studio.extensions.jsabos-SANDBOX", extensions = {TargetExtensions.ContentModel.class})
public class TestVATService {

    @Inject
    protected CoreSession session;
    @Inject
    protected VATService vatservice;

    @Test
    public void testService() {
        assertNotNull(vatservice);
    }

    @Test
    public void testComputePriceCheckIfHasWeirdTypeAndDoesNotApply() {
        // GIVEN
        final DocumentModel input = new DocumentModelImpl("File");
        // WHEN
        final DocumentModel result = vatservice.computePrice(input);
        // THEN
        assertThat(result).isNull();
    }

    @Test
    public void testComputePriceCheckIfIsTypeProduct() {
        // GIVEN
        final DocumentModel input = createProduct();
        // WHEN
        final DocumentModel result = vatservice.computePrice(input);
        // THEN
        assertThat(result).isNotNull();
    }

    @Test
    @Deploy("com.julian.sabos.jsabos-nuxeo-dev-training-core.test:OSGI-INF/test-contrib1.xml")
    public void testComputePriceReturnsADocumentWithAnUpdatedPrice() {
        // GIVEN
        final DocumentModel input = createProduct();
        input.setProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE, 120L);
        // WHEN
        final DocumentModel result = vatservice.computePrice(input);
        // THEN
        assertThat(result.getProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE))
                .isNotNull()
                .isInstanceOf(Long.class)
                .isEqualTo(1200L);
    }


    @Test
    @Deploy("com.julian.sabos.jsabos-nuxeo-dev-training-core.test:OSGI-INF/test-contrib2.xml")
    public void testComputePriceReturnsADocumentWithAnUpdatedPriceDefaultValue() {
        // GIVEN
        final DocumentModel input = createProduct();
        input.setProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE, 120L);
        // WHEN
        final DocumentModel result = vatservice.computePrice(input);
        // THEN
        assertThat(result.getProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE))
                .isNotNull()
                .isInstanceOf(Long.class)
                .isEqualTo(1440L);
    }

    /**
     * Create a Product.
     *
     * @return
     */
    private final DocumentModel createProduct() {
        final DocumentModel aProduct = session.createDocumentModel("/default-domain/workspaces",
                "aProduct", ProductSchemaInfos.TYPE);
        return session.createDocument(aProduct);
    }
}
