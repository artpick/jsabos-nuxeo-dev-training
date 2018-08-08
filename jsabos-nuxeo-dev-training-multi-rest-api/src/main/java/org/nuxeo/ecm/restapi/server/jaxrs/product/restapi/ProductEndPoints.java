package org.nuxeo.ecm.restapi.server.jaxrs.product.restapi;

import com.julian.sabos.product.services.VATService;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.DefaultObject;
import org.nuxeo.runtime.api.Framework;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@WebObject(type = "product")
@Produces(MediaType.APPLICATION_JSON)
public class ProductEndPoints extends DefaultObject {

    @GET
    @Path("computePrice/{productRefId}")
    public Object getWorkflowInstance(@PathParam("productRefId") String productRefId) {
        try {
            final VATService vatService = Framework.getService(VATService.class);
            final DocumentModel productInstance = getContext().getCoreSession().getDocument(new IdRef(productRefId));
            final DocumentModel tmp = vatService.computePrice(productInstance);
            return tmp;
        } catch (final NuxeoException e) {
            e.addInfo("Can not get product instance with the id: " + productRefId);
            throw e;
        }
    }
}
