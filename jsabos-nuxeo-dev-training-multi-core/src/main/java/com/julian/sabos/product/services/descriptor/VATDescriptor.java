package com.julian.sabos.product.services.descriptor;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("vat-rate")
public class VATDescriptor {

    @XNode("@id")
    public String id;

    @XNode("rate")
    public Long rate;

    public Long getRate() {
        return rate;
    }
}
