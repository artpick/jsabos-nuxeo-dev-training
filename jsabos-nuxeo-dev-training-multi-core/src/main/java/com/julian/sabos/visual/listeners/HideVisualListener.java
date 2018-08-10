package com.julian.sabos.visual.listeners;


import com.julian.sabos.product.adapter.ProductAdapter;
import com.julian.sabos.product.schema.ProductSchemaInfos;
import com.julian.sabos.rootproduct.schema.RootProductSchemaInfos;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HideVisualListener implements EventListener {

    private final static PathRef hiddenFolderRef = new PathRef("/default-domain/HiddenFolder");

    @Override
    public void handleEvent(final Event event) {
        final EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }

        final DocumentEventContext docCtx = (DocumentEventContext) ctx;
        final DocumentModel doc = docCtx.getSourceDocument();

        if (ProductSchemaInfos.TYPE.equals(doc.getType())) {
            final Optional<ProductAdapter> adapter = Optional.ofNullable(doc.getAdapter(ProductAdapter.class));

            adapter.filter(adt -> Boolean.FALSE == adt.getAvailableImmediately())
                    .ifPresent(adt -> {
                        try (final CoreSession crs = CoreInstance.openCoreSessionSystem(null)) {
                            // Create a ref to the Hidden Folder.
                            final DocumentRef dcMdlHiddenFld = getHiddenFolder(crs).getRef();

                            final DocumentModelList dctList = getDocumentModels(doc, crs);

                            final List<DocumentRef> allThingsToMove = dctList
                                    .stream()
                                    .map(el -> el.getRef())
                                    .collect(Collectors.toList());
                            crs.move(allThingsToMove, dcMdlHiddenFld);

                        } catch (Exception ex) {
                            throw new NuxeoException("Shit happen", ex);
                        }
                    });
        }
    }

    private DocumentModelList getDocumentModels(final DocumentModel doc, final CoreSession crs) {
        try {
            return crs.query("SELECT * FROM Document WHERE collectionMember:collectionIds/* IS NOT NULL AND collectionMember:collectionIds/* = '" + doc.getId() + "'");
        } catch (final Exception ex) {
            throw new NuxeoException("Shit happen again", ex);
        }
    }

    private DocumentModel getHiddenFolder(final CoreSession session) {

        if (!session.exists(hiddenFolderRef)) {
            final DocumentModel hiddenFolderToCreate = session.createDocumentModel("/default-domain", "HiddenFolder", RootProductSchemaInfos.TYPE);
            return session.createDocument(hiddenFolderToCreate);
        }
        return session.getDocument(hiddenFolderRef);
    }

}
