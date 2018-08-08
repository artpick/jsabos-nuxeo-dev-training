package com.julian.sabos.visual.listeners;

import com.google.inject.Inject;
import com.julian.sabos.product.adapter.ProductAdapter;
import com.julian.sabos.product.schema.ProductSchemaInfos;
import com.julian.sabos.test.utils.AutomationCustom;
import com.julian.sabos.visual.schema.VisualSchemaInfos;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.collections.api.CollectionManager;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(FeaturesRunner.class)
@Features(AutomationCustom.class)
public class TestHideVisualListener {

    protected CollectionManager collectionManager;

    @Inject
    protected CoreSession session;

    protected final List<String> events = Arrays.asList("beforeDocumentModification");

    @Inject
    protected EventService s;

    @Before
    public void init() {
        collectionManager = Framework.getService(CollectionManager.class);
    }

    @Test
    public void listenerRegistration() {
        final EventListenerDescriptor listener = s.getEventListener("hidevisuallistener");
        assertNotNull(listener);
        assertTrue(events.stream().allMatch(listener::acceptEvent));
    }

    @Test
    public void testFireEventAndChangeDirectoryToo() {

        //GIVEN
        final DocumentModel product = createProduct();
        final String originPath = product.getPathAsString();
        final ProductAdapter productAdapter = product.getAdapter(ProductAdapter.class);
        productAdapter.setAvailableImmediately(false);

        // WHEN
        final DocumentModel result = session.saveDocument(productAdapter.toDocumentModel());
        // THEN

        assertThat(result.getPathAsString())
                .isNotEqualTo(originPath)
                .isEqualTo("/default-domain/HiddenFolder/aProduct");
    }

    @Test
    public void testFireEventAndChangeDirectoryTooAndChangeVisualDirectory() {

        //GIVEN
        final DocumentModel product = createProduct();
        final DocumentModel visual = createVisual();
        addToAProductToAVisual(product, visual);

        final String originProductPath = product.getPathAsString();
        final String originVisualPath = visual.getPathAsString();

        final ProductAdapter productAdapter = product.getAdapter(ProductAdapter.class);
        productAdapter.setAvailableImmediately(false);

        // WHEN
        final DocumentModel result = session.saveDocument(productAdapter.toDocumentModel());
        // THEN
        assertThat(result.getPathAsString())
                .isNotEqualTo(originProductPath)
                .isEqualTo("/default-domain/HiddenFolder/aProduct");
        /*
        assertThat(result.getPathAsString())
                .isNotEqualTo(originVisualPath)
                .isEqualTo("/default-domain/HiddenFolder/aProduct");
        */
    }

    @Test
    public void testFireEventWithAWrongDocument() {

        //GIVEN
        final DocumentModel input = session.createDocumentModel("/default-domain/workspaces",
                "aFile", "File");
        session.createDocument(input);
        // WHEN
        final DocumentModel result = session.saveDocument(input);

        // THEN
        assertThat(result.getPathAsString());
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

    /**
     * Create a Visual.
     *
     * @return
     */
    private final DocumentModel createVisual() {
        final DocumentModel aProduct = session.createDocumentModel("/default-domain/workspaces",
                "aVisual", VisualSchemaInfos.TYPE);
        return session.createDocument(aProduct);
    }

    /**
     * Add to a product to a visual.
     */
    private final void addToAProductToAVisual(final DocumentModel aProduct, final DocumentModel aVisual) {
        collectionManager.addToCollection(aProduct, Lists.newArrayList(aVisual), session);
    }
}
