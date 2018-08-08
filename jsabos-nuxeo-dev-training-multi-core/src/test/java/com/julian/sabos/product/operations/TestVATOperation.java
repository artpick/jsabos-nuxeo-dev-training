package com.julian.sabos.product.operations;

import com.google.common.collect.Lists;
import com.julian.sabos.product.schema.ProductSchemaInfos;
import com.julian.sabos.pv.schema.ProductVisualSchemaInfos;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.*;

import javax.inject.Inject;

import java.util.List;

import static com.julian.sabos.pv.schema.ProductVisualSchemaInfos.PRP_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(FeaturesRunner.class)
@Features(AutomationFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy({"com.julian.sabos.jsabos-nuxeo-dev-training-core", "com.julian.sabos.jsabos-nuxeo-dev-training-core.test:OSGI-INF/test-contrib1.xml"})
@PartialDeploy(bundle = "studio.extensions.jsabos-SANDBOX", extensions = {TargetExtensions.ContentModel.class})
public class TestVATOperation {

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;

    private DocumentModel target1;

    @Before
    public void setUp() {
        target1 = session.createDocumentModel("/", "aProduct", ProductSchemaInfos.TYPE);
        target1 = session.createDocument(target1);
        session.save();
    }

    @Test
    public void callTheOperationAndGetTheGoodResult() throws OperationException {
        // GIVEN
        target1.setProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE, 32);
        session.saveDocument(target1);

        final OperationContext ctx = new OperationContext(session);
        ctx.setInput(target1);
        // WHEN
        final DocumentModel doc = (DocumentModel) automationService.run(ctx, VATOperation.ID);

        // THEN
        assertThat(doc.getProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE))
                .isNotNull()
                .isInstanceOf(Long.class)
                .isEqualTo(320L);
    }

    @Test
    public void callTheOperationWithDocRefAndGetTheGoodResult() throws OperationException {
        // GIVEN
        target1.setProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE, 32);
        session.saveDocument(target1);

        final OperationContext ctx = new OperationContext(session);
        ctx.setInput(target1.getRef());
        // WHEN
        final DocumentModel doc = (DocumentModel) automationService.run(ctx, VATOperation.ID);

        // THEN
        assertThat(doc.getProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE))
                .isNotNull()
                .isInstanceOf(Long.class)
                .isEqualTo(320L);
    }


    @Test
    public void callTheOperationWithNoValueAndGetTheGoodResult() throws OperationException {
        // GIVEN
        final OperationContext ctx = new OperationContext(session);
        ctx.setInput(target1);
        // WHEN
        final DocumentModel doc = (DocumentModel) automationService.run(ctx, VATOperation.ID);

        // THEN
        assertThat(doc.getProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE))
                .isNotNull()
                .isInstanceOf(Long.class)
                .isEqualTo(0L);
    }

    @Test
    public void callTheOperationWithDocumentListNoValueAndGetTheGoodResult() throws OperationException {
        // GIVEN
        target1.setProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE, 32);
        session.saveDocument(target1);

        final OperationContext ctx = new OperationContext(session);
        ctx.setInput(new DocumentModelListImpl(Lists.newArrayList(target1)));
        // WHEN
        final DocumentModelList docLst = (DocumentModelList) automationService.run(ctx, VATOperation.ID);

        // THEN
        assertThat(docLst).isNotNull();
        assertThat(docLst.get(0).getProperty(ProductVisualSchemaInfos.SCHEMA_NAME, PRP_PRICE))
                .isNotNull()
                .isInstanceOf(Long.class)
                .isEqualTo(320L);
    }

}
