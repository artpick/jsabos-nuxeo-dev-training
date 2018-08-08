package com.julian.sabos.visual.listeners;


import com.julian.sabos.product.adapter.ProductAdapter;
import com.julian.sabos.product.schema.ProductSchemaInfos;
import com.julian.sabos.rootproduct.schema.RootProductSchemaInfos;
import org.nuxeo.ecm.collections.api.CollectionManager;
import org.nuxeo.ecm.collections.core.adapter.Collection;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HideVisualListener implements EventListener {

    private DocumentModel hiddenFolder;
    protected CollectionManager collectionManager;

    @Override
    public void handleEvent(final Event event) {
        final EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }
        collectionManager = Framework.getService(CollectionManager.class);

        final DocumentEventContext docCtx = (DocumentEventContext) ctx;
        final DocumentModel doc = docCtx.getSourceDocument();

        if (ProductSchemaInfos.TYPE.equals(doc.getType())) {
            final Optional<ProductAdapter> adapter = Optional.ofNullable(doc.getAdapter(ProductAdapter.class));
            adapter.filter(adt -> Boolean.FALSE == adt.getAvailableImmediately())
                    .ifPresent(adt -> {
                        try (final CoreSession crs = CoreInstance.openCoreSession(null)) {
                            // Create a ref to the Hidden Folder.
                            final DocumentRef dcMdlHiddenFld = getHiddenFolder(crs).getRef();
                            // Make it like a collection
                            final Collection prop = new Collection(adt.toDocumentModel());
                            // Get all the associated Visuals
                            final List<DocumentRef> allThingsToMove = prop.getCollectedDocumentIds()
                                    .stream()
                                    .map(el -> new IdRef(el))
                                    .collect(Collectors.toList());
                            // Add the Product to the Visuals to move
                            allThingsToMove.add(adt.toDocumentModel().getRef());
                            // Move all the stuff.
                            crs.move(allThingsToMove, dcMdlHiddenFld);

                        }
                    });
        }
    }

    private DocumentModel getHiddenFolder(final CoreSession session) {
        if (hiddenFolder == null) {
            try {
                hiddenFolder = session.getDocument(new IdRef("/default-domain/HiddenFolder"));
            } catch (Exception ex) {
                final DocumentModel hiddenFolderToCreate = session.createDocumentModel("/default-domain", "HiddenFolder", RootProductSchemaInfos.TYPE);
                this.hiddenFolder = session.createDocument(hiddenFolderToCreate);
                session.save();
            }
        }
        return hiddenFolder;
    }

}
